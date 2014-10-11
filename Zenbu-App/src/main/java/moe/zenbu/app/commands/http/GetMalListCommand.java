package moe.zenbu.app.commands.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.HttpUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetMalListCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(GetMalListCommand.class);

    private static final String API_URL = "http://myanimelist.net/api/";
    private static final String USER_LIST_URL = "http://myanimelist.net/malappinfo.php?u=";
    private static final String API_KEY = "";

    private static final SqlSession db = DbUtils.getSqlSession();

    private Setting malUsername;
    private Setting malPassword;

    private List<Anime> malList = new ArrayList<>();

    @Override
    protected void initCommand()
    {
        malUsername = db.selectOne("db.mappers.settingmapper.selectSetting", "mal_username");
        malPassword = db.selectOne("db.mappers.settingmapper.selectSetting", "mal_password");
    }

    @Override
    protected void perform(final Wave wave)
    {
        log.info("Getting user list from mal");

        HttpGet get = new HttpGet(USER_LIST_URL + malUsername.getValue() + "&status=all");
        get.addHeader("User-Agent", API_KEY);

        CloseableHttpResponse response = HttpUtils.executeHttpRequest(get);
        Document doc = null;
        try
        {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response.getEntity().getContent());
        }
        catch(Exception e)
        {
            log.error("Could not get user list from mal for user {}", malUsername.getValue(), e);
        }
        
        HttpClientUtils.closeQuietly(response);

        NodeList list = doc.getElementsByTagName("anime");

        for(int i = 0; i < list.getLength(); i++)
        {
            AnimeUserData userData = new AnimeUserData();

            Element e = (Element) list.item(i);

            int malId = Integer.parseInt(e.getElementsByTagName("series_animedb_id").item(0).getTextContent());
            userData.setProgress(Integer.parseInt(e.getElementsByTagName("my_watched_episodes").item(0).getTextContent()));
            userData.setScore(new Integer(e.getElementsByTagName("my_score").item(0).getTextContent()).doubleValue());
            switch(Integer.parseInt(e.getElementsByTagName("my_status").item(0).getTextContent()))
            {
                case 1:
                    userData.setStatus(AnimeUserData.STATUS_CURRENT);
                    break;
                case 2:
                    userData.setStatus(AnimeUserData.STATUS_COMPLETED);
                    break;
                case 3:
                    userData.setStatus(AnimeUserData.STATUS_ON_HOLD);
                    break;
                case 4:
                    userData.setStatus(AnimeUserData.STATUS_DROPPED);
                    break;
                case 6:
                    userData.setStatus(AnimeUserData.STATUS_PLANNED);
                    break;
            }
            Anime anime = db.selectOne("db.mappers.animemapper.selectAnimeByMalId", malId);
            if(anime == null)
            {
                log.info("Anime with mal id {}, does not exist in database", malId);
                break;
            }
            anime.setUserData(userData);
            userData.setAnime(anime);
            userData.setLastChanged(new Date(Long.parseLong(e.getElementsByTagName("my_last_updated").item(0).getTextContent()) * 1000L));

            log.trace("Anime {} has progress {} and lastChanged {}", anime.getSelectedTitle(), anime.getUserData().getProgress(), anime.getUserData().getLastChanged());

            malList.add(anime);
        }

        db.close();
        
        sendWave(HttpCommandWaves.LIST_FETCHED, WaveData.build(JrebirthUtils.OBJECTARR_WAVE_ITEM, new Object[]{"mal", malList}));
        
        log.info("Got user list from mal, size is {}", malList.size());
    }
}

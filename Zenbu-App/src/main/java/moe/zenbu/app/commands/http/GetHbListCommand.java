package moe.zenbu.app.commands.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.HttpUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetHbListCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(GetHbListCommand.class);

    private static final String HB_API_URL = "https://hbrd-v1.p.mashape.com/";
    private static final String API_KEY = "";

    private static final SqlSession db = DbUtils.getSqlSession();

    private Setting hbUsername;
    private Setting hbPassword;

    private String authToken;

    private List<Anime> hbList = new ArrayList<>();

    @Override
    protected void initCommand()
    {
        hbUsername = db.selectOne("db.mappers.settingmapper.selectSetting", "hb_username");
        hbPassword = db.selectOne("db.mappers.settingmapper.selectSetting", "hb_password");
    }

    @Override
    protected void perform(final Wave wave)
    {
        log.info("Getting user list from hb");

        HttpGet get = new HttpGet(HB_API_URL + "users/" + hbUsername.getValue() + "/library");
        get.addHeader("X-Mashape-Key", API_KEY);

        CloseableHttpResponse response = HttpUtils.executeHttpRequest(get);
        List<Map> list = null;
        try
        {
            list = (List<Map>) JSONValue.parse(IOUtils.toString(response.getEntity().getContent()));
        }
        catch(Exception e)
        {
            log.error("Could not get list from hb", e);
        }

        HttpClientUtils.closeQuietly(response);

        for(Map map : list)
        {
            Map entry = (Map) map.get("anime");
            Map rating = (Map) map.get("rating");

            AnimeUserData userData = new AnimeUserData();
            long hbId = (long) entry.get("id");
            Anime anime = db.selectOne("db.mappers.animemapper.selectAnimeByHbId", hbId);

            if(anime == null)
            {
                log.info("Anime with hb id {}, does not exist in database", hbId);
                continue;
            }
            userData.setAnime(anime);
            anime.setUserData(userData);

            SimpleDateFormat zuluTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            zuluTime.setTimeZone(TimeZone.getTimeZone("UTC"));
            try
            {
                userData.setLastChanged(zuluTime.parse((String) map.get("last_watched")));
            }
            catch(ParseException e)
            {
                log.error("Could not parse time", e);
            }
            userData.setProgress(((Long) map.get("episodes_watched")).intValue());
            switch((String) map.get("status"))
            {
                case "currently-watching":
                    userData.setStatus(AnimeUserData.STATUS_CURRENT);
                    break;
                case "plan-to-watch":
                    userData.setStatus(AnimeUserData.STATUS_PLANNED);
                    break;
                case "completed":
                    userData.setStatus(AnimeUserData.STATUS_COMPLETED);
                    break;
                case "on-hold":
                    userData.setStatus(AnimeUserData.STATUS_ON_HOLD);
                    break;
                case "dropped":
                    userData.setStatus(AnimeUserData.STATUS_DROPPED);
                    break;
            }
            if(rating.get("value") == null)
            {
                userData.setScore(0.0);
            }
            else
            {
                userData.setScore(Double.valueOf((String) rating.get("value")) * 2.0);
            }

            hbList.add(anime);

            log.trace("Anime {} has progress {} and lastChanged {}", anime.getSelectedTitle(), anime.getUserData().getProgress(), anime.getUserData().getLastChanged());
        }

        db.close();

        sendWave(HttpCommandWaves.LIST_FETCHED, WaveData.build(JrebirthUtils.OBJECTARR_WAVE_ITEM, new Object[]{"hb", hbList}));

        log.info("Got user list from hb, size is {}", hbList.size());
    }
}

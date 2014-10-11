package moe.zenbu.app.commands.http;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.HttpUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMalListCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(SendMalListCommand.class);

    private static final String API_URL = "http://myanimelist.net/api/";
    private static final String API_KEY = "";

    private static final SqlSession db = DbUtils.getSqlSession();

    protected void initCommand()
    {
    }

    @Override
    protected void perform(Wave wave)
    {
        log.info("Sending local list to Mal");

        Object[] data = wave.getData(JrebirthUtils.OBJECTARR_WAVE_ITEM).getValue();

        List<Anime> localList = (List<Anime>) data[0];
        List<Anime> remoteList = (List<Anime>) data[1];

        String login = ((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "mal_username")).getValue() + ":" + ((Setting) db.selectOne("db.mapper.settingmapper.selectSetting", "mal_password")).getValue();
        String authToken = Base64.getEncoder().encodeToString(login.getBytes(StandardCharsets.UTF_8));

        for(Anime a : localList)
        {
            HttpPost post;
            List<NameValuePair> params = new ArrayList<>();

            if(remoteList.contains(a))
            {
                Anime b = remoteList.get(ListUtils.indexOf(remoteList, an -> a.equals(an)));

                // Update
                if(a.getUserData().isUserDataDifferent(b.getUserData()))
                {
                    log.debug("Updating anime {} with progress={}, status=\"{}\", score={}", a.getSelectedTitle(), a.getUserData().getProgress(), a.getUserData().getStatus(), a.getUserData().getScore());

                    post = new HttpPost(API_URL + "animelist/update/" + a.getMalId() + ".xml");

                }
                else
                {
                    continue;
                }
            }
            // Add
            else
            {
                log.debug("Adding anime {} with progress={}, status=\"{}\", score={}", a.getSelectedTitle(), a.getUserData().getProgress(), a.getUserData().getStatus(), a.getUserData().getScore());

                post = new HttpPost(API_URL + "animelist/add/" + a.getMalId() + ".xml");
            }

            String progress = String.valueOf(a.getUserData().getProgress());
            String score = String.valueOf(new Double(a.getUserData().getScore()).intValue());
            int status = -1;
            switch(a.getUserData().getStatus())
            {
                case AnimeUserData.STATUS_CURRENT:
                    status = 1;
                    break;
                case AnimeUserData.STATUS_COMPLETED:
                    status = 2;
                    break;
                case AnimeUserData.STATUS_PLANNED:
                    status = 6;
                    break;
                case AnimeUserData.STATUS_ON_HOLD:
                    status = 3;
                    break;
                case AnimeUserData.STATUS_DROPPED:
                    status = 4;
                    break;
            }

            //@formatter:off
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<entry>\n"
                + " <episode>" + progress + "</episode>\n"
                + " <status>" + status + "</status>\n"
                + " <score>" + score + "</score>\n"
                + " <downloaded_episodes></downloaded_episodes>\n"
                + " <storage_type></storage_type>\n"
                + " <storage_value></storage_value>\n"
                + " <times_rewatched></times_rewatched>\n"
                + " <rewatch_value></rewatch_value>\n"
                + " <date_start></date_start>\n"
                + " <date_finish></date_finish>\n"
                + " <priority></priority>\n"
                + " <enable_discussion></enable_discussion>\n"
                + " <enable_rewatching></enable_rewatching>\n"
                + " <comments></comments>\n"
                + " <fansub_group></fansub_group>\n"
                + " <tags></tags>\n"
                + "</entry>";
            //@formatter:on

            params.add(new BasicNameValuePair("data", xml));

            post = HttpUtils.setEntityQuietly(post, params);
            post.addHeader("User-Agent", API_KEY);
            post.addHeader("Authorization", "Basic " + authToken);

            CloseableHttpResponse response = HttpUtils.executeHttpRequest(post);

            log.debug("Mal responded with message {} and code {}", response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());

            HttpClientUtils.closeQuietly(response);
        };

        remoteList.forEach(b ->
        {
            if(!localList.contains(b))
            {
                log.debug("Deleting anime {}", b.getSelectedTitle());

                HttpPost post = new HttpPost(API_URL + "animelist/delete/" + b.getMalId() + ".xml");
                post.addHeader("User-Agent", API_KEY);
                post.addHeader("Authorization", "Basic " + authToken);

                CloseableHttpResponse response = HttpUtils.executeHttpRequest(post);

                log.debug("Hb responded with message {} and code {}", response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());

                HttpClientUtils.closeQuietly(response);
            }
        });

        db.close();

        sendWave(HttpCommandWaves.LIST_SENT);
    }
}

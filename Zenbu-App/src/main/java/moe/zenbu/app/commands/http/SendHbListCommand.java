package moe.zenbu.app.commands.http;

import java.util.ArrayList;
import java.util.List;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.util.HttpUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendHbListCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(SendHbListCommand.class);

    private static final String HB_API_URL = "https://hbrd-v1.p.mashape.com/";
    private static final String API_KEY = "";

    @Override
    protected void initCommand()
    {
    }

    @Override
    protected void perform(Wave wave)
    {
        log.info("Sending local list to Hb");

        Object[] data = wave.getData(JrebirthUtils.OBJECTARR_WAVE_ITEM).getValue();

        List<Anime> localList = (List<Anime>) data[0];
        List<Anime> remoteList = (List<Anime>) data[1];

        String authToken = HttpUtils.getHbAuthToken(API_KEY);

        for(Anime a : localList)
        {
            HttpPost post;
            List<NameValuePair> params = new ArrayList<>();

            int index = ListUtils.indexOf(remoteList, an -> a.equals(an));
            Anime b = null;
            if(index != -1)
            {
                b = remoteList.get(ListUtils.indexOf(remoteList, an -> a.equals(an)));
            }

            if(index == -1 || a.getUserData().isUserDataDifferent(b.getUserData()))
            {
                log.debug("Adding/updaing anime {} with progress={}, status=\"{}\", score={}", a.getSelectedTitle(), a.getUserData().getProgress(), a.getUserData().getStatus(), a.getUserData().getScore());

                post = new HttpPost(HB_API_URL + "libraries/" + a.getHbId());
                params.add(new BasicNameValuePair("episodes_watched", String.valueOf(a.getUserData().getProgress())));
                params.add(new BasicNameValuePair("rating", String.valueOf(a.getUserData().getScore() / 2.0))); // TODO: Add support for different scoring values

                String status = "";
                switch(a.getUserData().getStatus())
                {
                    case AnimeUserData.STATUS_CURRENT:
                        status = "currently-watching";
                        break;
                    case AnimeUserData.STATUS_COMPLETED:
                        status = "completed";
                        break;
                    case AnimeUserData.STATUS_PLANNED:
                        status = "plan-to-watch";
                        break;
                    case AnimeUserData.STATUS_ON_HOLD:
                        status = "on-hold";
                        break;
                    case AnimeUserData.STATUS_DROPPED:
                        status = "dropped";
                        break;
                }
                params.add(new BasicNameValuePair("status", status));
                params.add(new BasicNameValuePair("auth_token", authToken));

                post = HttpUtils.setEntityQuietly(post, params);
                post.addHeader("X-Mashape-Key", API_KEY);

                CloseableHttpResponse response = HttpUtils.executeHttpRequest(post);

                log.debug("Hb responded with message {} and code {}", response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());

                HttpClientUtils.closeQuietly(response);
            }
            else
            {
                continue;
            }
        };

        remoteList.forEach(b ->
        {
            if(!localList.contains(b))
            {
                log.debug("Deleting anime {}", b.getSelectedTitle());

                HttpPost post = new HttpPost(HB_API_URL + "libraries/" + b.getHbId() + "/remove");
                List<NameValuePair> params = new ArrayList<>();

                params.add(new BasicNameValuePair("auth_token", authToken));
                post = HttpUtils.setEntityQuietly(post, params);
                post.addHeader("X-Mashape-Key", API_KEY);

                CloseableHttpResponse response = HttpUtils.executeHttpRequest(post);

                log.debug("Hb responded with message {} and code {}", response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());

                HttpClientUtils.closeQuietly(response);
            }
        });

        sendWave(HttpCommandWaves.LIST_SENT);
    }
}

package moe.zenbu.app.commands.http;

import java.io.File;
import java.io.FileOutputStream;

import moe.zenbu.app.beans.Anime;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchCoverCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(FetchCoverCommand.class);

    protected void initCommand()
    {
    }

    @Override
    protected void perform(final Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        Anime anime = wave.getData(JrebirthUtils.ANIME_WAVE_ITEM).getValue();
        Setting preferredCoverProvider = db.selectOne("db.mappers.settingmapper.selectSetting", "preferred_cover_provider");

        File coverDir = new File("zenbu/app/covers/" + anime.getId());
        if(!coverDir.exists())
        {
            coverDir.mkdirs();
        }

        HttpGet httpGet = null;

        switch(preferredCoverProvider.getValue())
        {
            case "mal":
                httpGet = new HttpGet(anime.getMalImageUrl());
                break;
            case "hb":
                httpGet = new HttpGet(anime.getHbImageUrl());
                break;
            case "al":
                httpGet = new HttpGet(anime.getAlImageUrl());
                break;
        }

        CloseableHttpResponse httpResponse = HttpUtils.executeHttpRequest(httpGet);
        try
        {
            IOUtils.copy(httpResponse.getEntity().getContent(), new FileOutputStream(coverDir + "/cover.jpg"));
        }
        catch(Exception e)
        {
            log.error("Could not write cover image to file for anime {} with url {}", anime.getSelectedTitle(), httpGet.getURI(), e);
        }

        HttpClientUtils.closeQuietly(httpResponse);

        db.close();
        
        sendWave(HttpCommandWaves.COVER_FETCHED);

        log.debug("Cover image for anime {} successfully downloaded from url {}", anime.getSelectedTitle(), httpGet.getURI());
    }
}

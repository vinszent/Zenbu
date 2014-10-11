package moe.zenbu.app.commands.collection;

import java.awt.Desktop;
import java.io.File;

import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayEpisodeCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(PlayEpisodeCommand.class);

    @Override
    protected void initCommand()
    {
    }

    @Override
    protected void perform(final Wave wave)
    {
        Episode episode = wave.getData(JrebirthUtils.EPISODE_WAVE_ITEM).getValue();
        
        log.info("Playing episode {} for anime {}", episode.getEpisode(), episode.getUserData().getAnime().getSelectedTitle());

        if(Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().open(new File(episode.getFilepath()));
            }
            catch(Exception e)
            {
                log.error("Could not play episode {} for anime {}", episode.getEpisode(), episode.getUserData().getAnime().getSelectedTitle(), e);
            }
        }
    }
}

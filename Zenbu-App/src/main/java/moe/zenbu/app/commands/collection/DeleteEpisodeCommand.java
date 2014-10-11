package moe.zenbu.app.commands.collection;

import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteEpisodeCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(DeleteEpisodeCommand.class);

    protected void initCommand()
    {
    }

    @Override
    protected void perform(Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        Episode episode = wave.getData(JrebirthUtils.EPISODE_WAVE_ITEM).getValue();

        log.info("Deleting episode {} for series {}", episode.getEpisode(), episode.getUserData().getAnime().getSelectedTitle());

        db.delete("db.mappers.episodemapper.deleteEpisode", episode);

        db.commit();
        db.close();
    }
}

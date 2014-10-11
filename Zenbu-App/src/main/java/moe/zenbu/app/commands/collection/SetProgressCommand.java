package moe.zenbu.app.commands.collection;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetProgressCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(SetProgressCommand.class);

    protected void initCommand()
    {
    }

    @Override
    protected void perform(final Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        Anime anime = wave.getData(JrebirthUtils.ANIME_WAVE_ITEM).getValue();
        int progress = wave.getData(JrebirthUtils.INTEGER_WAVE_ITEM).getValue();

        anime.getUserData().setProgress(progress);

        db.update("db.mappers.animeuserdatamapper.updateAnimeUserData", anime.getUserData());
        db.commit();
        db.close();

        log.debug("Set progress for anime {} to {}", anime.getSelectedTitle(), progress);

        // TODO: Send wave when progress is updated
    }

}

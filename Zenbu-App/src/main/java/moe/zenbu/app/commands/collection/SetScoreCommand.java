package moe.zenbu.app.commands.collection;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetScoreCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(SetScoreCommand.class);

    protected void initCommand()
    {
    }

    @Override
    protected void perform(Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        Anime anime = wave.getData(JrebirthUtils.ANIME_WAVE_ITEM).getValue();
        double score = wave.getData(JrebirthUtils.DOUBLE_WAVE_ITEM).getValue();

        anime.getUserData().setScore(score);

        db.update("db.mappers.animeuserdatamapper.updateAnimeUserData", anime.getUserData());
        db.commit();
        db.close();

        log.debug("Set score for anime {} to {}", anime.getSelectedTitle(), score);

        // TODO: Send wave when score is updated
    }
}

package moe.zenbu.app.commands.collection;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.HistoryItem;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesoptions.SeriesOptionsPopupWaves;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteSeriesCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(DeleteSeriesCommand.class);

    protected void initCommand()
    {
    }

    @Override
    protected void perform(final Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        Anime anime = wave.getData(JrebirthUtils.ANIME_WAVE_ITEM).getValue();

        log.info("Deleting user data for anime {}", anime.getSelectedTitle());

        db.delete("db.mappers.animeuserdatamapper.deleteAnimeUserData", anime.getUserData());

        HistoryItem history = new HistoryItem();
        history.setAnimeId(anime.getId());
        history.setAction(HistoryItem.ACTION_DELETE);

        db.insert("db.mappers.historyitemmapper.insertHistoryItem", history);

        db.commit();
        db.close();

        sendWave(SeriesOptionsPopupWaves.HIDE_SERIES_OPTIONS);
        sendWave(CollectionPageWaves.REFRESH);
    }
}

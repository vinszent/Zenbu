package moe.zenbu.app.commands.collection;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.HistoryItem;
import moe.zenbu.app.commands.http.FetchCoverCommand;
import moe.zenbu.app.commands.http.HttpCommandWaves;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddSeriesCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(AddSeriesCommand.class);

    @Override
    protected void initCommand()
    {
    }

    @Override
    protected void perform(final Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        AnimeUserData userData = wave.getData(JrebirthUtils.ANIME_USER_DATA_WAVE_ITEM).getValue();
        Anime anime = userData.getAnime();

        HistoryItem history = new HistoryItem();
        history.setAnimeId(anime.getId());
        history.setAction(HistoryItem.ACTION_ADD);
        db.insert("db.mappers.historyitemmapper.insertHistoryItem", history);

        log.debug("Adding anime {}", anime.getSelectedTitle());

        if(((int) db.selectOne("db.mappers.animeuserdatamapper.countAnimeUserData", userData)) < 1)
        {
            db.insert("db.mappers.animeuserdatamapper.insertAnimeUserDataIfNotExists", userData);
            log.debug("Inserted anime");
        }

        db.commit();

        userData.getEpisodes().forEach(ep ->
        {
            db.insert("db.mappers.episodemapper.insertEpisode", ep);
            db.commit();
            ep.getVideoFlags().forEach(v -> db.insert("db.mappers.videoflagmapper.insertVideoFlag", v));
            ep.getAudioFlags().forEach(a -> db.insert("db.mappers.audioflagmapper.insertAudioFlag", a));
            ep.getSubgroups().forEach(s -> db.insert("db.mappers.subgroupmapper.insertSubgroup", s));
            db.commit();
        });

        log.info("Added new anime {} to collection", anime.getSelectedTitle());

        sendWave(CollectionCommandWaves.SERIES_ADDED);
    }
}

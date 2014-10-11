package moe.zenbu.app.commands.collection;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.commands.http.FetchCoverCommand;
import moe.zenbu.app.commands.http.HttpCommandWaves;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageWaves;
import moe.zenbu.app.util.ConcurrentUtils;
import moe.zenbu.app.util.JrebirthUtils;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AddSeriesBatchCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(AddSeriesBatchCommand.class);

    private CountDownLatch doneLatch;

    private int total;

    private DoubleProperty progressProperty;
    private StringProperty labelProperty;

    @Override
    protected void initCommand()
    {
        listen(CollectionCommandWaves.SERIES_ADDED);
        listen(HttpCommandWaves.COVER_FETCHED);
    }

    @Override
    protected void perform(final Wave wave)
    {
        progressProperty = wave.getData(JrebirthUtils.DOUBLE_PROPERTY_WAVE_ITEM).getValue();
        labelProperty = wave.getData(JrebirthUtils.STRING_PROPERTY_WAVE_ITEM).getValue();
        List<AnimeUserData> animeList = wave.getData(JrebirthUtils.ANIME_USER_DATA_LIST_WAVE_ITEM).getValue();

        total = animeList.size();
        doneLatch = new CountDownLatch(total);

        labelProperty.set("Adding series");
        progressProperty.set(0.0);

        animeList.forEach(ud -> callCommand(AddSeriesCommand.class, WaveData.build(JrebirthUtils.ANIME_USER_DATA_WAVE_ITEM, ud)));

        try
        {
            doneLatch.await();
        }
        catch(InterruptedException e)
        {
            log.error("Interrupted while waiting to add all series", e);
        }

        Platform.runLater(() -> labelProperty.set("Downloading covers"));
        doneLatch = new CountDownLatch(total);

        animeList.forEach(ud -> callCommand(FetchCoverCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, ud.getAnime())));

        try
        {
            doneLatch.await();
        }
        catch(InterruptedException e)
        {
            log.error("Interrupted while waiting to download covers", e);
        }

        ConcurrentUtils.sleepSilentlyMillis(500);

        sendWave(CollectionCommandWaves.BATCH_ADD_DONE);
        sendWave(CollectionPageWaves.REFRESH);

        unlisten(CollectionCommandWaves.SERIES_ADDED);
        unlisten(HttpCommandWaves.COVER_FETCHED);
    }

    public void doSeriesAdded(final Wave wave)
    {
        Platform.runLater(() -> progressProperty.set((total - (double) doneLatch.getCount()) / (double) total));
        doneLatch.countDown();
    }

    public void doCoverFetched(final Wave wave)
    {
        Platform.runLater(() -> progressProperty.set((total - (double) doneLatch.getCount()) / (double) total));
        doneLatch.countDown();
    }
}

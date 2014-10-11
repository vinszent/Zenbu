package moe.zenbu.app.services.hooks;

import moe.zenbu.app.commands.recognition.LoadTitleListCommand;
import moe.zenbu.app.commands.recognition.RecognitionCommandWaves;
import moe.zenbu.app.enums.Page;
import moe.zenbu.app.commands.db.CreateDatabase;
import moe.zenbu.app.services.rmi.ZenbuRmiWaves;
import moe.zenbu.app.ui.components.pages.PageWaves;

import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;

public class HooksService extends DefaultService
{
    // TODO: Redo
    private Wave currentWave;

    @Override
    protected void initService()
    {
        listen(HooksWaves.STARTUP);
        listen(ZenbuRmiWaves.ANIME_LIST_FETCH_DONE);
        listen(RecognitionCommandWaves.TITLE_LIST_LOADED);

        listen(HooksWaves.PRE_STARTUP);
    }

    public void doPreStartup(final Wave wave)
    {
        callCommand(CreateDatabase.class);
    }

    public void doStartup(final Wave wave)
    {
        currentWave = wave;
        updateProgress(currentWave, 1, 3);
        updateMessage(currentWave, "Updating database");
        sendWave(ZenbuRmiWaves.FETCH_ANIME_LIST);
    }
    
    public void doAnimeListFetchDone(final Wave wave)
    {
        updateProgress(currentWave, 2, 3);
        updateMessage(currentWave, "Loading recognition engine");
        callCommand(LoadTitleListCommand.class);
    }
    
    public void doTitleListLoaded(final Wave wave)
    {
        updateProgress(currentWave, 3, 3);
        updateMessage(currentWave, "Loading collection");
        currentWave = null;
        sendWave(PageWaves.SHOW_PAGE, WaveData.build(PageWaves.PAGE, Page.COLLECTION));
    }
}

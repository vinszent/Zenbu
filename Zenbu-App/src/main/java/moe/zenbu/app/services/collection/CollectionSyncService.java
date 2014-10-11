package moe.zenbu.app.services.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.HistoryItem;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.commands.http.FetchCoverCommand;
import moe.zenbu.app.commands.http.GetHbListCommand;
import moe.zenbu.app.commands.http.GetMalListCommand;
import moe.zenbu.app.commands.http.HttpCommandWaves;
import moe.zenbu.app.commands.http.SendHbListCommand;
import moe.zenbu.app.commands.http.SendMalListCommand;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageWaves;
import moe.zenbu.app.ui.components.popups.embedded.dialogue.choice.DialogueChoicePopupWaves;
import moe.zenbu.app.ui.components.popups.embedded.dialogue.confirmation.DialogueConfirmationPopupWaves;
import moe.zenbu.app.util.ConcurrentUtils;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionSyncService extends DefaultService
{
    private static final Logger log = LoggerFactory.getLogger(CollectionSyncService.class);

    private List<Anime> compiledRemoteList = new ArrayList<>();
    private List<Anime> localList;

    private CountDownLatch coversFetchedLatch;
    private List<Anime> newAnime;
    private List<Anime> oldAnime;

    private Map<Class<? extends DefaultPoolCommand>, List<Anime>> remoteLists;
    private CountDownLatch listFetchedLatch;
    private CountDownLatch listSentLatch;

    private CountDownLatch cloneLatch;
    private String cloneAction;
    private String primaryProvider;

    private Wave currentWave;

    @Override
    protected void initService()
    {
        listen(CollectionServiceWaves.SYNC_LISTS);
        listen(HttpCommandWaves.LIST_FETCHED);
        listen(HttpCommandWaves.LIST_SENT);
    }

    public void doSyncLists(final Wave wave)
    {
        log.info("Syncing lists");

        SqlSession db = DbUtils.getSqlSession();

        currentWave = wave;

        remoteLists = new HashMap<>();

        Setting multiEnabled = db.selectOne("db.mappers.settingmapper.selectSetting", "multi_providers_enabled");

        if(multiEnabled.getValueAsBoolean())
        {
            cloneLatch = new CountDownLatch(1);
            listen(DialogueConfirmationPopupWaves.CONFIRMATION_RECEIVED);
            sendWave(DialogueConfirmationPopupWaves.SHOW_CONFIRMATION, WaveData.build(JrebirthUtils.STRING_WAVE_ITEM, "You have recently enabled an extra sync provider. It is recommended that your lists are identical before multi-syncing, would you like Zenbu to do this for you?"));
            ConcurrentUtils.awaitSilenty(cloneLatch);
            unlisten(DialogueConfirmationPopupWaves.CONFIRMATION_RECEIVED);

            if(cloneAction.equals(DialogueConfirmationPopupWaves.YES))
            {
                log.debug("Ok to clone lists");

                List<String> links = new ArrayList<>();

                if(((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "mal_enabled")).getValueAsBoolean())
                {
                    links.add("MyAnimeList");
                }
                if(((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "hb_enabled")).getValueAsBoolean())
                {
                    links.add("Hummingbird");
                }
                if(((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "al_enabled")).getValueAsBoolean())
                {
                    links.add("AniList");
                }

                cloneLatch = new CountDownLatch(1);
                listen(DialogueChoicePopupWaves.CHOICE_RECEIVED);
                sendWave(DialogueChoicePopupWaves.SHOW_CHOICE, JrebirthUtils.buildWaveData(links));
                ConcurrentUtils.awaitSilenty(cloneLatch);
                unlisten(DialogueChoicePopupWaves.CHOICE_RECEIVED);

                updateMessage(wave, "Fetching remote lists");
                updateProgress(wave, 1, 3);

                getRemoteLists();
    
                List<Anime> primary = null;
                if(primaryProvider.equals("MyAnimeList"))
                {
                    primary = remoteLists.get(SendMalListCommand.class);
                    remoteLists.remove(SendMalListCommand.class);
                }
                else if(primaryProvider.equals("Hummingbird"))
                {
                    primary = remoteLists.get(SendHbListCommand.class);
                    remoteLists.remove(SendHbListCommand.class);
                }
                else if(primaryProvider.equals("AniList"))
                {
                    // TODO: AniList cloning
                }

                updateMessage(wave, "Cloning lists");
                updateProgress(wave, 2, 3);

                doCloneLists(new Object[]{primary, remoteLists}, wave);
                listSentLatch = new CountDownLatch((int) listSentLatch.getCount() - 1);

                ConcurrentUtils.awaitSilenty(listSentLatch);

                multiEnabled.setValue("false");
                db.update("db.mappers.settingmapper.updateSetting", multiEnabled);
            }
            else if(cloneAction.equals(DialogueConfirmationPopupWaves.NO))
            {
                log.debug("No to clone lists");
            }
            else if(cloneAction.equals(DialogueConfirmationPopupWaves.CANCEL))
            {
                log.debug("Cancel to clone lists");

                sendWave(CollectionServiceWaves.SYNC_LISTS_DONE);

                return;
            }
        }

        updateProgress(wave, 1, 4);
        updateMessage(wave, "Fetching remote lists");

        localList = db.selectList("db.mappers.animemapper.selectAnimeWithUserData");

        getRemoteLists();

        updateProgress(wave, 2, 4);
        updateMessage(wave, "Calculating differences");

        for(List<Anime> remoteList : remoteLists.values())
        {
            addToCompiledRemoteList(remoteList);

            log.info("Compiled list size {}", compiledRemoteList.size());
        }

        compareToLocalList();

        updateProgress(wave, 3, 4);
        updateMessage(wave, "Sending changes to sync providers");

        remoteLists.entrySet().forEach(e -> callCommand(e.getKey(), WaveData.build(JrebirthUtils.OBJECTARR_WAVE_ITEM, new Object[]{localList, e.getValue()})));
        ConcurrentUtils.awaitSilenty(listSentLatch);

        updateMessage(wave, "Fetching covers");

        listen(HttpCommandWaves.COVER_FETCHED);

        oldAnime = ListUtils.subtract(db.selectList("db.mappers.animemapper.selectAnimeWithUserData"), localList);
        newAnime = ListUtils.subtract(localList, db.selectList("db.mappers.animemapper.selectAnimeWithUserData"));

        log.debug("Old anime size {} : new anime size {}", oldAnime.size(), newAnime.size());
        
        coversFetchedLatch = new CountDownLatch(newAnime.size());
        
        newAnime.forEach(a ->
        {
            db.insert("db.mappers.animeuserdatamapper.updateAnimeUserData", a.getUserData());
            callCommand(FetchCoverCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, a));
        });

        oldAnime.forEach(a ->
        {
            db.delete("db.mappers.animeuserdatamapper.deleteAnimeUserData", a.getUserData());
            // TODO: Delete cover image
        });

        ConcurrentUtils.awaitSilenty(coversFetchedLatch);
        unlisten(HttpCommandWaves.COVER_FETCHED);

        localList.stream().map(a -> a.getUserData()).forEach(ud -> db.update("db.mappers.animeuserdatamapper.updateAnimeUserData", ud));

        db.close();

        sendWave(CollectionPageWaves.REFRESH);
        sendWave(CollectionServiceWaves.SYNC_LISTS_DONE);
    }

    public void doCloneLists(final Object[] lists, final Wave wave)
    {
        List<Anime> primary = (List<Anime>) lists[0];
        Map<Class<? extends DefaultPoolCommand>, List<Anime>> secondary = (Map<Class<? extends DefaultPoolCommand>, List<Anime>>) lists[1];

        secondary.entrySet().forEach(en -> callCommand(en.getKey(), WaveData.build(JrebirthUtils.OBJECTARR_WAVE_ITEM, new Object[]{primary, en.getValue()})));
    }

    public void doListFetched(final Object[] data, final Wave wave)
    {
        String provider = (String) data[0];
        switch(provider)
        {
            case "mal":
                    remoteLists.put(SendMalListCommand.class, (List<Anime>) data[1]);
                    break;
            case "hb":
                    remoteLists.put(SendHbListCommand.class, (List<Anime>) data[1]);
                    break;
            case "al":
                    // TODO: Send local list to Al
                    break;
        }
        listFetchedLatch.countDown();
    }

    public void doListSent(final Wave wave)
    {
        listSentLatch.countDown();
    }

    public void doCoverFetched(final Wave wave)
    {
        coversFetchedLatch.countDown();
        updateProgress(currentWave, newAnime.size() - coversFetchedLatch.getCount(), newAnime.size());
    }

    public void doConfirmationReceived(final String confirmation, final Wave wave)
    {
        cloneAction = confirmation;
        cloneLatch.countDown();
    }

    public void doChoiceReceived(final String choice, final Wave wave)
    {
        log.debug("Primary provider choice is {}", choice);
        primaryProvider = choice;
        cloneLatch.countDown();
    }

    private void getRemoteLists()
    {
        SqlSession db = DbUtils.getSqlSession();

        int listCounter = 0;
        remoteLists = new HashMap<>();

        if(((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "mal_enabled")).getValueAsBoolean())
        {
            listCounter++;
            callCommand(GetMalListCommand.class);
        }
        if(((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "hb_enabled")).getValueAsBoolean())
        {
            listCounter++;
            callCommand(GetHbListCommand.class);
        }
        if(((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "al_enabled")).getValueAsBoolean())
        {
            listCounter++;
        }
        listFetchedLatch = new CountDownLatch(listCounter);
        listSentLatch = new CountDownLatch(listCounter);

        try
        {
            listFetchedLatch.await();
        }
        catch(InterruptedException e)
        {
            log.error("Sync service interrupted while sleeping", e);
        }

        db.close();
    }

    private void addToCompiledRemoteList(final List<Anime> remoteList)
    {
        SqlSession db = DbUtils.getSqlSession();

        log.debug("Remote list size {}", remoteList.size());
        // Resolve conflicting entries with update time
        remoteList.parallelStream().filter(a -> compiledRemoteList.contains(a)).forEach(a ->
        {
            Anime an = compiledRemoteList.parallelStream().filter(ani -> ani.equals(a)).findFirst().get();
            if(a.getUserData().getLastChanged().after(an.getUserData().getLastChanged()))
            {
                an.setUserData(a.getUserData());
            }
        });
        compiledRemoteList.addAll(remoteList.parallelStream().filter(a -> !compiledRemoteList.contains(a)).collect(Collectors.toList()));

        db.close();
    }

    private void compareToLocalList()
    {
        SqlSession db = DbUtils.getSqlSession();

        log.debug("Comparing compiled remote list to local list");
        // Exist on both local and remote
        compiledRemoteList.parallelStream().filter(a -> localList.contains(a)).forEach(a ->
        {
            Anime an = localList.parallelStream().filter(ani -> ani.equals(a)).findFirst().get();
            if(a.getUserData().getLastChanged().after(an.getUserData().getLastChanged()))
            {
                an.setUserData(a.getUserData());
            }
        });
        // Exist only on remote
        compiledRemoteList.stream().filter(a -> !localList.contains(a)).forEach(a ->
        {
            Map params = new HashMap();
            params.put("animeId", a.getId());
            params.put("action", HistoryItem.ACTION_DELETE);
            if(((int) db.selectOne("db.mappers.historyitemmapper.countHistoryItemByAction", params)) < 1)
            {
                localList.add(a);
            }
            else
            {
                //TODO: Delete from database
                localList.remove(a);
            }
        });
        // Exists only on local
        localList.parallelStream().filter(a -> !compiledRemoteList.contains(a)).forEach(a ->
        {
            Map params = new HashMap();
            params.put("animeId", a.getId());
            params.put("action", HistoryItem.ACTION_ADD);
            if(((int) db.selectOne("db.mappers.historyitemmapper.countHistoryItemByAction", params)) < 1)
            {
                //TODO: Delete from database
                localList.remove(a);
            }
            else
            {
                localList.add(a);
            }
        });

        db.close();
    }
}

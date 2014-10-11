package moe.zenbu.app.services.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.List;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.concurrent.RunInto;
import org.jrebirth.af.core.concurrent.RunType;
import org.jrebirth.af.core.concurrent.RunnablePriority;
import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZenbuRmiService extends DefaultService
{
    private static final Logger log = LoggerFactory.getLogger(ZenbuRmiService.class);

    private final int RMI_PORT = 19362;
    private final String APP_RMI = "application";

    private Registry registry;
    private ZenbuServerInterface zenbuServerInterface;

    @Override
    protected void initService()
    {
        listen(ZenbuRmiWaves.FETCH_ANIME_LIST);

        try
        {
            registry = LocateRegistry.getRegistry("localhost", RMI_PORT);
//            registry = LocateRegistry.getRegistry("188.226.206.236", RMI_PORT);
            zenbuServerInterface = (ZenbuServerInterface) registry.lookup(APP_RMI);
        }
        catch(Exception e)
        {
            log.error("Could not start Zenbu RMI service", e);
        }

        log.info("Zenbu RMI service started");
    }

    @RunInto(value = RunType.JTP, priority = RunnablePriority.High)
    public void doFetchAnimeList(final Wave wave)
    {
        log.info("Fetching anime list");

        SqlSession db = DbUtils.getSqlSession();

        List<Anime> animeList = Collections.emptyList();

        Setting lastUpdated = db.selectOne("db.mappers.settingmapper.selectSetting", "anime_metadata_last_updated");

        try
        {
            animeList = zenbuServerInterface.getAnimeList(db.selectOne("db.mappers.settingmapper.selectSetting", "zenbu_version"), lastUpdated);

            log.debug("Animelist size is {}", animeList.size());
        }
        catch(Exception e)
        {
            log.error("Could not fetch anime list", e);
        }

        if(animeList == null)
        {
            log.warn("Client is out of date or server error, not updating anime list");

            sendWave(ZenbuRmiWaves.ANIME_LIST_FETCH_DONE);

            return;
        }
        else if(animeList.isEmpty())
        {
            log.info("Anime list is already up-to-date");

            sendWave(ZenbuRmiWaves.ANIME_LIST_FETCH_DONE);

            return;
        }
        else
        {
            log.info("Updating anime list");

            animeList.stream().forEach(a ->
            {
                db.insert("db.mappers.animemapper.mergeAnime", a);
                db.commit();
                // TODO: Add merge methods
                a.getTitles().forEach(t -> db.insert("db.mappers.titlemapper.insertTitle", t));
                a.getGenres().forEach(g -> db.insert("db.mappers.genremapper.insertGenre", g));
                db.commit();
            });

            log.info("Updated anime list");

            lastUpdated.setValue(String.valueOf(System.currentTimeMillis() / 1000L));
            db.update("db.mappers.settingmapper.updateSetting", lastUpdated);
            db.commit();

            log.info("Anime list updated");
        }

        db.close();

        sendWave(ZenbuRmiWaves.ANIME_LIST_FETCH_DONE);
    }
}

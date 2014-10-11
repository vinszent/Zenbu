package moe.zenbu.app.services.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.server.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZenbuServerImpl extends UnicastRemoteObject implements ZenbuServerInterface
{
    private static final Logger log = LoggerFactory.getLogger(ZenbuServerImpl.class);

    private static final long serialVersionUID = 1L;

    private static final SqlSession db = DbUtils.getSqlSession();

    public ZenbuServerImpl() throws RemoteException
    {
        super();
    }

    @Override
    public List<Anime> getAnimeList(Setting zenbuVersion, Setting lastUpdated)
    {
        log.info("Executed getAnimeList");

        if(db.selectOne("db.mappers.settingmapper.selectSetting", "zenbu_version").equals(zenbuVersion))
        {
            Date updated = new Date(Long.parseLong(lastUpdated.getValue()) * 1000L);

            log.debug("Last updated data is {}", updated.toString());

            List<Anime> list = db.selectList("db.mappers.animemapper.selectAllAnimeUpdatedAfter", updated);

            return list;
        }
        else
        {
            return Collections.emptyList();
        }
    }
}

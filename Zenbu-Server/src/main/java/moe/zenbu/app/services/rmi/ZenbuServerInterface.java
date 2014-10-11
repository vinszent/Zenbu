package moe.zenbu.app.services.rmi;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.Setting;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ZenbuServerInterface extends Remote
{
    public List<Anime> getAnimeList(Setting zenbuVersion, Setting lastUpdated) throws RemoteException;
}


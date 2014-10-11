package moe.zenbu.app.services.rmi;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface ZenbuRmiWaves
{
    WaveType FETCH_ANIME_LIST = WaveTypeBase.build("FETCH_ANIME_LIST");
    
    WaveType ANIME_LIST_FETCH_DONE = WaveTypeBase.build("ANIME_LIST_FETCH_DONE");
}

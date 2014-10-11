package moe.zenbu.app.commands.collection;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface CollectionCommandWaves
{
    WaveType SERIES_ADDED = WaveTypeBase.build("SERIES_ADDED");

    WaveType BATCH_ADD_DONE = WaveTypeBase.build("BATCH_ADD_DONE");
}

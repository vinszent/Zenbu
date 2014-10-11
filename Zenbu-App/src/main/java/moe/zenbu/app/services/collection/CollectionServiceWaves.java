package moe.zenbu.app.services.collection;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface CollectionServiceWaves
{
    WaveType SYNC_LISTS = WaveTypeBase.build("SYNC_LISTS");

    WaveType SYNC_LISTS_DONE = WaveTypeBase.build("SYNC_LISTS_DONE");

    WaveType ADD_SERIES_BATCH = WaveTypeBase.build("ADD_SERIES_BATCH", JrebirthUtils.ANIME_USER_DATA_LIST_WAVE_ITEM);

    WaveType ADD_SERIES_BATCH_DONE = WaveTypeBase.build("ADD_SERIES_BATCH_DONE");
}

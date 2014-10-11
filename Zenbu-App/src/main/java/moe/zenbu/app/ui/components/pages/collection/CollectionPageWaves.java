package moe.zenbu.app.ui.components.pages.collection;

import moe.zenbu.app.enums.CollectionPage;
import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface CollectionPageWaves
{
    WaveItem<CollectionPage> COLLECTION_PAGE = new WaveItem<CollectionPage>(){};

    WaveType SWITCH_COLLECTION_PAGE = WaveTypeBase.build("SWITCH_COLLECTION_PAGE", COLLECTION_PAGE);

    WaveType FILTER = WaveTypeBase.build("FILTER", JrebirthUtils.STRING_WAVE_ITEM);

    WaveType SORT = WaveTypeBase.build("SORT", JrebirthUtils.STRING_WAVE_ITEM);

    WaveType SEARCH = WaveTypeBase.build("SEARCH", JrebirthUtils.STRING_WAVE_ITEM);

    WaveType REFRESH = WaveTypeBase.build("REFRESH");
    
    WaveType REFRESH_DONE = WaveTypeBase.build("REFRESH_DONE");
}

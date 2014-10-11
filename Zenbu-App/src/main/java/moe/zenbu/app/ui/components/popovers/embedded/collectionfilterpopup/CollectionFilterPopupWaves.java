package moe.zenbu.app.ui.components.popovers.embedded.collectionfilterpopup;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface CollectionFilterPopupWaves
{
    WaveType SHOW_COLLECTION_FILTER_POPUP = WaveTypeBase.build("SHOW_COLLECTION_FILTER_POPUP");

    WaveType HIDE_COLLECTION_FILTER_POPUP = WaveTypeBase.build("HIDE_COLLECTION_FILTER_POPUP");
}

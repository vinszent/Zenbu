package moe.zenbu.app.ui.components.functionbar.collection;

import moe.zenbu.app.enums.CollectionFunctionBar;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface CollectionFunctionBarWaves
{
    WaveItem<CollectionFunctionBar> COLLECTION_FUNCTION_BAR = new WaveItem<CollectionFunctionBar>(){};

    WaveType SHOW_COLLECTION_FUNCTION_BAR = WaveTypeBase.build("SHOW_COLLECTION_FUNCTION_BAR", COLLECTION_FUNCTION_BAR);
}

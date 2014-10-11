package moe.zenbu.app.ui.components.functionbar.collection.standard;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface StandardCollectionFunctionBarWaves
{
    WaveType SHOW_FILTER_BOX = WaveTypeBase.build("SHOW_FILTER_BOX");

    WaveType HIDE_FILTER_BOX = WaveTypeBase.build("HIDE_FILTER_BOX");
}

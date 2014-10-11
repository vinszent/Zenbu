package moe.zenbu.app.ui.components.switcher;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface SwitcherWaves
{
    WaveItem<Boolean> SWITCHER_COMPACT = new WaveItem<Boolean>(){};
    WaveItem<Boolean> SWITCHER_COMPACT1 = new WaveItem<Boolean>(){};

    WaveType SET_COMPACT = WaveTypeBase.build("SET_COMPACT");
}

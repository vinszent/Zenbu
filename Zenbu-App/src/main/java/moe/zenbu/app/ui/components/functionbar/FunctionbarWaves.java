package moe.zenbu.app.ui.components.functionbar;

import moe.zenbu.app.enums.FunctionBar;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface FunctionbarWaves
{
    WaveItem<FunctionBar> FUNCTIONBAR = new WaveItem<FunctionBar>(){};

    WaveType SHOW_FUNCTIONBAR = WaveTypeBase.build("SHOW_FUNCTIONBAR", FUNCTIONBAR);
}

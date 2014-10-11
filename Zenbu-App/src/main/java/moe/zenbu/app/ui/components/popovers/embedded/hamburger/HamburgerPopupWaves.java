package moe.zenbu.app.ui.components.popovers.embedded.hamburger;

import moe.zenbu.app.enums.HamburgerContent;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface HamburgerPopupWaves
{
    WaveItem<HamburgerContent> HAMBURGER_CONTENT = new WaveItem<HamburgerContent>(){};

    WaveType SHOW = WaveTypeBase.build("SHOW");

    WaveType SWITCH_CONTENT = WaveTypeBase.build("SWITCH_CONTENT", HAMBURGER_CONTENT);
}

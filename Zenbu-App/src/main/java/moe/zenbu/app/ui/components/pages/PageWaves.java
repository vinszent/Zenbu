package moe.zenbu.app.ui.components.pages;

import moe.zenbu.app.enums.Page;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface PageWaves
{
    WaveItem<Page> PAGE = new WaveItem<Page>(){};

    WaveType SHOW_PAGE = WaveTypeBase.build("SHOW_PAGE", PAGE);
}

package moe.zenbu.app.ui.components.popups.embedded.seriesoptions;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface SeriesOptionsPopupWaves
{
    WaveType SHOW = WaveTypeBase.build("SHOW", JrebirthUtils.ANIME_LIST_WAVE_ITEM);

    WaveType HIDE_SERIES_OPTIONS = WaveTypeBase.build("HIDE_SERIES_OPTIONS");
}

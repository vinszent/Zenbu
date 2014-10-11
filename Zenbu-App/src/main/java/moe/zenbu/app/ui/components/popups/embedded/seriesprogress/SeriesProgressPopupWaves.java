package moe.zenbu.app.ui.components.popups.embedded.seriesprogress;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface SeriesProgressPopupWaves
{
    WaveType SHOW_PROGRESS_POPUP = WaveTypeBase.build("SHOW_PROGRESS_POPUP", JrebirthUtils.ANIME_WAVE_ITEM, JrebirthUtils.NODE_WAVE_ITEM);

    WaveType HIDE_PROGRESS_POPUP = WaveTypeBase.build("HIDE_PROGRESS_POPUP");

    WaveType PROGRESS_UPDATED = WaveTypeBase.build("PROGRESS_UPDATED", JrebirthUtils.ANIME_WAVE_ITEM);
}

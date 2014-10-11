package moe.zenbu.app.ui.components.popups.embedded.seriesscore;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface SeriesScoreFloatingWaves
{
    WaveType SHOW_SCORE_POPUP = WaveTypeBase.build("SHOW_SCORE_POPUP", JrebirthUtils.ANIME_WAVE_ITEM, JrebirthUtils.NODE_WAVE_ITEM);

    WaveType HIDE_SCORE_POPUP = WaveTypeBase.build("HIDE_SCORE_POPUP");

    WaveType SCORE_UPDATED = WaveTypeBase.build("SCORE_UPDATED", JrebirthUtils.ANIME_WAVE_ITEM);
}

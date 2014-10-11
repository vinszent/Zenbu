package moe.zenbu.app.ui.components.popups.embedded.episodeinfo;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface EpisodeInfoPopupWaves
{
    WaveType SHOW = WaveTypeBase.build("SHOW", JrebirthUtils.NODE_WAVE_ITEM, JrebirthUtils.EPISODE_WAVE_ITEM);

    WaveType HIDE = WaveTypeBase.build("HIDE");
}

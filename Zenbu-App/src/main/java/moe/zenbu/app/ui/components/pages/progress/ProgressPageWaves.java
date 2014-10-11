package moe.zenbu.app.ui.components.pages.progress;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface ProgressPageWaves
{
    WaveType SET_TOTAL = WaveTypeBase.build("SET_TOTAL", JrebirthUtils.INTEGER_WAVE_ITEM);
    
    WaveType INCREMENT = WaveTypeBase.build("INCREMENT");
    
    WaveType SET_TEXT = WaveTypeBase.build("SET_TEXT", JrebirthUtils.STRING_WAVE_ITEM);
    
    WaveType SET_INDEFINITE = WaveTypeBase.build("SET_INDEFINITE");
}

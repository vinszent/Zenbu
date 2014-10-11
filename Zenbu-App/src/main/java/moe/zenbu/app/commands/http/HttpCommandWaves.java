package moe.zenbu.app.commands.http;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface HttpCommandWaves
{
    WaveType COVER_FETCHED = WaveTypeBase.build("COVER_FETCHED");

    WaveType LIST_FETCHED = WaveTypeBase.build("LIST_FETCHED", JrebirthUtils.OBJECTARR_WAVE_ITEM);

    WaveType LIST_SENT = WaveTypeBase.build("LIST_SENT");
}

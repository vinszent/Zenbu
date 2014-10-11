package moe.zenbu.app.services.hooks;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface HooksWaves
{
    WaveType STARTUP = WaveTypeBase.build("STARTUP");

    WaveType PRE_STARTUP = WaveTypeBase.build("PRE_STARTUP");
}

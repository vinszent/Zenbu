package moe.zenbu.app.ui;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface Waves
{
    WaveItem<Double[]> COORDS = new WaveItem<Double[]>(){};

    WaveType SET_COORDS = WaveTypeBase.build("SET_COORDS", COORDS);

    WaveType MOVE_COORDS = WaveTypeBase.build("MOVE_COORDS", COORDS);
}

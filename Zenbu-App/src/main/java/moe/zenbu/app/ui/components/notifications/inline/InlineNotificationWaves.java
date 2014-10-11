package moe.zenbu.app.ui.components.notifications.inline;

import moe.zenbu.app.util.JrebirthUtils;

import org.controlsfx.control.action.Action;
import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface InlineNotificationWaves
{
    WaveItem<Action> RESPONSE = new WaveItem<Action>(){};

    WaveType SHOW = WaveTypeBase.build("SHOW", JrebirthUtils.STRING_WAVE_ITEM);

    WaveType SHOW_CONFIRMATION = WaveTypeBase.build("SHOW_CONFIRMATION", JrebirthUtils.STRING_WAVE_ITEM);

    WaveType RESPONSE_RECEIVED = WaveTypeBase.build("RESPONSE_RECEIVED", RESPONSE);

    WaveType COMMAND_LINK_RECEIVED = WaveTypeBase.build("COMMAND_LINK_RECEIVED", RESPONSE);
}

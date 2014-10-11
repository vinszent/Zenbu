package moe.zenbu.app.ui.components.popups.embedded.dialogue.confirmation;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface DialogueConfirmationPopupWaves
{
    String YES = "yes";
    String NO = "no";
    String CANCEL = "cancel";

    WaveType CONFIRMATION_RECEIVED = WaveTypeBase.build("CONFIRMATION_RECEIVED", JrebirthUtils.STRING_WAVE_ITEM);

    WaveType SHOW_CONFIRMATION = WaveTypeBase.build("SHOW_CONFIRMATION");
}

package moe.zenbu.app.ui.components.popups.embedded.dialogue.choice;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface DialogueChoicePopupWaves
{
    WaveType SHOW_CHOICE = WaveTypeBase.build("SHOW_CHOICE", JrebirthUtils.STRING_LIST_WAVE_ITEM);

    WaveType CHOICE_RECEIVED = WaveTypeBase.build("CHOICE_RECEIVED", JrebirthUtils.STRING_WAVE_ITEM);
}

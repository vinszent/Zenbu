package moe.zenbu.app.ui.components.popups.embedded.dialogue.confirmation;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;

public class DialogueConfirmationPopupModel extends DefaultModel<DialogueConfirmationPopupModel, DialogueConfirmationPopupView>
{
    @Override
    public void initModel()
    {
        listen(DialogueConfirmationPopupWaves.SHOW_CONFIRMATION);
    }

    @Override
    public void bind()
    {
    }

    public void doShowConfirmation(final String message, final Wave wave)
    {
        getView().getMessageLabel().setText(message);

        getView().getRootNode().show();
    }
}

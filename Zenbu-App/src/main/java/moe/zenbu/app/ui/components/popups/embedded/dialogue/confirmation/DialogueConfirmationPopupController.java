package moe.zenbu.app.ui.components.popups.embedded.dialogue.confirmation;

import javafx.event.ActionEvent;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.exception.CoreException;

public class DialogueConfirmationPopupController extends DefaultController<DialogueConfirmationPopupModel, DialogueConfirmationPopupView>
{
    public DialogueConfirmationPopupController(final DialogueConfirmationPopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    public void initEventAdapters()
    {
        linkWave(getView().getYesButton(), ActionEvent.ACTION, DialogueConfirmationPopupWaves.CONFIRMATION_RECEIVED, JrebirthUtils.buildWaveData(DialogueConfirmationPopupWaves.YES));
        linkWave(getView().getNoButton(), ActionEvent.ACTION, DialogueConfirmationPopupWaves.CONFIRMATION_RECEIVED, JrebirthUtils.buildWaveData(DialogueConfirmationPopupWaves.NO));
        linkWave(getView().getCancelButton(), ActionEvent.ACTION, DialogueConfirmationPopupWaves.CONFIRMATION_RECEIVED, JrebirthUtils.buildWaveData(DialogueConfirmationPopupWaves.CANCEL));
    }

    @Override
    public void initEventHandlers()
    {
    }

    public void onActionHide(final ActionEvent evt)
    {
        getView().getRootNode().hide();
    }
}

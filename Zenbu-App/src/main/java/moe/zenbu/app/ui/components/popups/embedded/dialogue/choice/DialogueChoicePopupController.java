package moe.zenbu.app.ui.components.popups.embedded.dialogue.choice;

import javafx.event.ActionEvent;

import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class DialogueChoicePopupController extends DefaultController<DialogueChoicePopupModel, DialogueChoicePopupView>
{
    public DialogueChoicePopupController(final DialogueChoicePopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    public void initEventAdapters()
    {
        //linkWave(getView().getDoneButton(), ActionEvent.ACTION, InlineDialogueChoicePopupWaves.CHOICE_RECEIVED, WaveData.build(JrebirthUtils.STRING_WAVE_ITEM, getView().getChoiceBox().getValue()));
    }

    @Override
    public void initEventHandlers()
    {
        getView().getDoneButton().setOnAction(evt ->
        {
        });
    }

    public void onActionDone(final ActionEvent evt)
    {
        String choice = getView().getChoiceBox().getValue();
        getModel().sendWave(DialogueChoicePopupWaves.CHOICE_RECEIVED, JrebirthUtils.buildWaveData(choice));
        getView().getRootNode().hide();
    }
}

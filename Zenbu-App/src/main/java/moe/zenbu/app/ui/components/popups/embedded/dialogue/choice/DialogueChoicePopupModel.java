package moe.zenbu.app.ui.components.popups.embedded.dialogue.choice;

import java.util.List;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;

public class DialogueChoicePopupModel extends DefaultModel<DialogueChoicePopupModel, DialogueChoicePopupView>
{
    @Override
    public void initModel()
    {
        listen(DialogueChoicePopupWaves.SHOW_CHOICE);
    }

    @Override
    public void bind()
    {
    }

    public void doShowChoice(final List<String> choices, final Wave wave)
    {
        getView().getChoiceBox().getItems().setAll(choices);

        getView().getRootNode().show();
    }
}


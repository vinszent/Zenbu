package moe.zenbu.app.ui.components.popups.embedded.dialogue.choice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.EmbeddedPopup;
import moe.zenbu.app.ui.workbench.WorkbenchModel;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

public class DialogueChoicePopupView extends DefaultView<DialogueChoicePopupModel, EmbeddedPopup, DialogueChoicePopupController>
{
    private FXMLLoader fxmlLoader;

    @FXML
    private VBox choicePopupBox;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    @OnAction(name = "Done")
    private Button doneButton;

    public DialogueChoicePopupView(final DialogueChoicePopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popups/ChoicePopup.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        getRootNode().setContent(choicePopupBox);
        getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane());
    }

    public VBox getChoicePopupBox()
    {
        return choicePopupBox;
    }

    public ChoiceBox<String> getChoiceBox()
    {
        return choiceBox;
    }

    public Button getDoneButton()
    {
        return doneButton;
    }
}

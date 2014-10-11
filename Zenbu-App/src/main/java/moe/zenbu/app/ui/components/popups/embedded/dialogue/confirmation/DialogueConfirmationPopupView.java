package moe.zenbu.app.ui.components.popups.embedded.dialogue.confirmation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.EmbeddedPopup;
import moe.zenbu.app.ui.workbench.WorkbenchModel;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

public class DialogueConfirmationPopupView extends DefaultView<DialogueConfirmationPopupModel, EmbeddedPopup, DialogueConfirmationPopupController>
{
    private FXMLLoader fxmlLoader;

    @FXML
    private Label messageLabel;

    @FXML
    @OnAction(name = "Hide")
    private Button yesButton;

    @FXML
    @OnAction(name = "Hide")
    private Button noButton;

    @FXML
    @OnAction(name = "Hide")
    private Button cancelButton;

    @FXML
    private VBox confirmationBox;

    public DialogueConfirmationPopupView(final DialogueConfirmationPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popups/ConfirmationPopup.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        messageLabel.maxWidthProperty().bind(confirmationBox.maxWidthProperty());

        getRootNode().setContent(confirmationBox);
        getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane());
    }

    public Label getMessageLabel()
    {
        return messageLabel;
    }

    public Button getYesButton()
    {
        return yesButton;
    }

    public Button getNoButton()
    {
        return noButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public VBox getConfirmationBox()
    {
        return confirmationBox;
    }
}

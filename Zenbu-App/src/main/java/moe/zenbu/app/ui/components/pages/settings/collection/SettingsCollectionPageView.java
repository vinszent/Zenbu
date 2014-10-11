package moe.zenbu.app.ui.components.pages.settings.collection;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.controlsfx.control.textfield.TextFields;
import org.jrebirth.af.core.concurrent.AbstractJrbRunnable;
import org.jrebirth.af.core.concurrent.JRebirth;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

public class SettingsCollectionPageView extends DefaultView<SettingsCollectionPageModel, GridPane, SettingsCollectionPageController>
{
    // Sync provider items
    private Node syncProviderBorder;
    private VBox syncProviderBox;
    private HBox enableSyncBox;
    @OnAction(name = "Provider")
    private ComboBox syncBox;
    private RadioButton enableSyncButton;
    private TextField usernameField;
    private PasswordField passwordField;
    @OnAction(name = "SaveCredentials")
    private Button saveButton;

    public SettingsCollectionPageView(final SettingsCollectionPageModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        // Setup sync provider items
        syncBox = new ComboBox();
        syncBox.getItems().addAll("MyAnimeList", "Hummingbird", "AniList");

        enableSyncButton = new RadioButton();

        enableSyncBox = new HBox();
        enableSyncBox.getChildren().addAll(syncBox, enableSyncButton);
        enableSyncBox.getStyleClass().add("sync-enable-box");

        usernameField = TextFields.createClearableTextField();
        //usernameField = new TextField();
        usernameField.setPromptText("Username");
        JRebirth.runIntoJAT(new AbstractJrbRunnable("tooltip")
        {
            @Override
            public void runInto()
            {
                usernameField.setTooltip(new Tooltip("Username"));
            }
        });
        usernameField.disableProperty().bind(enableSyncButton.selectedProperty().not());

        passwordField = TextFields.createClearablePasswordField();
        //passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        JRebirth.runIntoJAT(new AbstractJrbRunnable("tooltip")
        {
            @Override
            public void runInto()
            {
                passwordField.setTooltip(new Tooltip("Password"));
            }
        });
        passwordField.disableProperty().bind(enableSyncButton.selectedProperty().not());

        saveButton = new Button("Save");
        saveButton.disableProperty().bind(enableSyncButton.selectedProperty().not());

        syncProviderBox = new VBox();
        syncProviderBox.getChildren().addAll(enableSyncBox, usernameField, passwordField, saveButton);
        syncProviderBox.getStyleClass().add("sync-provider-box");

        // Setup root
        getRootNode().addColumn(0, syncProviderBox);
        getRootNode().getStyleClass().add("settings-collection-page");
    }

    public ComboBox getSyncBox()
    {
        return syncBox;
    }

    public TextField getUsernameField()
    {
        return usernameField;
    }

    public PasswordField getPasswordField()
    {
        return passwordField;
    }

    public Button getSaveButton()
    {
        return saveButton;
    }

    public RadioButton getEnableSyncButton()
    {
        return enableSyncButton;
    }
}

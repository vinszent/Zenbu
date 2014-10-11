package moe.zenbu.app.ui.components.popovers.embedded.hamburger.sync;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import moe.zenbu.app.resources.I18n;

import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

public class SyncPopupView extends DefaultView<SyncPopupModel, StackPane, SyncPopupController>
{
    @OnAction(name = "Sync")
    private Button syncButton;

    public SyncPopupView(final SyncPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        syncButton = new Button();
        syncButton.setText(I18n.getLocalisedString("popup.sync.sync"));
        syncButton.getStyleClass().add("sync-button");

        super.getRootNode().getChildren().add(syncButton);
        super.getRootNode().getStyleClass().add("sync-box");
    }

    public Button getSyncButton()
    {
        return syncButton;
    }
}

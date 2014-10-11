package moe.zenbu.app.ui.components.popovers.embedded.hamburger.history;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.jrebirth.af.core.ui.DefaultView;

public class HistoryPopupView extends DefaultView<HistoryPopupModel, StackPane, HistoryPopupController>
{
    public HistoryPopupView(final HistoryPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        getRootNode().getChildren().add(new Label("History"));
    }
}

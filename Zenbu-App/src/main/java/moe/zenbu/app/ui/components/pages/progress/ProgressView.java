package moe.zenbu.app.ui.components.pages.progress;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import org.jrebirth.af.core.ui.DefaultView;

public class ProgressView extends DefaultView<ProgressModel, VBox, ProgressController>
{
    private ProgressBar progressBar;
    private Label statusLabel;

    public ProgressView(final ProgressModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        progressBar = new ProgressBar();
        statusLabel = new Label();
        
        getRootNode().getChildren().addAll(progressBar, statusLabel);
        getRootNode().getStyleClass().add("progress-page");
    }

    public ProgressBar getProgressBar()
    {
        return progressBar;
    }

    public Label getStatusLabel()
    {
        return statusLabel;
    }
}

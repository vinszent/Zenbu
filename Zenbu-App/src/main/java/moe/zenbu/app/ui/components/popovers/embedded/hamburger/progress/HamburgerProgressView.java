package moe.zenbu.app.ui.components.popovers.embedded.hamburger.progress;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import org.jrebirth.af.core.ui.DefaultView;

public class HamburgerProgressView extends DefaultView<HamburgerProgressModel, VBox, HamburgerProgressController>
{
    private ProgressBar progressBar;
    private Label progressLabel;

    public HamburgerProgressView(final HamburgerProgressModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        progressBar = new ProgressBar();
        progressLabel = new Label();

        getRootNode().getChildren().addAll(progressBar, progressLabel);
        getRootNode().getStyleClass().add("progress-box");
    }

    public ProgressBar getProgressBar()
    {
        return progressBar;
    }

    public Label getProgressLabel()
    {
        return progressLabel;
    }
}

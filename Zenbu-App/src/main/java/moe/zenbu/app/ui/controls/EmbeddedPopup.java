package moe.zenbu.app.ui.controls;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import moe.zenbu.app.util.FXUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedPopup extends StackPane
{
    private static final Logger log = LoggerFactory.getLogger(EmbeddedPopup.class);

    private static final double DURATION_MILLIS = 300;

    private Pane pane;

    private Region blockingRegion;

    private FadeTransition showFadeTransition;
    private Timeline showZoomBounceScaleTransition;
    private ParallelTransition showZoomBounceTransition;

    private FadeTransition hideFadeTransition;
    private Timeline hideScaleTransition;
    private ParallelTransition hideZoomBounceTransition;

    private ScaleTransition showZoomInScaleTransition;
    private ParallelTransition showZoomInTransition;

    private ScaleTransition hideZoomInScaleTransition;
    private ParallelTransition hideZoomInTransition;

    private FadeTransition showRegionTransition;
    private FadeTransition hideRegionTransition;

    private Animation showAnimation;
    private Animation hideAnimation;

    private EventHandler<MouseEvent> exitHandler;

    public EmbeddedPopup()
    {
        blockingRegion = new Region();

        // Animations
        // Fade region
        showRegionTransition = new FadeTransition();
        showRegionTransition.setNode(blockingRegion);
        showRegionTransition.setFromValue(0.0);
        showRegionTransition.setToValue(0.8);
        showRegionTransition.setDuration(Duration.millis(DURATION_MILLIS));


        hideRegionTransition = new FadeTransition();
        hideRegionTransition.setNode(blockingRegion);
        hideRegionTransition.setFromValue(0.8);
        hideRegionTransition.setToValue(0.0);
        hideRegionTransition.setDuration(Duration.millis(DURATION_MILLIS));

        // Fade
        showFadeTransition = new FadeTransition();
        showFadeTransition.setNode(this);
        showFadeTransition.setFromValue(0.0);
        showFadeTransition.setToValue(1.0);
        showFadeTransition.setDuration(Duration.millis(DURATION_MILLIS));
        showFadeTransition.setInterpolator(FXUtils.WEB_EASE);

        hideFadeTransition = new FadeTransition();
        hideFadeTransition.setNode(this);
        hideFadeTransition.setFromValue(1.0);
        hideFadeTransition.setToValue(0.0);
        hideFadeTransition.setDuration(Duration.millis(DURATION_MILLIS));
        hideFadeTransition.setInterpolator(FXUtils.WEB_EASE);

        // Zoom-bounce
        showZoomBounceScaleTransition = new Timeline();
        showZoomBounceScaleTransition.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(0), new KeyValue(this.scaleXProperty(), 0.8, FXUtils.WEB_EASE), new KeyValue(this.scaleYProperty(), 0.8, FXUtils.WEB_EASE)),
            new KeyFrame(Duration.millis(0.9 * DURATION_MILLIS), new KeyValue(this.scaleXProperty(), 1.03, FXUtils.WEB_EASE), new KeyValue(this.scaleYProperty(), 1.03, FXUtils.WEB_EASE)),
            new KeyFrame(Duration.millis(DURATION_MILLIS), new KeyValue(this.scaleXProperty(), 1.0, FXUtils.WEB_EASE), new KeyValue(this.scaleYProperty(), 1.0, FXUtils.WEB_EASE)));

        showZoomBounceTransition = new ParallelTransition();
        showZoomBounceTransition.getChildren().addAll(showFadeTransition, showZoomBounceScaleTransition);

        showAnimation = showZoomBounceTransition;

        hideScaleTransition = new Timeline();
        hideScaleTransition.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(0), new KeyValue(this.scaleXProperty(), 1.0, FXUtils.WEB_EASE), new KeyValue(this.scaleYProperty(), 1.0, FXUtils.WEB_EASE)),
            new KeyFrame(Duration.millis(DURATION_MILLIS), new KeyValue(this.scaleXProperty(), 0.8, FXUtils.WEB_EASE), new KeyValue(this.scaleYProperty(), 0.8, FXUtils.WEB_EASE)));

        hideZoomBounceTransition = new ParallelTransition();
        hideZoomBounceTransition.getChildren().addAll(hideFadeTransition, hideScaleTransition);

        hideAnimation = hideZoomBounceTransition;

        // Zoom in
        showZoomInScaleTransition = new ScaleTransition(Duration.millis(DURATION_MILLIS), this);
        showZoomInScaleTransition.setToX(1);
        showZoomInScaleTransition.setFromX(0.8);
        showZoomInScaleTransition.setToY(1);
        showZoomInScaleTransition.setFromY(0.8);
        showZoomInScaleTransition.setInterpolator(Interpolator.EASE_OUT);

        showZoomInTransition = new ParallelTransition();
        showZoomInTransition.getChildren().addAll(showFadeTransition, showZoomInScaleTransition, showRegionTransition);
        showZoomInTransition.setInterpolator(Interpolator.EASE_OUT);

        hideZoomInScaleTransition = new ScaleTransition(Duration.millis(DURATION_MILLIS), this);
        hideZoomInScaleTransition.setToX(0.8);
        hideZoomInScaleTransition.setFromX(1);
        hideZoomInScaleTransition.setToY(0.8);
        hideZoomInScaleTransition.setFromY(1);
        hideZoomInScaleTransition.setInterpolator(Interpolator.EASE_IN);

        hideZoomInTransition = new ParallelTransition();
        hideZoomInTransition.getChildren().addAll(hideFadeTransition, hideZoomInScaleTransition, hideRegionTransition);
        hideZoomInTransition.setInterpolator(Interpolator.EASE_IN);

        hideAnimation.setOnFinished(evt -> this.setVisible(false));

        // Mouse exit handler
        exitHandler = (evt) -> hideAnimation.play();

        super.getStyleClass().add("embedded-popup");
        super.setVisible(false);
    }

    public void setPane(final Pane pane)
    {
        this.pane = pane;

        pane.getChildren().add(this);

        blockingRegion.prefHeightProperty().bind(pane.heightProperty());
        blockingRegion.prefWidthProperty().bind(pane.widthProperty());
    }

    public void setCloseOnMouseExit(final boolean arg)
    {
        if(arg)
        {
            showAnimation.setOnFinished(evt -> super.setOnMouseExited(exitHandler));
        }
        else
        {
            showAnimation.setOnFinished(null);
        }
    }


    public void setContent(final Node content)
    {
        this.getChildren().setAll(content);
    }

    public void show(final Region origin)
    {
        double x = FXUtils.getSceneX(origin) + origin.getWidth() / 2.0 - this.getWidth() / 2.0;
        double y = FXUtils.getSceneY(origin) + origin.getHeight() / 2.0 - this.getHeight() / 2.0;

        log.debug("Showing embedded popup at ({}, {})", x, y);

        super.setVisible(true);
        super.setLayoutX(x);
        super.setLayoutY(y);

        showAnimation.play();
    }

    public void show()
    {
        double x = pane.getWidth() / 2.0 - this.getWidth() / 2.0;
        double y = pane.getHeight() / 2.0 - this.getHeight() / 2.0;

        log.debug("Showing embedded popup at ({}, {})", x, y);

        super.setVisible(true);
        super.setLayoutX(x);
        super.setLayoutY(y);

        showAnimation.play();
    }

    public void hide()
    {
        hideAnimation.play();
    }
}

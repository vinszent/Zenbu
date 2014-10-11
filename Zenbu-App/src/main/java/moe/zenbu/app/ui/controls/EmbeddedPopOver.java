package moe.zenbu.app.ui.controls;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;

import moe.zenbu.app.util.FXUtils;

public class EmbeddedPopOver extends StackPane
{
    private static final Duration DEFAULT_ANIMATION_DURATION = Duration.millis(200);
    private static final double DEFAULT_CORNER_RADIUS = 10;
    private static final double DEFAULT_ARROW_INDENT = 10;
    private static final ARROW_POSITION DEFAULT_ARROW_POSTION = ARROW_POSITION.LEFT_TOP;
    private static final double DEFAULT_ARROW_WIDTH = 20;
    private static final double DEFAULT_ARROW_HEIGHT = 10;
    private static final double DEFAULT_BACKGROUND_INSETS = 10;

    private Pane pane;

    private ARROW_POSITION arrowPos;

    private Path path;

    private MoveTo start;

    private HLineTo topL;
    private HLineTo topR;
    private HLineTo bottom;
    private VLineTo rightT;
    private VLineTo rightB;
    private VLineTo leftB;
    private VLineTo leftT;

    private LineTo arrow1;
    private LineTo arrow2;

    private QuadCurveTo topRightCorner, bottomRightCorner, bottomLeftCorner, topLeftCorner;

    private ClosePath finish;

    private DoubleProperty arrowIndentProperty;
    private DoubleProperty cornerRadiusProperty;
    private DoubleProperty arrowWidthProperty;
    private DoubleProperty arrowHeightProperty;
    private ObjectProperty<ARROW_POSITION> arrowPositionProperty;

    private BorderPane centerNode;

    private Animation showAnimation;
    private ParallelTransition defaultShowAnimation; 
    private TranslateTransition showTranslateTransition;
    private FadeTransition showFadeTransition;

    private Animation hideAnimation;
    private ParallelTransition defaultHideAnimation; 
    private TranslateTransition hideTranslateTransition;
    private FadeTransition hideFadeTransition;

    public EmbeddedPopOver()
    {
        // Properties
        cornerRadiusProperty = new SimpleDoubleProperty();
        cornerRadiusProperty.set(DEFAULT_CORNER_RADIUS);

        arrowIndentProperty = new SimpleDoubleProperty();
        arrowIndentProperty.set(DEFAULT_ARROW_INDENT);

        arrowPositionProperty = new SimpleObjectProperty<>();
        arrowPositionProperty.set(DEFAULT_ARROW_POSTION);

        arrowWidthProperty = new SimpleDoubleProperty();
        arrowWidthProperty.set(DEFAULT_ARROW_WIDTH);

        arrowHeightProperty = new SimpleDoubleProperty();
        arrowHeightProperty.set(DEFAULT_ARROW_HEIGHT);

        // Layout
        centerNode = new BorderPane();

        start = new MoveTo(0, 0);

        topL = new HLineTo();

        arrow1 = new LineTo(10, -10);
        arrow1.setAbsolute(false);

        arrow2 = new LineTo(10, 10);
        arrow2.setAbsolute(false);
        
        topR = new HLineTo();

        topRightCorner = new QuadCurveTo(20, 0, 20, 20);
        topRightCorner.controlXProperty().bind(cornerRadiusProperty);
        topRightCorner.setControlY(0);
        topRightCorner.xProperty().bind(cornerRadiusProperty);
        topRightCorner.yProperty().bind(cornerRadiusProperty);
        topRightCorner.setAbsolute(false);

        rightT = new VLineTo();

        rightB = new VLineTo();

        bottomRightCorner = new QuadCurveTo(0, 20, -20, 20);
        bottomRightCorner.setControlX(0);
        bottomRightCorner.controlYProperty().bind(cornerRadiusProperty);
        bottomRightCorner.xProperty().bind(cornerRadiusProperty.multiply(-1));
        bottomRightCorner.yProperty().bind(cornerRadiusProperty);
        bottomRightCorner.setAbsolute(false);

        bottom = new HLineTo();

        bottomLeftCorner = new QuadCurveTo(-20, 0, -20, -20);
        bottomLeftCorner.controlXProperty().bind(cornerRadiusProperty.multiply(-1));
        bottomLeftCorner.setControlY(0);
        bottomLeftCorner.xProperty().bind(cornerRadiusProperty.multiply(-1));
        bottomLeftCorner.yProperty().bind(cornerRadiusProperty.multiply(-1));
        bottomLeftCorner.setAbsolute(false);

        leftB = new VLineTo();

        leftT = new VLineTo();

        topLeftCorner = new QuadCurveTo(20, 0, 20, 20);
        topLeftCorner.setControlX(0);
        topLeftCorner.controlYProperty().bind(cornerRadiusProperty.multiply(-1));
        topLeftCorner.xProperty().bind(cornerRadiusProperty);
        topLeftCorner.yProperty().bind(cornerRadiusProperty.multiply(-1));
        topLeftCorner.setAbsolute(false);

        finish = new ClosePath();

        path = new Path();

        //super.getChildren().add(path);
        super.setShape(path);
        super.getChildren().add(centerNode);
        super.setVisible(false);
        StackPane.setMargin(centerNode, new Insets(10 + arrowHeightProperty.get(), 10, 10, 10));
        StackPane.setAlignment(centerNode, Pos.CENTER);

        super.getStyleClass().add("embedded-popover");

        // Animations

        showTranslateTransition = new TranslateTransition();
        showTranslateTransition.setFromY(-10);
        showTranslateTransition.setToY(0);
        showTranslateTransition.setInterpolator(Interpolator.EASE_OUT);
        showTranslateTransition.setNode(this);
        showTranslateTransition.setDuration(DEFAULT_ANIMATION_DURATION);

        showFadeTransition = new FadeTransition();
        showFadeTransition.setToValue(1);
        showFadeTransition.setFromValue(0);
        showFadeTransition.setInterpolator(Interpolator.EASE_OUT);
        showFadeTransition.setNode(this);
        showFadeTransition.setDuration(DEFAULT_ANIMATION_DURATION);

        defaultShowAnimation = new ParallelTransition();
        defaultShowAnimation.getChildren().addAll(showTranslateTransition, showFadeTransition);
        defaultShowAnimation.setNode(this);

        showAnimation = defaultShowAnimation;

        hideTranslateTransition = new TranslateTransition();
        hideTranslateTransition.setFromY(0);
        hideTranslateTransition.setToY(10);
        hideTranslateTransition.setInterpolator(Interpolator.EASE_IN);
        hideTranslateTransition.setNode(this);
        hideTranslateTransition.setDuration(DEFAULT_ANIMATION_DURATION);

        hideFadeTransition = new FadeTransition();
        hideFadeTransition.setToValue(0);
        hideFadeTransition.setFromValue(1);
        hideFadeTransition.setInterpolator(Interpolator.EASE_IN);
        hideFadeTransition.setNode(this);
        hideFadeTransition.setDuration(DEFAULT_ANIMATION_DURATION);

        defaultHideAnimation = new ParallelTransition();
        defaultHideAnimation.getChildren().addAll(hideTranslateTransition, hideFadeTransition);
        defaultHideAnimation.setNode(this);
        defaultHideAnimation.setOnFinished(evt -> this.setVisible(false));

        hideAnimation = defaultHideAnimation;
    }

    public void setCornerRadius(final double cornerRadius)
    {
        cornerRadiusProperty.set(cornerRadius);
    }

    public void setArrowIndent(final double arrowIndent)
    {
        arrowIndentProperty.set(arrowIndent);
    }        

    public void setShowAnimation(final Animation animation)
    {
        showAnimation = animation;
    }

    public void setHideAnimation(final Animation animation)
    {
        hideAnimation = animation;
        hideAnimation.setOnFinished(evt -> this.setVisible(false));
    }

    public void setPane(final Pane pane)
    {
        this.pane = pane;
        Platform.runLater(() -> pane.getChildren().add(this));
    }

    public void setArrowPosition(final ARROW_POSITION arrowPos)
    {
        arrowPositionProperty.set(arrowPos);
        updatePath();
    }

    public void show(final Node origin)
    {
        Region r = (Region) origin;

        double x = FXUtils.getSceneX(origin) + (r.getWidth() / 2.0) - DEFAULT_BACKGROUND_INSETS;
        double y = FXUtils.getSceneY(origin) - DEFAULT_BACKGROUND_INSETS;

        switch(arrowPositionProperty.get())
        {
            case TOP_LEFT:
                x -= cornerRadiusProperty.get() + arrowIndentProperty.get() + arrowWidthProperty.divide(2.0).get();
                y += r.getHeight();
                break;
            case TOP_RIGHT:
                x -= this.getWidth() - DEFAULT_BACKGROUND_INSETS * 2 - arrowIndentProperty.get() - cornerRadiusProperty.get() - arrowWidthProperty.divide(2.0).get();
                y += r.getHeight();
                break;
        }

        super.setVisible(true);
        super.setLayoutX(x);
        super.setLayoutY(y);
        
        showAnimation.play();
    }

    public void hide()
    {
        hideAnimation.play();
    }

    public void setContent(final Node node)
    {
        centerNode.setCenter(node);
    }

    private void updatePath()
    {
        switch(arrowPositionProperty.get())
        {
            case TOP_LEFT:
                arrow1.xProperty().bind(arrowWidthProperty.divide(2.0));
                arrow2.xProperty().bind(arrowWidthProperty.divide(2.0));
                arrow1.yProperty().bind(arrowHeightProperty.multiply(-1.0));
                arrow2.yProperty().bind(arrowHeightProperty);

                topL.xProperty().bind(arrowIndentProperty);
                topL.setAbsolute(false);
                topR.xProperty().bind(centerNode.widthProperty().subtract(cornerRadiusProperty.add(arrowIndentProperty)));
                topR.setAbsolute(false);
                rightT.yProperty().bind(centerNode.heightProperty());
                rightT.setAbsolute(false);
                bottom.xProperty().set(0);
                leftB.yProperty().bind(cornerRadiusProperty);

                path.getElements().setAll(start, topL, arrow1, arrow2, topR, topRightCorner, rightT, bottomRightCorner, bottom, bottomLeftCorner, leftB, topLeftCorner, finish);
                break;
            case TOP_RIGHT:
                arrow1.xProperty().bind(arrowWidthProperty.divide(2.0));
                arrow2.xProperty().bind(arrowWidthProperty.divide(2.0));
                arrow1.yProperty().bind(arrowHeightProperty.multiply(-1.0));
                arrow2.yProperty().bind(arrowHeightProperty);

                topL.xProperty().bind(centerNode.widthProperty().subtract(arrowIndentProperty.add(arrowWidthProperty)));
                topL.setAbsolute(false);
                topR.xProperty().bind(arrowIndentProperty);
                topR.setAbsolute(false);
                rightT.yProperty().bind(centerNode.heightProperty());
                rightT.setAbsolute(false);
                bottom.setX(0);
                leftB.yProperty().bind(cornerRadiusProperty);

                path.getElements().setAll(start, topL, arrow1, arrow2, topR, topRightCorner, rightT, bottomRightCorner, bottom, bottomLeftCorner, leftB, topLeftCorner, finish);
                break;
           case LEFT_TOP:
                arrow1.xProperty().bind(arrowHeightProperty.multiply(-1.0));
                arrow1.yProperty().bind(arrowWidthProperty.divide(2.0));
                arrow2.xProperty().bind(arrowHeightProperty);
                arrow2.yProperty().bind(arrowWidthProperty);

                leftB.yProperty().bind(centerNode.heightProperty().subtract(arrowIndentProperty.add(arrowWidthProperty)));
                leftT.yProperty().bind(arrowIndentProperty);
                leftT.setAbsolute(false);
               break;
           case RIGHT_TOP:
               break;
        }

    }

    public enum ARROW_POSITION
    {
        TOP_LEFT,
        TOP_RIGHT,
        LEFT_TOP,
        RIGHT_TOP;
    }
}

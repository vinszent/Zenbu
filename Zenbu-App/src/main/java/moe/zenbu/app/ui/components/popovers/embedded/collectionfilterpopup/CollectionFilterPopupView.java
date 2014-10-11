package moe.zenbu.app.ui.components.popovers.embedded.collectionfilterpopup;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.EmbeddedPopOver;
import moe.zenbu.app.ui.controls.EmbeddedPopOver.ARROW_POSITION;
import moe.zenbu.app.ui.workbench.WorkbenchModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnFinished;

public class CollectionFilterPopupView extends DefaultView<CollectionFilterPopupModel, EmbeddedPopOver, CollectionFilterPopupController>
{
    private static final Log log = LogFactory.getLog(CollectionFilterPopupView.class);

    private static int DURATION_MILLIS = 400;

    private FXMLLoader fxmlLoader;

    private ParallelTransition showAnimation;
    private TranslateTransition showTranslateAnimation;
    private FadeTransition showFadeAnimation;

    @OnFinished(name = "Hide")
    private ParallelTransition hideAnimation;
    private TranslateTransition hideTranslateAnimation;
    private FadeTransition hideFadeAnimation;

    @FXML
    private ComboBox statusBox;

    @FXML
    private ComboBox sortBox;

    @FXML
    private VBox filterBox;

    public CollectionFilterPopupView(final CollectionFilterPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popovers/CollectionFilterPopOver.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            log.error("Could not load collection filter embedded popup fxml", e);
        }

        statusBox.setValue("All");
        statusBox.getItems().addAll("All", "Current", "Completed", "Planned", "On Hold", "Dropped");

        sortBox.getItems().addAll(I18n.getLocalisedString("collection.sort.alphabetic"), I18n.getLocalisedString("collection.sort.progress"), I18n.getLocalisedString("collection.sort.score"));
        sortBox.setValue(I18n.getLocalisedString("collection.sort.alphabetic"));

        showTranslateAnimation = new TranslateTransition();
        showTranslateAnimation.setFromY(-25);
        showTranslateAnimation.setToY(0.0);
        showTranslateAnimation.setInterpolator(Interpolator.EASE_OUT);
        showTranslateAnimation.setNode(getRootNode());
        showTranslateAnimation.setDuration(Duration.millis(DURATION_MILLIS));

        showFadeAnimation = new FadeTransition();
        showFadeAnimation.setToValue(1.0);
        showFadeAnimation.setFromValue(0.0);
        showFadeAnimation.setInterpolator(Interpolator.EASE_OUT);
        showFadeAnimation.setNode(getRootNode());
        showFadeAnimation.setDuration(Duration.millis(DURATION_MILLIS));

        showAnimation = new ParallelTransition();
        showAnimation.getChildren().addAll(showTranslateAnimation, showFadeAnimation);
        showAnimation.setNode(getRootNode());

        hideTranslateAnimation = new TranslateTransition();
        hideTranslateAnimation.setToY(25);
        hideTranslateAnimation.setFromY(0.0);
        hideTranslateAnimation.setInterpolator(Interpolator.EASE_IN);
        hideTranslateAnimation.setNode(getRootNode());
        hideTranslateAnimation.setDuration(Duration.millis(DURATION_MILLIS));

        hideFadeAnimation = new FadeTransition();
        hideFadeAnimation.setToValue(0.0);
        hideFadeAnimation.setFromValue(1.0);
        hideFadeAnimation.setInterpolator(Interpolator.EASE_IN);
        hideFadeAnimation.setNode(getRootNode());
        hideFadeAnimation.setDuration(Duration.millis(DURATION_MILLIS));

        hideAnimation = new ParallelTransition();
        hideAnimation.getChildren().addAll(hideTranslateAnimation, hideFadeAnimation);
        hideAnimation.setNode(getRootNode());

        super.getRootNode().setContent(filterBox);
        super.getRootNode().setVisible(false);
        super.getRootNode().setArrowPosition(ARROW_POSITION.TOP_LEFT);
        super.getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane());
        super.getRootNode().getStyleClass().add("collection-filter-popover");

    }

    public ParallelTransition getShowAnimation() 
    {
        return showAnimation;
    }

    public ParallelTransition getHideAnimation() 
    {
        return hideAnimation;
    }

    public ComboBox getStatusBox() {
        return statusBox;
    }

    public ComboBox getSortBox() {
        return sortBox;
    }
}

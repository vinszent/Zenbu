package moe.zenbu.app.ui.components.popovers.embedded.hamburger;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import moe.zenbu.app.ui.controls.EmbeddedPopOver;
import moe.zenbu.app.ui.controls.EmbeddedPopOver.ARROW_POSITION;
import moe.zenbu.app.ui.workbench.WorkbenchModel;

import org.controlsfx.control.SegmentedButton;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

public class HamburgerPopupView extends DefaultView<HamburgerPopupModel, EmbeddedPopOver, HamburgerPopupController>
{
    private static int DURATION_MILLIS = 400;

    private VBox hamburgerBox;

    private StackPane contentPane;

    private SegmentedButton contentButton;
    private ToggleGroup contentToggles;
    @OnAction(name = "AddSeries")
    private ToggleButton addSeriesButton;
    @OnAction(name = "History")
    private ToggleButton historyButton;
    @OnAction(name = "Sync")
    private ToggleButton syncButton;

    private Rectangle clip;

    public HamburgerPopupView(final HamburgerPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        addSeriesButton = new ToggleButton("Add");
        addSeriesButton.setSelected(true);

        historyButton = new ToggleButton("History");

        syncButton = new ToggleButton("Sync");

        contentToggles = new ToggleGroup();
        contentToggles.getToggles().addAll(addSeriesButton, historyButton, syncButton);

        contentButton = new SegmentedButton();
        //contentButton.translateXProperty().bind(getRootNode().widthProperty().subtract(contentButton.widthProperty()));
        contentButton.getButtons().addAll(addSeriesButton, historyButton, syncButton);

        contentPane = new StackPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);

        hamburgerBox = new VBox();
        hamburgerBox.getStyleClass().add("hamburger-box");
        hamburgerBox.getChildren().addAll(contentButton, contentPane);

        contentPane.maxWidthProperty().bind(hamburgerBox.widthProperty());

        clip = new Rectangle();
        clip.widthProperty().bind(contentPane.widthProperty());
        clip.heightProperty().bind(getRootNode().heightProperty());
        contentPane.setClip(clip);

        getRootNode().setVisible(false);
        getRootNode().setContent(hamburgerBox);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(getRootNode().widthProperty());
        clip.heightProperty().bind(getRootNode().heightProperty());
        getRootNode().setClip(clip);

        super.getRootNode().setArrowPosition(ARROW_POSITION.TOP_RIGHT);
        super.getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane());
    }

    public VBox getHamburgerBox()
    {
        return hamburgerBox;
    }

    public StackPane getContentPane()
    {
        return contentPane;
    }

    public SegmentedButton getContentButton()
    {
        return contentButton;
    }

    public ToggleGroup getContentToggles()
    {
        return contentToggles;
    }

    public ToggleButton getAddSeriesButton()
    {
        return addSeriesButton;
    }

    public ToggleButton getHistoryButton()
    {
        return historyButton;
    }

    public ToggleButton getSyncButton()
    {
        return syncButton;
    }
}

package moe.zenbu.app.ui.components.functionbar.collection.standard;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.AutoCompleteField;

import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.enzo.flippanel.FlipPanel;

public class StandardCollectionFunctionBarView extends DefaultView<StandardCollectionFunctionBarModel, HBox, StandardCollectionFunctionBarController>
{
    private static final Logger log = LoggerFactory.getLogger(StandardCollectionFunctionBarView.class);

    // Filter items
    @OnAction(name = "ShowFilter")
    private ToggleButton filterButton;
    private SVGPath filterSvg;
    private ParallelTransition showFilterAnimation;
    private Timeline showFilterBounceAnimation;
    private Timeline showFilterBounceAnimation1;
    private FadeTransition showFilterFadeAnimation;
    private FadeTransition hideFilterFadeAnimation;
    private Timeline hideFilterBounceAnimation;
    private SequentialTransition hideFilterAnimation;
    private VBox filterBox;
    private Group filterGroup;
    private HBox sorterBox;
    private ComboBox statusBox;
    private ComboBox orderBox;
    private AutoCompleteField autoCompleteField;

    private FlipPanel filterPanel;

    // Tools items
    private HBox toolsBox;
    private Button addButton;
    private SVGPath addSvg;
    private ToggleButton historyButton;
    private SVGPath historySvg;
    private Button syncButton;
    private SVGPath syncSvg;

    // Spacers
    private Region spacer1;
    private Region spacer2;

    public StandardCollectionFunctionBarView(final StandardCollectionFunctionBarModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        // Setup filter
        filterSvg = new SVGPath();
        filterSvg.getStyleClass().add("svg");
        filterSvg.setContent("m 1.443926,-44.692251 c -4.1921435,0 -7.5903743,3.398231 -7.5903743,7.588957 0,4.192851 3.73529,7.928141 7.9274334,7.928141 4.1921435,0 7.5903742,-3.39823 7.5903742,-7.590374 0,-4.191435 -3.7352899,-7.926724 -7.9274333,-7.926724 m 12.724935,23.996371 -6.6965133,-6.696513 c -1.6590659,1.009757 -3.6069872,1.591369 -5.6913626,1.591369 -6.0542946,0 -11.3008541,-5.246558 -11.3008541,-11.30227 0,-6.054295 4.9087921,-10.963087 10.963795,-10.963087 6.0550039,0 11.300854,5.246559 11.300854,11.300854 0,2.013136 -0.545106,3.898679 -1.492132,5.51982 l 6.73302,6.73302 c 0.658876,0.658876 0.658876,1.727115 0,2.385283 l -1.669698,1.670407 c -0.658876,0.658877 -1.488232,0.419994 -2.147109,-0.238883");

        filterButton = new ToggleButton();
        filterButton.getStyleClass().add("functionbar-button");
        filterButton.setId("filter");
        filterButton.setGraphic(filterSvg);
        filterButton.setOnMouseEntered(evt -> ((Node) evt.getSource()).setCursor(Cursor.DEFAULT));

        filterBox = new VBox();
        filterBox.setManaged(false);
        filterBox.setVisible(false);
        filterBox.setPrefWidth(240);
        filterBox.setOnMouseEntered(evt -> ((Node) evt.getSource()).setCursor(Cursor.DEFAULT));

        sorterBox = new HBox();

        statusBox = new ComboBox();
        statusBox.getItems().addAll("All", "Current", "Completed", "Planned", "On Hold", "Dropped");
        HBox.setHgrow(statusBox, Priority.SOMETIMES);

        orderBox = new ComboBox();
        orderBox.getItems().addAll(I18n.getLocalisedString("collection.sort.alphabetic"), I18n.getLocalisedString("collection.sort.progress"), I18n.getLocalisedString("collection.sort.score"));
        HBox.setHgrow(orderBox, Priority.SOMETIMES);

        autoCompleteField = new AutoCompleteField(true);
        autoCompleteField.getSearchBox().setPromptText("Search anime...");
        //autoCompleteField.getRoot().setOnMouseEntered(evt -> ((Node) evt.getSource()).setCursor(Cursor.DEFAULT));

        sorterBox.getChildren().addAll(statusBox, orderBox);
        sorterBox.setAlignment(Pos.CENTER);

        filterBox.getChildren().addAll(sorterBox, autoCompleteField.getRoot());
        filterBox.setAlignment(Pos.CENTER);

        filterPanel = new FlipPanel(Orientation.VERTICAL);

        filterPanel.getFront().getChildren().add(autoCompleteField.getRoot());
        filterPanel.getBack().getChildren().add(sorterBox);

        // Setup tools
        toolsBox = new HBox();
        toolsBox.getStyleClass().add("tools-box");

        addSvg = new SVGPath();
        addSvg.getStyleClass().add("svg");
        addSvg.setContent("m -28.448276,-17.586207 -10.137931,0 0,10.1379311 C -38.586207,-6.1148966 -39.666621,-6 -41,-6 c -1.333379,0 -2.413793,-0.1148966 -2.413793,-1.4482759 l 0,-10.1379311 -10.137931,0 C -54.885103,-17.586207 -55,-18.666621 -55,-20 c 0,-1.333379 0.114897,-2.413793 1.448276,-2.413793 l 10.137931,0 0,-10.137931 C -43.413793,-33.885103 -42.333379,-34 -41,-34 c 1.333379,0 2.413793,0.114897 2.413793,1.448276 l 0,10.137931 10.137931,0 c 1.333379,0 1.448276,1.080414 1.448276,2.413793 0,1.333379 -0.114897,2.413793 -1.448276,2.413793");

        addButton = new Button();
        addButton.getStyleClass().add("functionbar-button");
        addButton.setId("add");
        addButton.setGraphic(addSvg);

        historySvg = new SVGPath();
        historySvg.setContent("m -39,-4.4 -14,0 c -1.1048,0 -2,-1.25328 -2,-2.8 0,-1.54672 0.8952,-2.8 2,-2.8 l 14,0 c 1.1048,0 2,1.25328 2,2.8 0,1.54672 -0.8952,2.8 -2,2.8 m 0,11.2 -14,0 c -1.1048,0 -2,-1.25328 -2,-2.8 0,-1.54672 0.8952,-2.8 2,-2.8 l 14,0 c 1.1048,0 2,1.25328 2,2.8 0,1.54672 -0.8952,2.8 -2,2.8 m -14,5.6 14,0 c 1.1048,0 2,1.25328 2,2.8 0,1.54672 -0.8952,2.8 -2,2.8 l -14,0 c -1.1048,0 -2,-1.25328 -2,-2.8 0,-1.54672 0.8952,-2.8 2,-2.8 m -8,5.6 -2,0 c -1.1048,0 -2,-1.25328 -2,-2.8 0,-1.54672 0.8952,-2.8 2,-2.8 l 2,0 c 1.1048,0 2,1.25328 2,2.8 0,1.54672 -0.8952,2.8 -2,2.8 m 0,-11.2 -2,0 c -1.1048,0 -2,-1.25328 -2,-2.8 0,-1.54672 0.8952,-2.8 2,-2.8 l 2,0 c 1.1048,0 2,1.25328 2,2.8 0,1.54672 -0.8952,2.8 -2,2.8 m 0,-11.2 -2,0 c -1.1048,0 -2,-1.25328 -2,-2.8 0,-1.54672 0.8952,-2.8 2,-2.8 l 2,0 c 1.1048,0 2,1.25328 2,2.8 0,1.54672 -0.8952,2.8 -2,2.8");
        historySvg.getStyleClass().add("svg");

        historyButton = new ToggleButton();
        historyButton.getStyleClass().add("functionbar-button");
        historyButton.setId("history");
        historyButton.setGraphic(historySvg);
        historyButton.prefHeightProperty().bind(getRootNode().heightProperty());
        //historyButton.setGraphic(historyPane);
        
        syncSvg = new SVGPath();
        syncSvg.setContent("m -34.119401,-52.469561 0.02596,-9.960829 3.937415,3.965415 c 3.862293,-4.152879 3.794342,-10.661513 -0.217171,-14.701708 -1.642098,-1.654048 -3.694975,-2.640536 -5.830487,-2.964586 L -36.08417,-79.676 c 2.996,0.364 5.894341,1.700146 8.190341,4.012196 5.376342,5.415267 5.440194,14.154681 0.209659,19.688096 l 2.971756,2.992927 -9.406975,0.51322 z M -45.46589,-72.886342 c -3.862633,4.152196 -3.794,10.661171 0.217513,14.701367 1.642098,1.653365 3.694976,2.640536 5.829464,2.964585 l -0.119168,3.54439 c -2.995317,-0.363658 -5.893658,-1.699804 -8.19,-4.012196 -5.376,-5.414927 -5.439854,-14.154 -0.21,-19.687414 l -2.971756,-2.993267 9.406975,-0.513562 -0.02596,9.960829 -3.937074,-3.964732 z");
        syncSvg.getStyleClass().add("svg");

        syncButton = new Button();
        syncButton.getStyleClass().add("functionbar-button");
        syncButton.setId("sync");
        syncButton.setGraphic(syncSvg);

        toolsBox.getChildren().addAll(historyButton);
        toolsBox.setOnMouseEntered(evt -> ((Node) evt.getSource()).setCursor(Cursor.DEFAULT));

        spacer1 = new Region();

        getRootNode().setHgrow(spacer1, Priority.ALWAYS);
        getRootNode().getStyleClass().add("standard-collection-functionbar");
        getRootNode().getChildren().addAll(filterButton, autoCompleteField.getRoot(), spacer1, toolsBox);
        //getRootNode().getChildren().addAll(autoCompleteField.getRoot(), spacer1, toolsBox);
    }

    public ToggleButton getFilterButton()
    {
        return filterButton;
    }

    public SVGPath getFilterSvg()
    {
        return filterSvg;
    }

    public Animation getShowFilterAnimation()
    {
        return showFilterAnimation;
    }

    public Animation getHideFilterAnimation()
    {
        return hideFilterAnimation;
    }

    public VBox getFilterBox()
    {
        return filterBox;
    }

    public AutoCompleteField getAutoCompleteField()
    {
        return autoCompleteField;
    }

    public FlipPanel getFilterPanel()
    {
        return filterPanel;
    }

    public Button getAddButton()
    {
        return addButton;
    }

    public SVGPath getAddSvg()
    {
        return addSvg;
    }

    public ToggleButton getHistoryButton()
    {
        return historyButton;
    }

    public SVGPath getHistorySvg()
    {
        return historySvg;
    }

    public Button getSyncButton()
    {
        return syncButton;
    }

    public SVGPath getSyncSvg()
    {
        return syncSvg;
    }

    public ComboBox getStatusBox()
    {
        return statusBox;
    }

    public ComboBox getOrderBox()
    {
        return orderBox;
    }
}

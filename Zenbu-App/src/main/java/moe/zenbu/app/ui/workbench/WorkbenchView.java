package moe.zenbu.app.ui.workbench;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import moe.zenbu.app.ui.controls.WindowResizeButton;

import org.jrebirth.af.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkbenchView extends DefaultView<WorkbenchModel, StackPane, WorkbenchController>
{
    private static final Logger log = LoggerFactory.getLogger(WorkbenchView.class);

    private BorderPane workbenchPane;

    private Pane absolutePane;

    private WindowResizeButton wrb;

    public WorkbenchView(final WorkbenchModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        workbenchPane = new BorderPane();
        workbenchPane.getStyleClass().add("workbench-pane");
        getRootNode().getChildren().add(workbenchPane);
        getRootNode().setPrefSize(600, 800);

        absolutePane = new Pane();
        absolutePane.setPickOnBounds(false);

        getRootNode().getChildren().add(absolutePane);
        getRootNode().getStyleClass().add("layer-pane");

        Rectangle rec = new Rectangle();
        rec.setArcWidth(15);
        rec.setArcHeight(15);
        rec.widthProperty().bind(getRootNode().widthProperty());
        rec.heightProperty().bind(getRootNode().heightProperty());
        //getRootNode().setClip(rec);

        workbenchPane.setTop(getModel().getInnerModel(WorkbenchInnerModels.FUNCTIONBAR).getRootNode());
        workbenchPane.setLeft(getModel().getInnerModel(WorkbenchInnerModels.SWITCHER).getRootNode());
        workbenchPane.setCenter(getModel().getInnerModel(WorkbenchInnerModels.PAGE).getRootNode());

        wrb = new WindowResizeButton(getModel().getLocalFacade().getGlobalFacade().getApplication().getStage(), 800, 600);
        absolutePane.getChildren().add(wrb);
        wrb.layoutXProperty().bind(getRootNode().widthProperty().subtract(wrb.widthProperty()));
        wrb.layoutYProperty().bind(getRootNode().heightProperty().subtract(wrb.heightProperty()));
    }

    public Pane getAbsolutePane() 
    {
        return absolutePane;
    }
}

package moe.zenbu.app.ui.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import org.controlsfx.control.PopOver;

public class TimelineFlow extends VBox
{
    private DoubleProperty graphicRadius = new SimpleDoubleProperty(30.0);

    public TimelineFlow()
    {
        getStyleClass().add("timelineflow");
    }

    public Double getGraphicRadius()
    {
        return graphicRadius.get(); 
    }        

    public void setGraphic(double radius)
    {
        graphicRadius.set(radius); 
    }        

    public DoubleProperty graphicRadiusProperty()
    {
        return graphicRadius; 
    }        

    public void addParentItem(ImagePattern graphic, Node content)
    {
        getChildren().add(new TimelineFlowItem(TimelineFlowItem.GRAPHIC, graphic, content));
    }        

    public void addChildItem(Node content)
    {
        getChildren().add(new TimelineFlowItem(TimelineFlowItem.LINE, content));
    }        

    public void addItem(String type)
    {
    }

    private class TimelineFlowItem extends HBox
    {
        public final static String GRAPHIC = "graphic";
        public final static String LINE = "line";

        // Class items
        private String type;

        // Graphic items
        private StackPane graphicBox;
        private Circle graphicClip;
        private Line connectingLine;

        // Content items
        private StackPane contentBox;

        // Popup items
        private PopOver popOver;
        private Text popOverText;

        public TimelineFlowItem(String type, Node content)
        {
            this(type, null, content);
        }

        public TimelineFlowItem(String type, ImagePattern graphic, Node content)
        {
            this.type = type;

            // Setup timeline items
            graphicClip = new Circle();            
            graphicClip.radiusProperty().bind(graphicRadius);
            graphicClip.setFill(graphic);
            graphicClip.getStyleClass().add("clip-circle");

            connectingLine = new Line();
            connectingLine.getStyleClass().add("connecting-line");

            graphicBox = new StackPane();
            graphicBox.getStyleClass().add("graphic-box");
            HBox.setHgrow(graphicBox, Priority.NEVER);

            // Setup content items
            contentBox = new StackPane();
            contentBox.getStyleClass().add("content-box");
            contentBox.getChildren().add(content);
            HBox.setHgrow(contentBox, Priority.ALWAYS);

            // Setup root
            getChildren().addAll(graphicBox, contentBox);
            getStyleClass().add("item-box");

            // Finalize position specific properties
            switch(type)
            {
                case GRAPHIC:
                    graphicBox.getChildren().addAll(graphicClip);

                    getStyleClass().add("item-box-graphic");
                    break;
                case LINE:
                    connectingLine.setStartY(0.5);
                    connectingLine.endYProperty().bind(this.heightProperty().subtract(0.5));

                    graphicBox.getChildren().addAll(connectingLine);

                    getStyleClass().add("item-box-line");
                    translateXProperty().bind(graphicClip.radiusProperty());
                    break;
            }
        }
    }
}

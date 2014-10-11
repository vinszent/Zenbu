package moe.zenbu.app.ui.controls;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class WindowResizeButton extends Region
{
    private Stage stage;

    private double minWidth;
    private double minHeight;
    
    private double initx;
    private double inity;

    public WindowResizeButton(final Stage stage, final double minWidth, final double minHeight)
    {
        this.stage = stage;
        this.minWidth = minWidth;
        this.minHeight = minHeight;

        super.setPrefSize(15, 15);

        super.setOnMousePressed(evt ->
        {
            initx = evt.getX();
            inity = evt.getY();
        });

        super.setOnMouseDragged(evt ->
        {
            double newWidth = stage.getWidth() + (evt.getX() - initx);
            double newHeight = stage.getHeight() + (evt.getY() - inity);

            if(newWidth > minWidth && newHeight > minHeight)
            {
                stage.setWidth(newWidth);
                stage.setHeight(newHeight);
            }

            initx = evt.getX();
            inity = evt.getY();
            evt.consume();
        });

        super.setOnMouseEntered(evt -> ((Node) evt.getSource()).setCursor(Cursor.SE_RESIZE));
    }
}

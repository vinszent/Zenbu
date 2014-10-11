package moe.zenbu.app.ui.controls.halfincrementalrating;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DividedRatingStar
{
    public static final String FULL = "full";
    public static final String HALF = "half";
    public static final String EMPTY = "empty";

    private static final Logger log = LoggerFactory.getLogger(DividedRatingStar.class);

    private FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/controls/halfincrementalrating/DividedRatingStar.fxml"));

    private int pos;

    private String fill;

    @FXML
    private HBox dividedRatingStar;

    @FXML
    private StackPane leftStarPane;

    @FXML
    private SVGPath leftStarPath;

    @FXML
    private StackPane rightStarPane;

    @FXML
    private SVGPath rightStarPath;

    public DividedRatingStar(final int pos, final IntegerProperty ratingProperty)
    {
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            log.error("Could not load divided rating star fxml", e);
        }

        leftStarPane.setOnMouseExited(evt ->
        {
            if(evt.getX() < 0.0)
            {
                fill = EMPTY;

                if(pos == 1)
                {
                    ratingProperty.set(0);
                }
                else
                {
                    ratingProperty.set(pos);
                }

                leftStarPath.getStyleClass().remove("filled");
            }
            else
            {
                fill = HALF;

                if(!leftStarPath.getStyleClass().contains("filled"))
                {
                    leftStarPath.getStyleClass().add("filled");
                }
            }
        });
        leftStarPane.setOnMouseEntered(evt -> ratingProperty.set(pos * 2 - 1));
        rightStarPane.setOnMouseExited(evt ->
        {
            if(evt.getX() < 0.0)
            {
                fill = HALF;

                rightStarPath.getStyleClass().remove("filled");
            }
            else
            {
                fill = FULL;

                if(!rightStarPath.getStyleClass().contains("filled"))
                {
                    rightStarPath.getStyleClass().add("filled");
                }
            }
        });
        rightStarPane.setOnMouseEntered(evt -> ratingProperty.set(pos * 2));

        this.pos = pos;
    }

    public void fill()
    {
        if(!leftStarPath.getStyleClass().contains("filled"))
        {
            leftStarPath.getStyleClass().add("filled");
        }
        if(!rightStarPath.getStyleClass().contains("filled"))
        {
            rightStarPath.getStyleClass().add("filled");
        }
    }

    public void unFill()
    {
        leftStarPath.getStyleClass().remove("filled");
        rightStarPath.getStyleClass().remove("filled");
    }

    public void halfFill()
    {
        if(!leftStarPath.getStyleClass().contains("filled"))
        {
            leftStarPath.getStyleClass().add("filled");
        }
        rightStarPath.getStyleClass().remove("filled");
    }

    public int getPosition()
    {
        return pos;
    }

    public HBox getGraphic()
    {
        return dividedRatingStar;
    }

    public String getFill()
    {
        return fill;
    }
}

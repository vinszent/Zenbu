package moe.zenbu.app.ui.controls.halfincrementalrating;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.HBox;

public class HalfIncrementalRating extends HBox
{
    private List<DividedRatingStar> stars = new ArrayList<>();

    private IntegerProperty currentPos = new SimpleIntegerProperty();

    private IntegerProperty ratingProperty = new SimpleIntegerProperty();

    public HalfIncrementalRating()
    {
        this(5);
    }

    public HalfIncrementalRating(final int count)
    {
        for(int i = 1; i < count + 1; i++)
        {
            DividedRatingStar star = new DividedRatingStar(i, ratingProperty);

            stars.add(star);

            star.getGraphic().setOnMouseEntered(evt -> currentPos.set(star.getPosition()));

            super.getChildren().add(star.getGraphic());
        }

        currentPos.addListener((ov, oldVal, newVal) -> refresh(newVal.intValue()));

        super.getStyleClass().add("half-incremental-rating");
    }

    public void setRating(final double rating)
    {
        ratingProperty.set((int) Math.round(rating));

        double pos = rating / 2.0;

        stars.forEach(star ->
        {
            if(star.getPosition() < pos)
            {
                star.fill();
            }
            else if(star.getPosition() - 0.5 == pos)
            {
                star.halfFill();
            }
            else
            {
                star.unFill();
            }
        });
    }

    private void refresh(final int pos)
    {
        stars.forEach(star ->
        {
            if(star.getPosition() < pos)
            {
                star.fill();
            }
            else if(star.getPosition() > pos)
            {
                star.unFill();
            }
        });
    }

    public IntegerProperty getRatingProperty()
    {
        return ratingProperty;
    }
}

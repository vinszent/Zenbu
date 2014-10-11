package moe.zenbu.app.util;

import javafx.animation.Interpolator;
import javafx.scene.Node;

public class FXUtils
{
    public static final Interpolator WEB_EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);

    private FXUtils()
    {
    }

    public static double getScreenX(Node node)
    {
        return node.localToScene(0, 0).getX() + node.getScene().getX() + node.getScene().getWindow().getX();
    }

    public static double getSceneX(Node node)
    {
        return node.localToScene(0, 0).getX() + node.getScene().getX();
    }

    public static double getScreenY(Node node)
    {
        return node.localToScene(0, 0).getY() + node.getScene().getY() + node.getScene().getWindow().getY();
    }

    public static double getSceneY(Node node)
    {
        return node.localToScene(0, 0).getY() + node.getScene().getY();
    }
}

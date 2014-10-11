package moe.zenbu.app.ui.controls;

import java.lang.reflect.Method;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.layout.StackPane;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;

public class AnimatedScrollListView<T> extends ListView<T>
{
    private ObjectProperty<Skin<?>> skinProperty = new SimpleObjectProperty();
    private VirtualFlow flow;
    private StackPane sp = new StackPane();

    public AnimatedScrollListView()
    {
        super();

        ListViewSkin skin = (ListViewSkin) super.createDefaultSkin();
        skinProperty.set(skin);
        flow = (VirtualFlow) skin.getChildren().get(0);

        try
        {
            Method m = flow.getClass().getDeclaredMethod("getVbar");
            Method m1 = flow.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getChildren");
            m.setAccessible(true);
            m1.setAccessible(true);

            VirtualScrollBar vbar = (VirtualScrollBar) m.invoke(flow);
            ObservableList<Node> children = (ObservableList<Node>) m1.invoke(flow);

            children.remove(vbar);

            sp.getChildren().add(vbar);
            sp.prefHeightProperty().bind(flow.heightProperty());
            sp.prefWidthProperty().bind(vbar.widthProperty());

            //skin.getChildren().add(sp);
            children.add(sp);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        this.skinProperty().bind(skinProperty);
    }

    @Override
    protected void layoutChildren()
    {
        super.layoutChildren();

        sp.autosize();
        sp.setLayoutX(flow.getWidth());
    }
}

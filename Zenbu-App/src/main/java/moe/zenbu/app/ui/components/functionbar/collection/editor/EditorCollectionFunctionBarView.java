package moe.zenbu.app.ui.components.functionbar.collection.editor;

import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import moe.zenbu.app.resources.I18n;

import org.jrebirth.af.core.concurrent.AbstractJrbRunnable;
import org.jrebirth.af.core.concurrent.JRebirth;
import org.jrebirth.af.core.exception.JRebirthThreadException;
import org.jrebirth.af.core.ui.DefaultView;

public class EditorCollectionFunctionBarView extends DefaultView<EditorCollectionFunctionBarModel, HBox, EditorCollectionFunctionBarController>
{
    private Slider scaleSlider;
    private Slider hSpacingSlider;
    private Slider vSpacingSlider;

    public EditorCollectionFunctionBarView(final EditorCollectionFunctionBarModel model)
    {
        super(model);
    }
    
    @Override
    protected void initView()
    {
        scaleSlider = new Slider(0.5, 1.5, 1);
        hSpacingSlider = new Slider(-50, 100, 20);
        vSpacingSlider = new Slider(-50, 100, 20);
        
        JRebirth.runIntoJAT(new AbstractJrbRunnable("editor.collection.functionbar.initview")
        {
            
            @Override
            protected void runInto() throws JRebirthThreadException
            {
                scaleSlider.setTooltip(new Tooltip(I18n.getLocalisedString("editor.collection.functionbar.tooltip.scale")));
                hSpacingSlider.setTooltip(new Tooltip(I18n.getLocalisedString("editor.collection.functionbar.tooltip.hspacing")));
                vSpacingSlider.setTooltip(new Tooltip(I18n.getLocalisedString("editor.collection.functionbar.tooltip.vspacing")));
            }
        });
        
        getRootNode().getChildren().addAll(scaleSlider, hSpacingSlider, vSpacingSlider);
        getRootNode().getStyleClass().add("editor-collection-functionbar");
    }

    public Slider getScaleSlider()
    {
        return scaleSlider;
    }

    public Slider getHSpacingSlider()
    {
        return hSpacingSlider;
    }

    public Slider getVSpacingSlider()
    {
        return vSpacingSlider;
    }
}

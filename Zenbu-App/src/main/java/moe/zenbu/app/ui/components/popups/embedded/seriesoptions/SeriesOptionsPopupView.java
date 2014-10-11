package moe.zenbu.app.ui.components.popups.embedded.seriesoptions;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import moe.zenbu.app.resources.I18n;

import moe.zenbu.app.ui.controls.EmbeddedPopup;
import org.jrebirth.af.core.ui.DefaultView;

public class SeriesOptionsPopupView extends DefaultView<SeriesOptionsPopupModel, EmbeddedPopup, SeriesOptionsPopupController>
{
    private TabPane tabPane;
    private Tab generalTab;
    private Tab downloadTab;

    public SeriesOptionsPopupView(final SeriesOptionsPopupModel model)
    {
        super(model);
    }
    
    @Override
    protected void initView()
    {
        generalTab = new Tab(I18n.getLocalisedString("popup.series.option.general"));
        generalTab.setContent(getModel().getInnerModel(SeriesOptionsPopupInnerModels.GENERAL).getRootNode());
        generalTab.setClosable(false);
        
        tabPane = new TabPane();
        tabPane.getTabs().addAll(generalTab);
        
        getRootNode().setContent(tabPane);
        getRootNode().getStyleClass().add("series-options-popup");
        getRootNode().getStyleClass().add("inline-popup");
    }

    public TabPane getTabPane()
    {
        return tabPane;
    }
}

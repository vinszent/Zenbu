package moe.zenbu.app.ui.components.popups.embedded.seriesoptions.general;

import moe.zenbu.app.ui.components.popups.embedded.seriesoptions.SeriesOptionsPopupModel;

import org.jrebirth.af.core.ui.DefaultModel;

public class InlineSeriesOptionsPopupGeneralTabModel extends DefaultModel<InlineSeriesOptionsPopupGeneralTabModel, InlineSeriesOptionsPopupGeneralTabView>
{
    @Override
    protected void bind()
    {
        getView().getStatusBox().valueProperty().bindBidirectional(getModel(SeriesOptionsPopupModel.class).getStatusProperty());
    }
}

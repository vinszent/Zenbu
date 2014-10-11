package moe.zenbu.app.ui.components.popups.embedded.seriesoptions;

import moe.zenbu.app.ui.components.popups.embedded.seriesoptions.general.InlineSeriesOptionsPopupGeneralTabModel;

import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.InnerModel;
import org.jrebirth.af.core.ui.Model;

public enum SeriesOptionsPopupInnerModels implements InnerModel
{
    GENERAL(InlineSeriesOptionsPopupGeneralTabModel.class),
    DOWNLOADS(null);
    
    private final UniqueKey<? extends Model> modelKey;
    
    private SeriesOptionsPopupInnerModels(final Class<? extends Model> modelClass)
    {
        this.modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(modelClass);
    }

    @Override
    public UniqueKey<? extends Model> getKey()
    {
        return modelKey;
    }

}
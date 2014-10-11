package moe.zenbu.app.ui.components.functionbar;

import moe.zenbu.app.ui.components.functionbar.collection.CollectionFunctionBarModel;

import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.InnerModel;
import org.jrebirth.af.core.ui.Model;

public enum FunctionbarInnerModels implements InnerModel
{
    COLLECTION(CollectionFunctionBarModel.class),
    BROWSE(null),
    DOWNLOAD(null),
    CHAT(null),
    SETTINGS(null);

    private final UniqueKey<? extends Model> modelKey;

    private FunctionbarInnerModels(final Class<? extends Model> modelClass)
    {
        this.modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(modelClass);
//        this.modelKey = getLocalFacade().getGlobalFacade().getUiFacade().buildKey(modelClass);
    }

    @Override
    public UniqueKey<? extends Model> getKey()
    {
        return this.modelKey;
    }
}

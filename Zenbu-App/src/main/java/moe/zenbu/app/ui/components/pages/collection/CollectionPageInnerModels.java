package moe.zenbu.app.ui.components.pages.collection;

import moe.zenbu.app.ui.components.pages.collection.grid.CollectionGridModel;

import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.InnerModel;
import org.jrebirth.af.core.ui.Model;

public enum CollectionPageInnerModels implements InnerModel
{
    GRID(CollectionGridModel.class),
    LIST(null);

    private final UniqueKey<? extends Model> modelKey;

    private CollectionPageInnerModels(final Class<? extends Model> modelClass)
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

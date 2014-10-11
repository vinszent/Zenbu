package moe.zenbu.app.ui.components.pages.settings;

import moe.zenbu.app.ui.components.pages.settings.collection.SettingsCollectionPageModel;
import moe.zenbu.app.ui.components.pages.settings.lookandfeel.SettingsLafPageModel;

import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.InnerModel;
import org.jrebirth.af.core.ui.Model;

public enum SettingsPageInnerModels implements InnerModel
{
    COLLECTION(SettingsCollectionPageModel.class),
    LOOKANDFEEL(SettingsLafPageModel.class),
    CHAT(null),
    DOWNLOADS(null),
    SCROBBLER(null);

    private final UniqueKey<? extends Model> modelKey;

    private SettingsPageInnerModels(final Class<? extends Model> modelClass)
    {
        this.modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(modelClass);
    }

    @Override
    public UniqueKey<? extends Model> getKey()
    {
        return this.modelKey;
    }
}

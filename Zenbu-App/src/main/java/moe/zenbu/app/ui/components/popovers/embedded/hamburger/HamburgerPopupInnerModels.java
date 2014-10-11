package moe.zenbu.app.ui.components.popovers.embedded.hamburger;

import moe.zenbu.app.ui.components.popovers.embedded.hamburger.addseries.HamburgerAddSeriesModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.history.HistoryPopupModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.progress.HamburgerProgressModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.sync.SyncPopupModel;

import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.InnerModel;
import org.jrebirth.af.core.ui.Model;

public enum HamburgerPopupInnerModels implements InnerModel
{
    ADD_SERIES(HamburgerAddSeriesModel.class),
    HISTORY(HistoryPopupModel.class),
    SYNC(SyncPopupModel.class),
    PROGRESS(HamburgerProgressModel.class);

    private final UniqueKey<? extends Model> modelKey;

    private HamburgerPopupInnerModels(final Class<? extends Model> clazz)
    {
        this.modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(clazz);
    }

    @Override
    public UniqueKey<? extends Model> getKey()
    {
        return this.modelKey;
    }
}


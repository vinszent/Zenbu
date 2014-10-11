package moe.zenbu.app.ui.workbench;

import moe.zenbu.app.ui.components.functionbar.FunctionbarModel;
import moe.zenbu.app.ui.components.pages.PageModel;
import moe.zenbu.app.ui.components.switcher.SwitcherModel;

import org.jrebirth.af.core.concurrent.JRebirthThread;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.InnerModel;
import org.jrebirth.af.core.ui.Model;

public enum WorkbenchInnerModels implements InnerModel
{
    SWITCHER(SwitcherModel.class),
    FUNCTIONBAR(FunctionbarModel.class),
    PAGE(PageModel.class);

    private final UniqueKey<? extends Model> modelKey;

    private WorkbenchInnerModels(final Class<? extends Model> modelClass)
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

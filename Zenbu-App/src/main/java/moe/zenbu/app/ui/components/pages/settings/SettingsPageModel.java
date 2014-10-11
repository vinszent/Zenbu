package moe.zenbu.app.ui.components.pages.settings;

import org.jrebirth.af.core.ui.DefaultModel;

public class SettingsPageModel extends DefaultModel<SettingsPageModel, SettingsPageView>
{
    @Override
    protected void initModel()
    {
    }

    @Override
    protected void initInnerModels()
    {
        getInnerModel(SettingsPageInnerModels.COLLECTION);
        getInnerModel(SettingsPageInnerModels.LOOKANDFEEL);
    }
}

package moe.zenbu.app.ui.components.pages.settings;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import moe.zenbu.app.resources.I18n;

import org.jrebirth.af.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsPageView extends DefaultView<SettingsPageModel, TabPane, SettingsPageController>
{
    private static final Logger log = LoggerFactory.getLogger(SettingsPageView.class);

    private Tab collectionTab;
    private Tab lafTab;

    public SettingsPageView(final SettingsPageModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        collectionTab = new Tab("Collection");
        collectionTab.setContent(getModel().getInnerModel(SettingsPageInnerModels.COLLECTION).getRootNode());
        collectionTab.setClosable(false);

        lafTab = new Tab(I18n.getLocalisedString("settings.tab.laf"));
        lafTab.setContent(getModel().getInnerModel(SettingsPageInnerModels.LOOKANDFEEL).getRootNode());
        lafTab.setClosable(false);

        getRootNode().getTabs().addAll(collectionTab, lafTab);

        log.info("Settings page view initialised");
    }
}

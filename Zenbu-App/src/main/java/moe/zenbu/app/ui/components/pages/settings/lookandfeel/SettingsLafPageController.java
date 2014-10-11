package moe.zenbu.app.ui.components.pages.settings.lookandfeel;

import javafx.event.ActionEvent;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class SettingsLafPageController extends DefaultController<SettingsLafPageModel, SettingsLafPageView>
{
    public SettingsLafPageController(final SettingsLafPageView view) throws CoreException
    {
        super(view);
    }

    public void onActionReloadCss(final ActionEvent evt)
    {
        getModel().getLocalFacade().getGlobalFacade().getApplication().getScene().getStylesheets().remove("file:./src/main/resources/styles/zenbu.css");
        getModel().getLocalFacade().getGlobalFacade().getApplication().getScene().getStylesheets().add("file:./src/main/resources/styles/zenbu.css");
    }
}

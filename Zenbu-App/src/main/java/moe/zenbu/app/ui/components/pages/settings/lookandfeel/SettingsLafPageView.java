package moe.zenbu.app.ui.components.pages.settings.lookandfeel;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import moe.zenbu.app.resources.I18n;

import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

public class SettingsLafPageView extends DefaultView<SettingsLafPageModel, GridPane, SettingsLafPageController>
{
    @OnAction(name = "ReloadCss")
    private Button reloadCssButton;

    public SettingsLafPageView(final SettingsLafPageModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        reloadCssButton = new Button();
        reloadCssButton.setText(I18n.getLocalisedString("settings.laf.reloadcss"));

        getRootNode().addColumn(0, reloadCssButton);
    }
}

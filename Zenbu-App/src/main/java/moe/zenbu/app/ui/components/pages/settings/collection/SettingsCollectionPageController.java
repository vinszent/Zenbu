package moe.zenbu.app.ui.components.pages.settings.collection;

import javafx.event.ActionEvent;

import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.ui.components.notifications.inline.InlineNotificationWaves;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.WaveData;

public class SettingsCollectionPageController extends DefaultController<SettingsCollectionPageModel, SettingsCollectionPageView>
{
    public SettingsCollectionPageController(final SettingsCollectionPageView view) throws CoreException
    {
        super(view);
    }

    public void onActionSaveCredentials(final ActionEvent evt)
    {
        SqlSession db = DbUtils.getSqlSession();

        switch(getView().getSyncBox().getValue().toString())
        {
            case "MyAnimeList":
                getModel().getMalEnabled().setValue("true");
                getModel().getMalUsername().setValue(getView().getUsernameField().getText());
                getModel().getMalPassword().setValue(getView().getPasswordField().getText());

                db.update("db.mappers.settingmapper.updateSetting", getModel().getMalEnabled());
                db.update("db.mappers.settingmapper.updateSetting", getModel().getMalUsername());
                db.update("db.mappers.settingmapper.updateSetting", getModel().getMalPassword());
                break;
            case "Hummingbird":
                getModel().getHbEnabled().setValue("true");
                getModel().getHbUsername().setValue(getView().getUsernameField().getText());
                getModel().getHbPassword().setValue(getView().getPasswordField().getText());

                db.update("db.mappers.settingmapper.updateSetting", getModel().getHbEnabled());
                db.update("db.mappers.settingmapper.updateSetting", getModel().getHbUsername());
                db.update("db.mappers.settingmapper.updateSetting", getModel().getHbPassword());
                break;
            case "AniList":
                getModel().getAlEnabled().setValue("true");
                getModel().getAlUsername().setValue(getView().getUsernameField().getText());
                getModel().getAlPassword().setValue(getView().getPasswordField().getText());

                db.update("db.mappers.settingmapper.updateSetting", getModel().getAlEnabled());
                db.update("db.mappers.settingmapper.updateSetting", getModel().getAlUsername());
                db.update("db.mappers.settingmapper.updateSetting", getModel().getAlPassword());
                break;
        }    

        int enabled = 0;
        if(getModel().getMalEnabled().getValueAsBoolean())
        {
            enabled++;
        }
        if(getModel().getHbEnabled().getValueAsBoolean())
        {
            enabled++;
        }
        if(getModel().getAlEnabled().getValueAsBoolean())
        {
            enabled++;
        }

        if(enabled > 1)
        {
            Setting multiEnabled = db.selectOne("db.mappers.settingmapper.selectSetting", "multi_providers_enabled");
            multiEnabled.setValue("true");
            db.update("db.mappers.settingmapper.updateSetting", multiEnabled);
        }

        db.close();

        getModel().sendWave(InlineNotificationWaves.SHOW, WaveData.build(JrebirthUtils.STRING_WAVE_ITEM, "Saved credentials"));
    }        

    public void onActionProvider(final ActionEvent evt)
    {
        switch(getView().getSyncBox().getValue().toString())
        {
            case "MyAnimeList":
                if(getModel().getMalEnabled().getValueAsBoolean())
                {
                    getView().getEnableSyncButton().setSelected(true);
                    getView().getUsernameField().setText(getModel().getMalUsername().getValue());
                    getView().getPasswordField().setText(getModel().getMalPassword().getValue());
                }
                else
                {
                    getView().getEnableSyncButton().setSelected(false);
                }
                break;
            case "Hummingbird":
                if(getModel().getHbEnabled().getValueAsBoolean())
                {
                    getView().getEnableSyncButton().setSelected(true);
                    getView().getUsernameField().setText(getModel().getHbUsername().getValue());
                    getView().getPasswordField().setText(getModel().getHbPassword().getValue());
                }
                else
                {
                    getView().getEnableSyncButton().setSelected(false);
                }
                break;
            case "AniList":
                if(getModel().getAlEnabled().getValueAsBoolean())
                {
                    getView().getEnableSyncButton().setSelected(true);
                    getView().getUsernameField().setText(getModel().getAlUsername().getValue());
                    getView().getPasswordField().setText(getModel().getAlPassword().getValue());
                }
                else
                {
                    getView().getEnableSyncButton().setSelected(false);
                }
                break;
        }

        if(!getView().getEnableSyncButton().isSelected())
        {
            getView().getUsernameField().clear();
            getView().getPasswordField().clear();
        }
    }        
}

package moe.zenbu.app.ui.components.pages.settings.collection;

import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.ui.DefaultModel;

public class SettingsCollectionPageModel extends DefaultModel<SettingsCollectionPageModel, SettingsCollectionPageView>
{
    private Setting malEnabled;
    private Setting hbEnabled;
    private Setting alEnabled;

    private Setting malUsername;
    private Setting malPassword;
    private Setting hbUsername;
    private Setting hbPassword;
    private Setting alUsername;
    private Setting alPassword;

    @Override
    protected void initModel()
    {
        SqlSession db = DbUtils.getSqlSession();

        malEnabled = db.selectOne("db.mappers.settingmapper.selectSetting", "mal_enabled");
        hbEnabled = db.selectOne("db.mappers.settingmapper.selectSetting", "hb_enabled");
        alEnabled = db.selectOne("db.mappers.settingmapper.selectSetting", "al_enabled");

        malUsername = db.selectOne("db.mappers.settingmapper.selectSetting", "mal_username");
        malPassword = db.selectOne("db.mappers.settingmapper.selectSetting", "mal_password");
        hbPassword = db.selectOne("db.mappers.settingmapper.selectSetting", "hb_password");
        hbUsername = db.selectOne("db.mappers.settingmapper.selectSetting", "hb_username");
        alUsername = db.selectOne("db.mappers.settingmapper.selectSetting", "al_username");
        alPassword = db.selectOne("db.mappers.settingmapper.selectSetting", "al_password");

        db.close();
    }

    @Override
    protected void bind()
    {
    }

    public Setting getMalEnabled()
    {
        return malEnabled;
    }

    public Setting getHbEnabled()
    {
        return hbEnabled;
    }

    public Setting getAlEnabled()
    {
        return alEnabled;
    }

    public Setting getMalUsername()
    {
        return malUsername;
    }

    public Setting getMalPassword()
    {
        return malPassword;
    }

    public Setting getHbUsername()
    {
        return hbUsername;
    }

    public Setting getHbPassword()
    {
        return hbPassword;
    }

    public Setting getAlUsername()
    {
        return alUsername;
    }

    public Setting getAlPassword()
    {
        return alPassword;
    }
}

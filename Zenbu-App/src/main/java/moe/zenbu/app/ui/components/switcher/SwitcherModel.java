package moe.zenbu.app.ui.components.switcher;

import javafx.scene.control.ToggleButton;

import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.enums.Page;
import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitcherModel extends DefaultModel<SwitcherModel, SwitcherView>
{
    private static final Logger log = LoggerFactory.getLogger(SwitcherModel.class);

    private Setting switcherCompact;

    @Override
    protected void initModel()
    {
        listen(SwitcherWaves.SET_COMPACT);
    }

    public void doShowPage(final Page page, final Wave wave)
    {
        switch(page)
        {
            case COLLECTION:
                    getView().getButtonGroup().selectToggle(getView().getCollectionButton());
                    break;
            case DOWNLOAD:
                    getView().getButtonGroup().selectToggle(getView().getDownloadButton());
                    break;
            case SETTINGS:
                    getView().getButtonGroup().selectToggle(getView().getSettingsButton());
                    break;
        }
    }        
    
    public void doSetCompact(final Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        switcherCompact = db.selectOne("db.mappers.settingmapper.selectSetting", "switcher_compact");
        if(switcherCompact != null && switcherCompact.getValueAsBoolean())
        {
            getView().getButtonGroup().getToggles().forEach(t -> ((ToggleButton) t).getStyleClass().add("compact"));
            getView().getRootNode().getStyleClass().add("compact");
        }
        else
        {
            getView().getButtonGroup().getToggles().forEach(t -> ((ToggleButton) t).getStyleClass().remove("compact"));
            getView().getRootNode().getStyleClass().remove("compact");
        }

        db.close();
    }

    public Setting getSwitcherCompact()
    {
        return switcherCompact;
    }
}

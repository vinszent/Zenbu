package moe.zenbu.app.ui.components.functionbar;

import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.ui.components.switcher.SwitcherWaves;

import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;

public class FunctionbarModel extends DefaultModel<FunctionbarModel, FunctionbarView>
{
    private Setting switcherCompact;

    @Override
    protected void initModel()
    {
        listen(FunctionbarWaves.SHOW_FUNCTIONBAR);
        listen(SwitcherWaves.SET_COMPACT);

        SqlSession db = DbUtils.getSqlSession();

        switcherCompact = db.selectOne("db.mappers.settingmapper.selectSetting", "switcher_compact");

        db.close();
    }

    @Override
    protected void initInnerModels()
    {
        getInnerModel(FunctionbarInnerModels.COLLECTION);
    }
    
    public void doSetCompact(final Wave wave)
    {
        if(switcherCompact.getValueAsBoolean())
        {
            getView().getRootNode().getStyleClass().add("compact");
        }
        else
        {
            getView().getRootNode().getStyleClass().remove("compact");
        }
    }
}

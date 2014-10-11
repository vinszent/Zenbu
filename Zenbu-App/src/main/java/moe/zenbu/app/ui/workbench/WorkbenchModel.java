package moe.zenbu.app.ui.workbench;

import moe.zenbu.app.services.collection.CollectionSyncService;
import moe.zenbu.app.services.fxutils.MoveStageService;
import moe.zenbu.app.services.hooks.HooksService;
import moe.zenbu.app.services.hooks.HooksWaves;
import moe.zenbu.app.services.rmi.ZenbuRmiService;
import moe.zenbu.app.ui.components.notifications.inline.InlineNotificationModel;
import moe.zenbu.app.ui.components.pages.progress.ProgressModel;

import org.jrebirth.af.core.annotation.Component;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.JRebirthWaves;
import org.jrebirth.af.core.wave.WaveData;

public class WorkbenchModel extends DefaultModel<WorkbenchModel, WorkbenchView>
{
    // TODO: Review
    @Component
    private MoveStageService moveStageService;

    @Component
    private ZenbuRmiService zenbuRmiService;

    @Component
    private ProgressModel progressModel;
    
    @Component
    private CollectionSyncService collectionSyncService;

    @Component
    private InlineNotificationModel inlineNotificationModel;

    @Override
    protected void initModel()
    {
    }

    @Override
    protected void initInnerModels()
    {
        getInnerModel(WorkbenchInnerModels.SWITCHER);
        getInnerModel(WorkbenchInnerModels.FUNCTIONBAR);
        getInnerModel(WorkbenchInnerModels.PAGE);
    }

    @Override
    protected void showView()
    {
    }
}

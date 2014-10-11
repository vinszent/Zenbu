package moe.zenbu.app.ui.components.popovers.embedded.hamburger.sync;

import javafx.event.ActionEvent;

import moe.zenbu.app.enums.HamburgerContent;
import moe.zenbu.app.services.collection.CollectionServiceWaves;
import moe.zenbu.app.services.collection.CollectionSyncService;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.HamburgerPopupWaves;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.progress.HamburgerProgressModel;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.JRebirthWaves;
import org.jrebirth.af.core.wave.WaveData;
import org.jrebirth.af.core.wave.WaveTypeBase;

public class SyncPopupController extends DefaultController<SyncPopupModel, SyncPopupView>
{
    public SyncPopupController(final SyncPopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    public void initEventAdapters()
    {
        //linkService(getView().getSyncButton(), ActionEvent.ACTION, CollectionSyncService.class, (WaveTypeBase) CollectionServiceWaves.SYNC_LISTS, WaveData.build(JRebirthWaves.PROGRESS_BAR, getModel().getModel(ProgressHamburgerModel.class).getView().getProgressBar()), WaveData.build(JRebirthWaves.TASK_MESSAGE, getModel().getModel(ProgressHamburgerModel.class).getView().getProgressLabel().textProperty()));
    }

    @Override
    public void initEventHandlers()
    {
    }

    public void onActionSync(final ActionEvent evt)
    {
        getModel().sendWave(HamburgerPopupWaves.SWITCH_CONTENT, WaveData.build(HamburgerPopupWaves.HAMBURGER_CONTENT, HamburgerContent.PROGRESS));
        getModel().returnData(CollectionSyncService.class, (WaveTypeBase) CollectionServiceWaves.SYNC_LISTS, WaveData.build(JRebirthWaves.PROGRESS_BAR, getModel().getModel(HamburgerProgressModel.class).getView().getProgressBar()), WaveData.build(JRebirthWaves.TASK_MESSAGE, getModel().getModel(HamburgerProgressModel.class).getView().getProgressLabel().textProperty()));
    }
}

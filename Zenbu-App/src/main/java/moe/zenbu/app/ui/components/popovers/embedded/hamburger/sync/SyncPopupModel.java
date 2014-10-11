package moe.zenbu.app.ui.components.popovers.embedded.hamburger.sync;

import moe.zenbu.app.enums.HamburgerContent;
import moe.zenbu.app.services.collection.CollectionServiceWaves;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.HamburgerPopupWaves;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;

public class SyncPopupModel extends DefaultModel<SyncPopupModel, SyncPopupView>
{
    @Override
    public void initModel()
    {
        listen(CollectionServiceWaves.SYNC_LISTS_DONE);
    }

    @Override
    public void bind()
    {
    }

    public void doSyncListsDone(final Wave wave)
    {
        sendWave(HamburgerPopupWaves.SWITCH_CONTENT, WaveData.build(HamburgerPopupWaves.HAMBURGER_CONTENT, HamburgerContent.SYNC));
    }
}

package moe.zenbu.app.ui.components.switcher;

import javafx.event.ActionEvent;
import moe.zenbu.app.enums.Page;
import moe.zenbu.app.ui.components.pages.PageWaves;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.WaveData;

public class SwitcherController extends DefaultController<SwitcherModel, SwitcherView>
{
    public SwitcherController(final SwitcherView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventAdapters()
    {
        linkWave(getView().getCollectionButton(), ActionEvent.ACTION, PageWaves.SHOW_PAGE, WaveData.build(PageWaves.PAGE, Page.COLLECTION));
        linkWave(getView().getDownloadButton(), ActionEvent.ACTION, PageWaves.SHOW_PAGE, WaveData.build(PageWaves.PAGE, Page.DOWNLOAD));
        linkWave(getView().getSettingsButton(), ActionEvent.ACTION, PageWaves.SHOW_PAGE, WaveData.build(PageWaves.PAGE, Page.SETTINGS));
    }
}

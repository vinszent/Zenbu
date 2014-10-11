package moe.zenbu.app.ui.components.pages;

import javafx.scene.layout.StackPane;

import moe.zenbu.app.enums.Page;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageModel;
import moe.zenbu.app.ui.components.pages.download.DownloadPageModel;
import moe.zenbu.app.ui.components.pages.progress.ProgressModel;
import moe.zenbu.app.ui.components.pages.settings.SettingsPageModel;

import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.basic.showmodel.ShowFadingModelCommand;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.Model;
import org.jrebirth.af.core.ui.simple.DefaultSimpleModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageModel extends DefaultSimpleModel<StackPane>
{
    private static final Logger log = LoggerFactory.getLogger(PageModel.class);

    private UniqueKey<? extends Model> currentModel;

    private Page currentPage = null;

    @Override
    protected void initModel()
    {
        listen(PageWaves.SHOW_PAGE);
    }

    @Override
    protected void initSimpleView()
    {
        super.initSimpleView();

        doShowPage(Page.PROGRESS, null);
    }

    public void doShowPage(final Page page, final Wave wave)
    {
        log.debug("Showing page {}", page);

        DisplayModelWaveBean waveBean = new DisplayModelWaveBean();
        waveBean.setChidrenPlaceHolder(getRootNode().getChildren());
        waveBean.setAppendChild(false);
        //DisplayModelWaveBean waveBean = DisplayModelWaveBean.create().childrenPlaceHolder(getRootNode().getChildren()).appendChild(false);

        switch(page)
        {
            case COLLECTION:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(CollectionPageModel.class));
                break;
            case BROWSE:

                break;
            case DOWNLOAD:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(DownloadPageModel.class));
                break;
            case CHAT:

                break;
            case SETTINGS:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(SettingsPageModel.class));
                //getRootNode().getChildren().setAll(getModel(SettingsPageModel.class).getView().getRootNode());
                //return;
                break;
            case PROGRESS:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(ProgressModel.class));
                break;
        }

        if(currentPage == page)
        {
            return;
        }
        else
        {
            waveBean.setHideModelKey(this.currentModel);
            this.currentModel = waveBean.getShowModelKey();
            callCommand(ShowFadingModelCommand.class, waveBean);

            currentPage = page;
        }
    }

    @Override
    protected void showView()
    {
    }
}

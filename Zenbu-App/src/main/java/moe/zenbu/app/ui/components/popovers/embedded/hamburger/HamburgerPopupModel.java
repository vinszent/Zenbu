package moe.zenbu.app.ui.components.popovers.embedded.hamburger;

import javafx.scene.control.ToggleButton;

import moe.zenbu.app.enums.HamburgerContent;
import moe.zenbu.app.commands.transitions.ShowSlideLeftModelCommand;
import moe.zenbu.app.commands.transitions.ShowSlideRightModelCommand;
import moe.zenbu.app.ui.components.functionbar.collection.standard.StandardCollectionFunctionBarModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.addseries.HamburgerAddSeriesModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.history.HistoryPopupModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.progress.HamburgerProgressModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.sync.SyncPopupModel;

import org.jrebirth.af.core.command.DefaultMultiBeanCommand;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.basic.showmodel.ShowFadingModelCommand;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.ui.Model;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HamburgerPopupModel extends DefaultModel<HamburgerPopupModel, HamburgerPopupView>
{
    private static final Logger log = LoggerFactory.getLogger(HamburgerPopupModel.class);

    private UniqueKey<? extends Model> currentModelKey;

    private HamburgerContent currentContent = HamburgerContent.SYNC;

    @Override
    public void initModel()
    {
        listen(HamburgerPopupWaves.SHOW);
        listen(HamburgerPopupWaves.SWITCH_CONTENT);
    }

    @Override
    protected void initInnerModels()
    {
        getInnerModel(HamburgerPopupInnerModels.ADD_SERIES);
        getInnerModel(HamburgerPopupInnerModels.SYNC);
        getInnerModel(HamburgerPopupInnerModels.HISTORY);
        getInnerModel(HamburgerPopupInnerModels.PROGRESS);
    }

    @Override
    public void bind()
    {
    }

    public void doShow(final Wave wave)
    {
        if(getView().getRootNode().isVisible())
        {
            //getView().getHideAnimation().playFromStart();
            getView().getRootNode().hide();
        }
        else
        {
            doSwitchContent(HamburgerContent.ADD_SERIES, null);

            ToggleButton origin = getModel(StandardCollectionFunctionBarModel.class).getView().getHistoryButton();
            getView().getRootNode().show(origin);
            //getView().getRootNode().setVisible(true);
            //getView().getRootNode().relocate(FXUtils.getSceneX(getModel(StandardCollectionFunctionBarModel.class).getView().getHistoryButton()) - getView().getRootNode().getWidth() + getModel(StandardCollectionFunctionBarModel.class).getView().getHistoryButton().getWidth() , FXUtils.getSceneY(getModel(StandardCollectionFunctionBarModel.class).getView().getHistoryButton()) + origin.getHeight());
            //getView().getShowAnimation().playFromStart();
        }
    }

    public void doSwitchContent(final HamburgerContent content, final Wave wave)
    {
        log.info("Showing hamburger content {}", content);

        if(content == currentContent)
        {
            log.trace("Content {} is already being shown", content);

            return;
        }

        DisplayModelWaveBean waveBean = new DisplayModelWaveBean();
        waveBean.setChidrenPlaceHolder(getView().getContentPane().getChildren());
        waveBean.setAppendChild(false);
        //DisplayModelWaveBean waveBean = DisplayModelWaveBean.create().childrenPlaceHolder(getView().getContentPane().getChildren()).appendChild(false);

        Class<? extends DefaultMultiBeanCommand<DisplayModelWaveBean>> transitionCommand = null;

        switch(content)
        {
            case ADD_SERIES:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(HamburgerAddSeriesModel.class));
                if(currentContent == HamburgerContent.SYNC)
                {
                    transitionCommand = ShowSlideLeftModelCommand.class;
                }
                else
                {
                    transitionCommand = ShowSlideRightModelCommand.class;
                }
                break;
            case HISTORY:
                if(currentContent == HamburgerContent.ADD_SERIES)
                {
                    transitionCommand = ShowSlideLeftModelCommand.class;
                }
                else
                {
                    transitionCommand = ShowSlideRightModelCommand.class;
                }
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(HistoryPopupModel.class));
                break;
            case SYNC:
                if(currentContent == HamburgerContent.HISTORY)
                {
                    transitionCommand = ShowSlideLeftModelCommand.class;
                }
                else
                {
                    transitionCommand = ShowSlideRightModelCommand.class;
                }
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(SyncPopupModel.class));
                break;
            case PROGRESS:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(HamburgerProgressModel.class));
                currentContent = HamburgerContent.PROGRESS;
                break;
        }

        if(currentContent == HamburgerContent.PROGRESS)
        {
            transitionCommand = ShowFadingModelCommand.class;
        }

        waveBean.setHideModelKey(this.currentModelKey);
        this.currentModelKey = waveBean.getShowModelKey();
        callCommand(transitionCommand, waveBean);
        currentContent = content;
    }
}

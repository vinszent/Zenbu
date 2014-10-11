package moe.zenbu.app.ui.components.functionbar.collection;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import moe.zenbu.app.enums.CollectionFunctionBar;
import moe.zenbu.app.ui.components.functionbar.collection.editor.EditorCollectionFunctionBarModel;
import moe.zenbu.app.ui.components.functionbar.collection.standard.StandardCollectionFunctionBarModel;

import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.basic.showmodel.ShowFadingModelCommand;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.Model;
import org.jrebirth.af.core.ui.simple.DefaultSimpleModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionFunctionBarModel extends DefaultSimpleModel<StackPane>
{
    private static final Logger log = LoggerFactory.getLogger(CollectionFunctionBarModel.class);

    private UniqueKey<? extends Model> currentModel;

    @Override
    protected void initModel()
    {
        listen(CollectionFunctionBarWaves.SHOW_COLLECTION_FUNCTION_BAR);
    }

    @Override
    protected void initSimpleView()
    {
        super.initSimpleView();

        HBox.setHgrow(getRootNode(), Priority.ALWAYS);
        getRootNode().getStyleClass().add("collection-functionbar");

        doShowCollectionFunctionBar(CollectionFunctionBar.STANDARD, null);
    }

    public void doShowCollectionFunctionBar(final CollectionFunctionBar collectionFunctionBar, final Wave wave)
    {
        log.trace("Showing collection function bar {}", collectionFunctionBar);

        DisplayModelWaveBean waveBean = new DisplayModelWaveBean();
        waveBean.setChidrenPlaceHolder(getRootNode().getChildren());
        waveBean.setAppendChild(false);
        //DisplayModelWaveBean waveBean = DisplayModelWaveBean.create().childrenPlaceHolder(getRootNode().getChildren()).appendChild(false);

        switch(collectionFunctionBar)
        {
            case STANDARD:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(StandardCollectionFunctionBarModel.class));
                break;
            case EDITOR:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(EditorCollectionFunctionBarModel.class));
                break;
        }

        waveBean.setHideModelKey(this.currentModel);
        this.currentModel = waveBean.getShowModelKey();
        callCommand(ShowFadingModelCommand.class, waveBean);
    }

    @Override
    protected void showView()
    {
    }
}

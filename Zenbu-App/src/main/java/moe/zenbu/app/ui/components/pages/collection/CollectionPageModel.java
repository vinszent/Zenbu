package moe.zenbu.app.ui.components.pages.collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.layout.StackPane;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.enums.CollectionPage;
import moe.zenbu.app.ui.components.pages.collection.grid.CollectionGridModel;
import moe.zenbu.app.ui.components.pages.collection.list.CollectionListModel;

import moe.zenbu.app.ui.components.popups.embedded.seriesprogress.SeriesProgressPopupModel;
import moe.zenbu.app.ui.components.popups.embedded.seriesscore.SeriesScorePopupModel;
import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.annotation.Component;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.basic.showmodel.ShowModelCommand;
import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.Model;
import org.jrebirth.af.core.ui.simple.DefaultSimpleModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionPageModel extends DefaultSimpleModel<StackPane>
{
    private static final Logger log = LoggerFactory.getLogger(CollectionPageModel.class);

    private UniqueKey<? extends Model> currentModelKey;

    private ObservableList<Anime> animeList;
    private FilteredList<Anime> filteredList;
    private SortedList<Anime> sortedList;

    @Component
    private CollectionGridModel collectionGridModel;

    @Component
    private CollectionListModel collectionListModel;

    @Component
    private SeriesProgressPopupModel seriesProgressPopupModel;

    @Component
    private SeriesScorePopupModel seriesScorePopupModel;

    @Override
    protected void initModel()
    {
        // Intialise listeners
        listen(CollectionPageWaves.SWITCH_COLLECTION_PAGE);
        listen(CollectionPageWaves.REFRESH);

        doRefresh(null);
    }

    @Override
    protected void initSimpleView()
    {
        super.initSimpleView();

        doSwitchCollectionPage(CollectionPage.GRID, null);
//        getRootNode().getChildren().add(getModel(CollectionGridModel.class).getView().getRootNode());
    }

    public void doSwitchCollectionPage(final CollectionPage collectionPage, final Wave wave)
    {
        DisplayModelWaveBean waveBean = new DisplayModelWaveBean();
        waveBean.setChidrenPlaceHolder(getRootNode().getChildren());
        waveBean.setAppendChild(false);
        //DisplayModelWaveBean waveBean = DisplayModelWaveBean.create().childrenPlaceHolder(getRootNode().getChildren()).appendChild(false);

        switch(collectionPage)
        {
            case GRID:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(CollectionGridModel.class));
                break;
            case LIST:
                waveBean.setShowModelKey(getLocalFacade().getGlobalFacade().getUiFacade().buildKey(CollectionListModel.class));
                break;
        }

        waveBean.setHideModelKey(this.currentModelKey);
        this.currentModelKey = waveBean.getShowModelKey();
        callCommand(ShowModelCommand.class, waveBean);
    }

    @Override
    protected void showView()
    {
    }

    public void doRefresh(final Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        animeList = FXCollections.observableArrayList(db.selectList("db.mappers.animemapper.selectAnimeWithUserData"));
        filteredList = new FilteredList<>(animeList, a -> true);
        sortedList = new SortedList<>(filteredList);
        sortedList.setComparator((a, b) -> a.getSelectedTitle().compareTo(b.getSelectedTitle()));

        sortedList.comparatorProperty().addListener((ov, oldVal, newVal) ->
        {
            getModel(CollectionGridModel.class).getView().forceUpdateCells();

            log.debug("Grid view items are {}", getModel(CollectionGridModel.class).getView().getRootNode().getChildrenUnmodifiable());
        });

        db.close();

        sendWave(CollectionPageWaves.REFRESH_DONE);
    }

    public SortedList<Anime> getSortedList()
    {
        return sortedList;
    }

    @Override
    protected void bind()
    {

    }

    public FilteredList<Anime> getFilteredList()
    {
        return filteredList;
    }
}

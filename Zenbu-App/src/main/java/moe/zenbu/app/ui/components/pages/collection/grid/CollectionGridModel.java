package moe.zenbu.app.ui.components.pages.collection.grid;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.collections.ListChangeListener;
import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.Setting;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageModel;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageWaves;
import moe.zenbu.app.ui.components.popups.embedded.episodeinfo.EpisodeInfoPopupModel;
import moe.zenbu.app.ui.components.popups.embedded.seriesoptions.SeriesOptionsPopupModel;
import moe.zenbu.app.ui.components.popups.embedded.seriesprogress.SeriesProgressPopupWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesscore.SeriesScoreFloatingWaves;
import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.controlsfx.control.GridView;
import org.jrebirth.af.core.annotation.Component;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class CollectionGridModel extends DefaultModel<CollectionGridModel, CollectionGridView>
{
    private static final Logger log = LoggerFactory.getLogger(CollectionGridModel.class);

    private SeriesOptionsPopupModel seriesOptionsPopupModel;

    @Component
    private EpisodeInfoPopupModel episodeInfoPopOverModel;

    // Settings
    private Setting gridCellWidth;
    private Setting gridCellHeight;
    private Setting coverImageHeight;

    @Override
    protected void initModel()
    {
        listen(CollectionPageWaves.REFRESH_DONE); // TODO: Only listen to this when active

        listen(SeriesProgressPopupWaves.PROGRESS_UPDATED);
        listen(SeriesScoreFloatingWaves.SCORE_UPDATED);

        // Get settings

        SqlSession db = DbUtils.getSqlSession();

        gridCellWidth = db.selectOne("db.mappers.settingmapper.selectSetting", "grid_cell_width");
        gridCellHeight = db.selectOne("db.mappers.settingmapper.selectSetting", "grid_cell_height");
        coverImageHeight = db.selectOne("db.mappers.settingmapper.selectSetting", "cover_image_height");

        db.close();
    }

    @Override
    protected void bind()
    {
        getView().getRootNode().setCellWidth(Double.parseDouble(gridCellWidth.getValue()));
        getView().getRootNode().setCellHeight(Double.parseDouble(gridCellHeight.getValue()));

        //getView().getRootNode().setItems(getModel(CollectionPageModel.class).getSortedList());
        //getModel(CollectionPageModel.class).getSortedList().addListener(new ListChangeListener<Anime>()
        getView().getRootNode().getItems().addListener(new ListChangeListener<Anime>()
        {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Anime> c)
            {
                log.trace("Grid view items changed");
                refreshCells();
            }
        });
    }

    public void doRefreshDone(final Wave wave)
    {
        log.debug("Refreshing grid items");

        getView().getRootNode().setItems(getModel(CollectionPageModel.class).getSortedList());
        refreshCells();
    }

    public void doProgressUpdated(final Anime anime, final Wave wave)
    {
        getView().forceUpdateCells();
    }

    public void doScoreUpdated(final Anime anime, final Wave wave)
    {
        getView().forceUpdateCells();
    }

    public Setting getGridCellWidth()
    {
        return gridCellWidth;
    }

    public Setting getGridCellHeight()
    {
        return gridCellHeight;
    }

    public Setting getCoverImageHeight()
    {
        return coverImageHeight;
    }

    private void refreshCells()
    {
        GridView<Anime> root = getView().getRootNode();
        if(root.getSkin() != null)
        {
            Class clazz = root.getSkin().getClass().getSuperclass();

            try
            {
                Field f = clazz.getDeclaredField("flow");
                f.setAccessible(true);

                VirtualFlow flow = (VirtualFlow) f.get(root.getSkin());
                flow.recreateCells(); // FIXME: Wait for this to be patched
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

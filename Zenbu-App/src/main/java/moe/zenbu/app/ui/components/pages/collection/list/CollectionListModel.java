package moe.zenbu.app.ui.components.pages.collection.list;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageModel;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesprogress.SeriesProgressPopupWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesscore.SeriesScoreFloatingWaves;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionListModel extends DefaultModel<CollectionListModel, CollectionListView>
{
    private static final Logger log = LoggerFactory.getLogger(CollectionListModel.class);

    @Override
    protected void initModel()
    {
        listen(CollectionPageWaves.REFRESH_DONE);

        listen(SeriesProgressPopupWaves.PROGRESS_UPDATED);
        listen(SeriesScoreFloatingWaves.SCORE_UPDATED);
    }

    @Override
    protected void bind()
    {
    }

    public void doRefreshDone(final Wave wave)
    {
        log.debug("Refreshing list items");

        getView().getRootNode().setItems(getModel(CollectionPageModel.class).getSortedList());
    }

    public void doProgressUpdated(final Anime anime, final Wave wave)
    {
        getView().forceRefresh();
    }

    public void doScoreUpdated(final Anime anime, final Wave wave)
    {
        getView().forceRefresh();
    }
}

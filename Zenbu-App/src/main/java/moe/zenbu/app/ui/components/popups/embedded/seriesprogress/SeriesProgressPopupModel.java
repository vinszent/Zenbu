package moe.zenbu.app.ui.components.popups.embedded.seriesprogress;

import javafx.scene.layout.Region;
import moe.zenbu.app.beans.Anime;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeriesProgressPopupModel extends DefaultModel<SeriesProgressPopupModel, SeriesProgressPopupView>
{
    private static final Logger log = LoggerFactory.getLogger(SeriesProgressPopupModel.class);

    private Anime anime;

    @Override
    public void initModel()
    {
        listen(SeriesProgressPopupWaves.SHOW_PROGRESS_POPUP);
    }

    @Override
    public void bind()
    {
    }

    public void doShowProgressPopup(final Anime anime, final Region node, final Wave wave)
    {
        log.debug("Showing progress popup for anime {}", anime.getSelectedTitle());

        this.anime = anime;

        getView().getProgressField().setText(String.valueOf(anime.getUserData().getProgress()));
        getView().getTotalUnitsField().setText(String.valueOf(anime.getTotalUnits()));

        getView().getRootNode().show(node);
    }

    public Anime getAnime()
    {
        return anime;
    }
}

package moe.zenbu.app.ui.components.popups.embedded.seriesscore;

import javafx.scene.layout.Region;
import moe.zenbu.app.beans.Anime;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeriesScorePopupModel extends DefaultModel<SeriesScorePopupModel, SeriesScorePopupView>
{
    private static final Logger log = LoggerFactory.getLogger(SeriesScorePopupModel.class);

    private Anime currentAnime;

    @Override
    public void initModel()
    {
        listen(SeriesScoreFloatingWaves.SHOW_SCORE_POPUP);
    }

    @Override
    public void bind()
    {
    }

    public void doShowScorePopup(final Anime anime, final Region node, final Wave wave)
    {
        log.debug("Showing score popup for anime {}", anime.getSelectedTitle());

        currentAnime = anime;

        getView().getHalfIncrementalRating().setRating(anime.getUserData().getScore());

        getView().getRootNode().show(node);
    }

    public Anime getCurrentAnime()
    {
        return currentAnime;
    }
}

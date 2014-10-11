package moe.zenbu.app.ui.components.popups.embedded.seriesscore;

import javafx.scene.input.MouseEvent;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.commands.collection.SetScoreCommand;
import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.WaveData;

public class SeriesScorePopupController extends DefaultController<SeriesScorePopupModel, SeriesScorePopupView>
{
    public SeriesScorePopupController(final SeriesScorePopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    public void initEventAdapters()
    {
    }

    @Override
    public void initEventHandlers()
    {
    }

    public void onMouseClickedSave(final MouseEvent evt)
    {
        int score = getView().getHalfIncrementalRating().getRatingProperty().get();

        Anime anime = getModel().getCurrentAnime();

        getModel().callCommand(SetScoreCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, anime), WaveData.build(JrebirthUtils.DOUBLE_WAVE_ITEM, (double) score));

        getView().getRootNode().hide();
    }
}

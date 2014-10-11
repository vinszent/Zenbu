package moe.zenbu.app.ui.components.popups.embedded.seriesprogress;

import javafx.scene.input.MouseEvent;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.commands.collection.SetProgressCommand;
import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.WaveData;

public class SeriesProgressPopupController extends DefaultController<SeriesProgressPopupModel, SeriesProgressPopupView>
{
    public SeriesProgressPopupController(final SeriesProgressPopupView view) throws CoreException
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
        int progress = Integer.parseInt(getView().getProgressField().getText());
        int totalUnits = Integer.parseInt(getView().getTotalUnitsField().getText());

        Anime anime = getModel().getAnime();
        anime.setTotalUnits(totalUnits);

        getModel().callCommand(SetProgressCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, anime), WaveData.build(JrebirthUtils.INTEGER_WAVE_ITEM, progress));

        getView().getRootNode().hide();
    }
}

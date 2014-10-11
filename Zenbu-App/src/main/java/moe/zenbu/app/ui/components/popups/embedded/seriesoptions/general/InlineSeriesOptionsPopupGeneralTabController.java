package moe.zenbu.app.ui.components.popups.embedded.seriesoptions.general;

import moe.zenbu.app.commands.collection.DeleteSeriesCommand;
import moe.zenbu.app.ui.components.popups.embedded.seriesoptions.SeriesOptionsPopupModel;
import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.WaveData;

public class InlineSeriesOptionsPopupGeneralTabController extends DefaultController<InlineSeriesOptionsPopupGeneralTabModel, InlineSeriesOptionsPopupGeneralTabView>
{
    public InlineSeriesOptionsPopupGeneralTabController(final InlineSeriesOptionsPopupGeneralTabView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventAdapters() throws CoreException
    {
    }

    @Override
    protected void initEventHandlers() throws CoreException
    {
        getView().getDeleteButton().setOnAction(evt -> getModel().callCommand(DeleteSeriesCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, getModel().getModel(SeriesOptionsPopupModel.class).getCurrentAnime())));
    }
}
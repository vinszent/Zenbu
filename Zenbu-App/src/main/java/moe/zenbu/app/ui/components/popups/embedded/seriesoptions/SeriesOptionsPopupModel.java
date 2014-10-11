package moe.zenbu.app.ui.components.popups.embedded.seriesoptions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.util.ZenbuUtils;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeriesOptionsPopupModel extends DefaultModel<SeriesOptionsPopupModel, SeriesOptionsPopupView>
{
    private static final Logger log = LoggerFactory.getLogger(SeriesOptionsPopupModel.class);

    private ObjectProperty<String[]> statusProperty = new SimpleObjectProperty<>();
    private Anime currentAnime;

    @Override
    protected void initModel()
    {
        listen(SeriesOptionsPopupWaves.SHOW);
    }

    @Override
    protected void initInnerModels()
    {
        getInnerModel(SeriesOptionsPopupInnerModels.GENERAL);
        //getInnerModel(SeriesSettingsPopupInnerModels.DOWNLOADS);

        listen(SeriesOptionsPopupWaves.HIDE_SERIES_OPTIONS);
    }

    @Override
    protected void bind()
    {
        super.bind();
    }

    public void doShow(final Anime anime, final Wave wave)
    {
        log.trace("Showing series options popup for anime {}", anime.getSelectedTitle());

        getView().getRootNode().show();

        currentAnime = anime;

        statusProperty.set(ZenbuUtils.getStatusList().parallelStream().filter(a -> a[1].equals(anime.getUserData().getStatus())).findFirst().get());
    }

    public ObjectProperty<String[]> getStatusProperty()
    {
        return statusProperty;
    }

    public Anime getCurrentAnime()
    {
        return currentAnime;
    }

    public void doHideSeriesOptions(final Wave wave)
    {
        getView().getRootNode().hide();
    }
}

package moe.zenbu.app.ui.components.popovers.embedded.hamburger.addseries;

import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.beans.Title;
import moe.zenbu.app.commands.recognition.RecognitionCommandWaves;
import moe.zenbu.app.enums.HamburgerContent;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.HamburgerPopupWaves;
import moe.zenbu.app.util.ConcurrentUtils;
import moe.zenbu.app.util.DbUtils;
import moe.zenbu.app.util.JrebirthUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HamburgerAddSeriesModel extends DefaultModel<HamburgerAddSeriesModel, HamburgerAddSeriesView>
{
    private final static Logger log = LoggerFactory.getLogger(HamburgerAddSeriesModel.class);

    protected void initModel()
    {
    }

    @Override
    protected void bind()
    {
        loadTitles();

        getView().getAddButton().disableProperty().bind(getView().getTitleField().getSearchBox().valueProperty().isNull());
    }

    private void loadTitles()
    {
        SqlSession db = DbUtils.getSqlSession();

        List<Title> titles = db.selectList("db.mappers.titlemapper.selectAllSelectedTitle");

        getView().getTitleField().loadTitles(titles);

        db.close();

        log.info("Loaded titles");
    }

    public void doBatchAddDone(final Wave wave)
    {
        sendWave(HamburgerPopupWaves.SWITCH_CONTENT, JrebirthUtils.buildWaveData(HamburgerContent.ADD_SERIES));
    }

    public void doBatchMatchFileDone(final List<File> files, final List<Episode> episodes, final Wave wave)
    {
        unlisten(RecognitionCommandWaves.BATCH_MATCH_FILE_DONE);

        episodes.forEach(e ->
        {
            AnimeUserData userData = getView().getSeriesAccordion().getPanes().stream().map(tp -> ((AnimeUserData) tp.getUserData())).filter(ud-> ud.getId() == e.getId()).findFirst().orElse(null);
            if(userData == null)
            {
                userData = new AnimeUserData();
                userData.setId(e.getId());
                userData.setProgress(0);
                userData.setScore(0);
                userData.setStatus(AnimeUserData.STATUS_CURRENT);
                userData.setEpisodes(new ArrayList<>(Arrays.asList(e)));

                getView().add(userData);
            }
            else
            {
                userData.getEpisodes().add(e);
            }
        });

        ConcurrentUtils.sleepSilentlyMillis(500);

        getView().refreshEpisodes();

        sendWave(HamburgerPopupWaves.SWITCH_CONTENT, JrebirthUtils.buildWaveData(HamburgerContent.ADD_SERIES));
    }
}

package moe.zenbu.app.ui.components.popovers.embedded.hamburger.addseries;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Title;
import moe.zenbu.app.commands.collection.AddSeriesBatchCommand;
import moe.zenbu.app.commands.collection.CollectionCommandWaves;
import moe.zenbu.app.commands.recognition.BatchMatchFileCommand;
import moe.zenbu.app.commands.recognition.RecognitionCommandWaves;
import moe.zenbu.app.enums.HamburgerContent;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.HamburgerPopupWaves;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.progress.HamburgerProgressModel;
import moe.zenbu.app.util.JrebirthUtils;
import org.apache.commons.io.FileUtils;
import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.ui.adapter.DefaultDragAdapter;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HamburgerAddSeriesController extends DefaultController<HamburgerAddSeriesModel, HamburgerAddSeriesView>
{
    private static final Logger log = LoggerFactory.getLogger(HamburgerAddSeriesController.class);

    public HamburgerAddSeriesController(HamburgerAddSeriesView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventAdapters() throws CoreException
    {
        getView().getSeriesScrollPane().setOnDragDetected(evt -> getView().getSeriesScrollPane().startDragAndDrop(TransferMode.ANY));
        getView().getSeriesScrollPane().setOnDragOver(evt ->
        {
            if(evt.getDragboard().hasFiles())
            {
                evt.acceptTransferModes(TransferMode.ANY);
            }
            else
            {
                evt.consume();
            }
        });
        getView().getSeriesScrollPane().setOnDragDropped(evt ->
        {
            getModel().sendWave(HamburgerPopupWaves.SWITCH_CONTENT, WaveData.build(HamburgerPopupWaves.HAMBURGER_CONTENT, HamburgerContent.PROGRESS));
            List<File> files = new ArrayList<>();
            evt.getDragboard().getFiles().stream().forEach(f ->
            {
               if(f.isDirectory())
               {
                   files.addAll(FileUtils.listFiles(f, new String[]{"mkv", "mp4", "avi"}, true));
               }
               else
               {
                   files.add(f);
               }
            });

            getModel().callCommand(BatchMatchFileCommand.class, WaveData.build(JrebirthUtils.FILE_LIST_WAVE_ITEM, files));
            getModel().listen(RecognitionCommandWaves.BATCH_MATCH_FILE_DONE);
        });
    }

    @Override
    protected void initEventHandlers() throws CoreException
    {
        super.initEventHandlers();
    }

    private void onActionAdd(final ActionEvent evt)
    {
        Title title = getView().getTitleField().getSelectedTitle();
        Anime a = title.getAnime();
        AnimeUserData userData = new AnimeUserData();
        userData.setId(a.getId());
        userData.setAnime(a);
        userData.setProgress(0);
        userData.setScore(0);
        userData.setStatus(AnimeUserData.STATUS_CURRENT);

        getView().add(userData);

        getView().getTitleField().getSearchBox().setValue(null);
    }

    private void onActionDone(final ActionEvent evt)
    {
        getModel().listen(CollectionCommandWaves.BATCH_ADD_DONE);
        getModel().sendWave(HamburgerPopupWaves.SWITCH_CONTENT, JrebirthUtils.buildWaveData(HamburgerContent.PROGRESS));
        getModel().callCommand(AddSeriesBatchCommand.class, WaveData.build(JrebirthUtils.ANIME_USER_DATA_LIST_WAVE_ITEM, getView().getAnime()), WaveData.build(JrebirthUtils.STRING_PROPERTY_WAVE_ITEM, getModel().getModel(HamburgerProgressModel.class).getView().getProgressLabel().textProperty()), WaveData.build(JrebirthUtils.DOUBLE_PROPERTY_WAVE_ITEM, getModel().getModel(HamburgerProgressModel.class).getView().getProgressBar().progressProperty()));

        getView().getSeriesAccordion().getPanes().clear();
    }

    private void onActionClear(final ActionEvent evt)
    {
        getView().getSeriesAccordion().getPanes().clear();
    }
}

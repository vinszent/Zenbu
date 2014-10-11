package moe.zenbu.app.ui.components.popups.embedded.episodeinfo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.EmbeddedPopup;
import moe.zenbu.app.ui.controls.ResizableWriteableImageView;

import moe.zenbu.app.ui.workbench.WorkbenchModel;
import org.jrebirth.af.core.ui.DefaultView;

public class EpisodeInfoPopupView extends DefaultView<EpisodeInfoPopupModel, EmbeddedPopup, EpisodeInfoPopupController>
{
    private FXMLLoader fxmlLoader;

    @FXML
    private VBox episodeBox;

    @FXML
    private Label subgroupLabel;

    @FXML
    private Label videoFlagLabel;

    @FXML
    private Label audioFlagLabel;

    @FXML
    private ProgressBar playerPositionBar;

    @FXML
    private VBox canvasBox;

    @FXML
    private ResizableWriteableImageView canvas;

    public EpisodeInfoPopupView(final EpisodeInfoPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popups/EpisodeInfoPopup.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        playerPositionBar.progressProperty().bind(getModel().getPlayerPositionProperty());
        playerPositionBar.prefWidthProperty().bind(canvas.fitWidthProperty());

        super.getRootNode().setContent(episodeBox);
        super.getRootNode().setCloseOnMouseExit(false);
        Platform.runLater(() -> getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane()));
    }

    public VBox getEpisodeBox()
    {
        return episodeBox;
    }

    public Label getSubgroupLabel()
    {
        return subgroupLabel;
    }

    public Label getVideoFlagLabel()
    {
        return videoFlagLabel;
    }

    public Label getAudioFlagLabel()
    {
        return audioFlagLabel;
    }

    public ResizableWriteableImageView getCanvas()
    {
        return canvas;
    }
}

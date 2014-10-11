package moe.zenbu.app.ui.components.popups.embedded.seriesscore;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.EmbeddedPopup;
import moe.zenbu.app.ui.controls.halfincrementalrating.HalfIncrementalRating;

import moe.zenbu.app.ui.workbench.WorkbenchModel;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnMouse;

public class SeriesScorePopupView extends DefaultView<SeriesScorePopupModel, EmbeddedPopup, SeriesScorePopupController>
{
    private FXMLLoader fxmlLoader;

    @FXML
    private Label scoreLabel;

    @FXML
    private HalfIncrementalRating halfIncrementalRating;

    @FXML
    @OnMouse(value = OnMouse.MouseType.Clicked, name = "Save")
    private HBox scoreBox;

    public SeriesScorePopupView(final SeriesScorePopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popups/ScorePopup.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        scoreLabel.textProperty().bind(halfIncrementalRating.getRatingProperty().asString());

        getRootNode().setContent(scoreBox);
        getRootNode().setCloseOnMouseExit(true);
        Platform.runLater(() -> getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane()));
    }

    public HalfIncrementalRating getHalfIncrementalRating()
    {
        return halfIncrementalRating;
    }

    public HBox getScoreBox()
    {
        return scoreBox;
    }
}

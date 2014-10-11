package moe.zenbu.app.ui.components.popups.embedded.seriesprogress;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import moe.zenbu.app.resources.I18n;

import moe.zenbu.app.ui.controls.EmbeddedPopup;
import moe.zenbu.app.ui.workbench.WorkbenchModel;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnMouse;

public class SeriesProgressPopupView extends DefaultView<SeriesProgressPopupModel, EmbeddedPopup, SeriesProgressPopupController>
{
    private FXMLLoader fxmlLoader;

    @FXML
    @OnMouse(value = OnMouse.MouseType.Clicked, name = "Save")
    private HBox progressBox;

    @FXML
    private TextField progressField;

    @FXML
    private TextField totalUnitsField;

    public SeriesProgressPopupView(final SeriesProgressPopupModel model)
    {
        super(model);
    }

    @Override
    public void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popups/ProgressPopup.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        getRootNode().setContent(progressBox);
        getRootNode().setCloseOnMouseExit(true);
        Platform.runLater(() -> getRootNode().setPane(getModel().getModel(WorkbenchModel.class).getView().getAbsolutePane()));
    }

    public HBox getProgressBox()
    {
        return progressBox;
    }

    public TextField getProgressField()
    {
        return progressField;
    }

    public TextField getTotalUnitsField()
    {
        return totalUnitsField;
    }
}

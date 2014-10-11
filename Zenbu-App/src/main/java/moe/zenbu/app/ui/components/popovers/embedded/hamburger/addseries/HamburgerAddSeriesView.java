package moe.zenbu.app.ui.components.popovers.embedded.hamburger.addseries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.autocomplete.TitleAutoCompleteField;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.ui.annotation.OnAction;

import javax.swing.text.html.*;
import java.util.List;
import java.util.stream.Collectors;

public class HamburgerAddSeriesView extends DefaultView<HamburgerAddSeriesModel, StackPane, HamburgerAddSeriesController>
{
    private static FXMLLoader fxmlLoader;

    @FXML
    private TitleAutoCompleteField titleField;

    @FXML
    private Accordion seriesAccordion;

    @FXML
    private ScrollPane seriesScrollPane;

    @OnAction(name = "clear")
    @FXML
    private Button clearButton;

    @OnAction(name = "Done")
    @FXML
    private Button doneButton;

    @OnAction(name = "Add")
    @FXML
    private Button addButton;

    @FXML
    private HBox bottomBox;

    @FXML
    private HBox topBox;

    @FXML
    private VBox addSeriesBox;

    public HamburgerAddSeriesView(HamburgerAddSeriesModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popovers/hamburger/AddSeries.fxml"), I18n.getBundle());
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        seriesScrollPane.setFitToHeight(true);
        seriesScrollPane.maxHeightProperty().bind(super.getRootNode().heightProperty().subtract(topBox.heightProperty()).subtract(bottomBox.heightProperty()));

        super.getRootNode().getChildren().add(addSeriesBox);
    }

    public Accordion getSeriesAccordion()
    {
        return seriesAccordion;
    }

    public TitleAutoCompleteField getTitleField()
    {
        return titleField;
    }

    public Button getAddButton()
    {
        return addButton;
    }

    public ScrollPane getSeriesScrollPane()
    {
        return seriesScrollPane;
    }

    protected void add(final AnimeUserData userData)
    {
        TitledPane t = new TitledPane();
        t.setText(userData.getAnime().getSelectedTitle());
        t.setUserData(userData);

        t.maxWidthProperty().bind(super.getRootNode().widthProperty().subtract(2));
        t.prefWidthProperty().bind(super.getRootNode().widthProperty().subtract(2));
        t.minWidthProperty().bind(super.getRootNode().widthProperty().subtract(2));

        ListView<Episode> episodeListView = new ListView();
        episodeListView.setCellFactory(c ->
        {
            return new ListCell<Episode>()
            {
                @Override
                protected void updateItem(Episode item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if(empty)
                    {
                        setText(null);
                    }
                    else
                    {
                        setText(String.valueOf(item.getEpisode()));
                    }
                }
            };
        });

        t.setContent(episodeListView);

        seriesAccordion.getPanes().add(t);
    }

    protected void refreshEpisodes()
    {
        seriesAccordion.getPanes().forEach(t ->
        {
            ListView<Episode> lv = ((ListView<Episode>) t.getContent());
            AnimeUserData ud = ((AnimeUserData) t.getUserData());

            lv.getItems().setAll(ud.getEpisodes());
        });
    }

    protected List<AnimeUserData> getAnime()
    {
        return seriesAccordion.getPanes().stream().map(tp -> ((AnimeUserData) tp.getUserData())).collect(Collectors.toList());
    }
}

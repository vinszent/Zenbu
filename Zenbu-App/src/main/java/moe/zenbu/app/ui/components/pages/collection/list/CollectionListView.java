package moe.zenbu.app.ui.components.pages.collection.list;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.ui.components.popups.embedded.seriesprogress.SeriesProgressPopupWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesscore.SeriesScoreFloatingWaves;
import moe.zenbu.app.util.JrebirthUtils;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.scene.control.skin.ListViewSkin;

public class CollectionListView extends DefaultView<CollectionListModel, ListView, CollectionListController>
{
    private static final Logger log = LoggerFactory.getLogger(CollectionListView.class);

    public CollectionListView(final CollectionListModel model) throws CoreException
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        getRootNode().setCellFactory(listView ->
        {
            return new CollectionListCell();
        });
        getRootNode().getStyleClass().add("collection-list-view");
    }

    class CollectionListCell extends ListCell<Anime>
    {
        private Anime anime;

        private FXMLLoader fxmlLoader;

        @FXML
        private HBox listCellBox;

        @FXML
        private Label typeLabel;

        @FXML
        private Rectangle typeRectangle;

        @FXML
        private Label titleLabel;

        @FXML
        private StackPane progressPane;

        @FXML
        private HBox progressButtonsBox;

        @FXML
        private Label progressLabel;

        @FXML
        private ProgressBar progressBar;

        @FXML
        private HBox scoreBox;

        @FXML
        private Label scoreLabel;

        @FXML
        private Button extrasButton;

        public CollectionListCell()
        {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/pages/collection/CollectionListCell.fxml"));
            fxmlLoader.setController(this);
            try
            {
                fxmlLoader.load();
            }
            catch(Exception e)
            {
                log.error("Could not load collection list cell fxml", e);
            }

            scoreBox.setOnMouseClicked(evt ->
            {
                if(evt.getClickCount() > 1)
                {
                    CollectionListView.this.getModel().sendWave(SeriesScoreFloatingWaves.SHOW_SCORE_POPUP, JrebirthUtils.buildWaveData(anime, scoreBox));
                }
            });
            progressButtonsBox.setOnMouseClicked(evt ->
            {
                if(evt.getClickCount() > 1)
                {
                    CollectionListView.this.getModel().sendWave(SeriesProgressPopupWaves.SHOW_PROGRESS_POPUP, JrebirthUtils.buildWaveData(anime, progressPane));
                }
            });
        }

        @Override
        public void updateItem(final Anime item, final boolean empty)
        {
            super.updateItem(item, empty);

            anime = item;

            if(empty)
            {
                super.setGraphic(null);
                super.setText(null);
            }
            else
            {
                typeLabel.setText(item.getType());

                switch(item.getType())
                {
                    case "TV":
                        typeRectangle.setId("tv");
                        break;
                    case "Movie":
                        typeRectangle.setId("movie");
                        break;
                    case "Special":
                        typeRectangle.setId("special");
                        break;
                    case "OVA":
                        typeRectangle.setId("ova");
                        break;
                    case "ONA":
                        typeRectangle.setId("ona");
                        break;
                    default:
                        typeRectangle.setId("default");
                        break;
                }

                titleLabel.setText(item.getSelectedTitle());

                progressLabel.setText(item.getUserData().getProgress() + "/" + item.getTotalUnits());

                progressBar.setProgress((double) item.getUserData().getProgress() / (double) item.getTotalUnits());

                scoreLabel.setText(String.valueOf(item.getUserData().getScore()));

                super.setGraphic(listCellBox);
            }
        }
    }

    public void forceRefresh()
    {
        ListViewSkin skin = (ListViewSkin) getRootNode().getSkin();
        skin.updateListViewItems();
    }
}

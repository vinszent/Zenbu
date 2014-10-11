package moe.zenbu.app.ui.components.pages.collection.grid;

import impl.org.controlsfx.skin.GridViewSkin;

import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import javafx.util.Duration;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.commands.collection.DeleteEpisodeCommand;
import moe.zenbu.app.commands.collection.PlayEpisodeCommand;
import moe.zenbu.app.commands.collection.SetProgressCommand;
import moe.zenbu.app.ui.components.functionbar.collection.editor.EditorCollectionFunctionBarModel;
import moe.zenbu.app.ui.components.popovers.collection.CollectionInfoPopOver;
import moe.zenbu.app.ui.components.popups.embedded.seriesprogress.SeriesProgressPopupWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesscore.SeriesScoreFloatingWaves;
import moe.zenbu.app.ui.components.popups.embedded.episodeinfo.EpisodeInfoPopupWaves;
import moe.zenbu.app.ui.components.popups.embedded.seriesoptions.SeriesOptionsPopupWaves;
import moe.zenbu.app.util.CancelableTimer;
import moe.zenbu.app.util.FXUtils;
import moe.zenbu.app.util.JrebirthUtils;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultView;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.enzo.flippanel.FlipPanel;

public class CollectionGridView extends DefaultView<CollectionGridModel, GridView<Anime>, CollectionGridController>
{
    private static final Logger log = LoggerFactory.getLogger(CollectionGridView.class);

    private EventHandler<MouseEvent> currentHandler = evt -> {};

    public CollectionGridView(final CollectionGridModel model) throws CoreException
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        getRootNode().setCellFactory(gridView ->
        {
            return new CollectionGridCell();
        });

        getRootNode().getStyleClass().add("collection-grid");

        getRootNode().horizontalCellSpacingProperty().bind(getModel().getModel(EditorCollectionFunctionBarModel.class).getView().getHSpacingSlider().valueProperty());
        getRootNode().verticalCellSpacingProperty().bind(getModel().getModel(EditorCollectionFunctionBarModel.class).getView().getVSpacingSlider().valueProperty());

        getRootNode().getStyleClass().remove("grid-view");
    }

    public EventHandler<MouseEvent> getCurrentHandler()
    {
        return currentHandler;
    }

    class CollectionGridCell extends GridCell<Anime>
    {
        // TODO: Change to FXML
        // Class variables
        private Anime anime;

        // Root items
        private FlipPanel flipper;
        private VBox root;

        // Front pane items
        private StackPane frontPane;
        private ImageView coverImageView;
        private Rectangle coverClipRectangle;
        private Rectangle shadingRectangle;
        private BorderPane toolsPane;

        private Button playButton;
        private StackPane progressPane;
        private HBox progressButtonsBox;
        private ProgressBar progressBar;
        private Label progressLabel;
        private Button increaseProgressButton;
        private Button decreaseProgressButton;

        private HBox toolsBox;
        private Region toolsSpacer;
        private Button optionsButton;
        private Button chatButton;
        private HBox scoreBox;
        private SVGPath scoreGraphic;
        private Label scoreLabel;

        // Back pane items
        private StackPane backPane;
        private ListView<Episode> episodeList;
        private SVGPath flipGraphic;
        private Button flipFrontButton;

        // Title container items
        private VBox titleBox;
        private Label titleLabel;
        private Label totalUnitsLabel;

        // PopOver items
        private CollectionInfoPopOver popOver;

        private ObjectProperty<Skin<?>> episodeListSkinProperty;

        private CancelableTimer timer;

        private EventHandler<MouseEvent> mouseExitedPopupHandler;

        public CollectionGridCell()
        {
            /*
             * View
             */
            double gridCellWidth = Double.parseDouble(getModel().getGridCellWidth().getValue());
            double gridCellHeight = Double.parseDouble(getModel().getGridCellHeight().getValue());
            double coverImageHeight = Double.parseDouble(getModel().getCoverImageHeight().getValue());

            // Setup front pane items
            coverClipRectangle = new Rectangle(gridCellWidth, coverImageHeight);
            coverClipRectangle.getStyleClass().add("cover-clip-rectangle");
            coverClipRectangle.setArcHeight(14);
            coverClipRectangle.setArcWidth(14);

            coverImageView = new ImageView();
            coverImageView.setClip(coverClipRectangle);
            coverImageView.setFitWidth(gridCellWidth);
            coverImageView.setFitHeight(coverImageHeight);

            shadingRectangle = new Rectangle(gridCellWidth, coverImageHeight);
            shadingRectangle.getStyleClass().add("shading-rectangle");
            shadingRectangle.setVisible(false);

            // --- Setup tools pane items
            playButton = new Button();
            playButton.getStyleClass().add("play-button");

            decreaseProgressButton = new Button("-");
            decreaseProgressButton.getStyleClass().add("progress-button");
            decreaseProgressButton.setVisible(false);

            increaseProgressButton = new Button("+");
            increaseProgressButton.getStyleClass().add("progress-button");
            increaseProgressButton.setVisible(false);

            progressLabel = new Label();
            progressLabel.getStyleClass().add("progress-label");

            progressButtonsBox = new HBox();
            progressButtonsBox.getStyleClass().add("progress-buttons-box");
            progressButtonsBox.getChildren().addAll(decreaseProgressButton, progressLabel, increaseProgressButton);

            progressBar = new ProgressBar();

            progressPane = new StackPane();
            progressPane.getStyleClass().add("progress-pane");
            progressPane.getChildren().addAll(progressBar, progressButtonsBox);

            SVGPath scoreGraphic = new SVGPath();
            scoreGraphic.getStyleClass().add("score-graphic");
            scoreGraphic.setContent("m -37.4,-88 1.79085,5.0418 4.80915,0 -3.9237,2.96085 1.40265,5.19735 -4.07895,-3.1161 -4.07865,3.1161 1.40235,-5.19735 -3.9237,-2.96085 4.80915,0 L -37.4,-88 z");

            scoreLabel = new Label();
            scoreLabel.getStyleClass().add("score-label");

            scoreBox = new HBox();
            scoreBox.getChildren().addAll(scoreGraphic, scoreLabel);
            scoreBox.getStyleClass().add("score-box");

            optionsButton = new Button();
            optionsButton.setId("options");

            chatButton = new Button();
            chatButton.setId("chat");

            toolsSpacer = new Region();
            HBox.setHgrow(toolsSpacer, Priority.ALWAYS);

            toolsBox = new HBox();
            toolsBox.getStyleClass().add("tools-box");
            toolsBox.getChildren().addAll(scoreBox, toolsSpacer, chatButton, optionsButton);

            toolsPane = new BorderPane();
            toolsPane.getStyleClass().add("tools-pane");
            toolsPane.setVisible(false);
            toolsPane.setTop(toolsBox);
            toolsPane.setCenter(playButton);
            toolsPane.setBottom(progressPane);

            frontPane = new StackPane();
            frontPane.getStyleClass().add("front-pane");
            frontPane.getChildren().addAll(coverImageView, shadingRectangle, toolsPane);

            // Setup back pane items
            episodeList = new ListView<>();
            episodeList.setCellFactory(new Callback<ListView<Episode>, ListCell<Episode>>()
            {
                @Override
                public ListCell<Episode> call(ListView<Episode> listView)
                {
                    return new EpisodeListCell();
                }
            });

            flipGraphic = new SVGPath();
            flipGraphic.getStyleClass().add("flip-graphic");
            flipGraphic.setContent("m -37.567647,-27.09125 -4.806397,0 7.379559,-7.96875 7.119485,7.96875 -4.264412,0 c 0,9.678309 -10.735588,11.884191 -10.735588,11.884191 4.841912,-2.739265 5.307353,-6.631103 5.307353,-11.884191");

            flipFrontButton = new Button("Flip");
            flipFrontButton.getStyleClass().add("flip-button");
            flipFrontButton.setGraphic(flipGraphic);

            backPane = new StackPane()
            {

                @Override
                protected void layoutChildren()
                {
                    super.layoutChildren();

                    flipFrontButton.autosize();
                    flipFrontButton.setLayoutX(backPane.getWidth() - flipFrontButton.getWidth());
                    flipFrontButton.setLayoutY(backPane.getHeight() - flipFrontButton.getHeight());
                }

            };
            backPane.getChildren().addAll(episodeList, flipFrontButton);
            backPane.getStyleClass().add("back-pane");

            // Setup title box items
            titleLabel = new Label();
            titleLabel.setWrapText(true);
            titleLabel.getStyleClass().add("title-label");

            totalUnitsLabel = new Label();
            totalUnitsLabel.getStyleClass().add("total-units-label");

            titleBox = new VBox();
            titleBox.getStyleClass().add("title-box");
            titleBox.getChildren().addAll(titleLabel, totalUnitsLabel);

            // Setup pop over items
            popOver = new CollectionInfoPopOver();

            flipper = new FlipPanel(Orientation.HORIZONTAL);
            flipper.getFront().getChildren().add(frontPane);
            flipper.getBack().getChildren().add(backPane);
            flipper.setMaxHeight(coverImageHeight);

            root = new VBox();
            root.getStyleClass().add("collection-grid-box");
            root.getChildren().addAll(flipper, titleBox);

            super.setWidth(gridCellWidth);
            super.setHeight(gridCellHeight);
            super.setGraphic(root);
            super.scaleYProperty().bind(CollectionGridView.this.getModel().getModel(EditorCollectionFunctionBarModel.class).getView().getScaleSlider().valueProperty());
            super.scaleXProperty().bind(CollectionGridView.this.getModel().getModel(EditorCollectionFunctionBarModel.class).getView().getScaleSlider().valueProperty());

            /*
             * Controller
             */
            progressButtonsBox.setOnMouseEntered(evt ->
            {
                increaseProgressButton.setVisible(true);
                decreaseProgressButton.setVisible(true);
            });
            progressButtonsBox.setOnMouseExited(evt ->
            {
                increaseProgressButton.setVisible(false);
                decreaseProgressButton.setVisible(false);
            });

            increaseProgressButton.setOnAction(evt -> increaseProgress());
            decreaseProgressButton.setOnAction(evt -> decreaseProgress());

            playButton.setOnAction(evt ->
            {
                popOver.hide();
                flipper.flipToBack();
                shadingRectangle.setVisible(false);
                toolsPane.setVisible(false);
            });

            scoreBox.setOnMouseClicked(evt ->
            {
                if(evt.getClickCount() > 1)
                {
//                    CollectionGridView.this.getModel().getModel(FloatingPopupModel.class).getView().getRootNode().addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedPopupHandler);
                    CollectionGridView.this.getModel().sendWave(SeriesScoreFloatingWaves.SHOW_SCORE_POPUP, JrebirthUtils.buildWaveData(anime, scoreBox));

                    currentHandler = mouseExitedPopupHandler;
                }
            });
            progressButtonsBox.setOnMouseClicked(evt ->
            {
                if(evt.getClickCount() > 1)
                {
//                    CollectionGridView.this.getModel().getModel(FloatingPopupModel.class).getView().getRootNode().addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedPopupHandler);
                    CollectionGridView.this.getModel().sendWave(SeriesProgressPopupWaves.SHOW_PROGRESS_POPUP, JrebirthUtils.buildWaveData(anime, progressButtonsBox));

                    currentHandler = mouseExitedPopupHandler;
                }
            });

            optionsButton.setOnAction(evt ->
            {
                popOver.hide(Duration.ZERO);
                CollectionGridView.this.getModel().sendWave(SeriesOptionsPopupWaves.SHOW, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, anime));
            });

//            frontPane.setOnMouseEntered(evt ->
//            {
//                shadingRectangle.setVisible(true);
//                toolsPane.setVisible(true);
//
//                if(!popOver.isShowing())
//                {
//                    popOver.show(root, FXUtils.getScreenX(root) + root.getWidth() + 10, FXUtils.getScreenY(root) + 30, new Duration(350));
//                }
//            });
//            frontPane.setOnMouseExited(evt ->
//            {
//                if(!isInBounds(evt.getSceneX(), evt.getSceneY(), frontPane))
//                {
//                    shadingRectangle.setVisible(false);
//                    toolsPane.setVisible(false);
//                    popOver.hide();
//                }
//            });
            frontPane.hoverProperty().addListener((ov, oldVal, newVal) ->
            {
                if(newVal)
                {
                    shadingRectangle.setVisible(true);
                    toolsPane.setVisible(true);
                    popOver.show(root, FXUtils.getScreenX(root) + root.getWidth() + 10, FXUtils.getScreenY(root) + 30, new Duration(350));
                }
                else
                {
                    shadingRectangle.setVisible(false);
                    toolsPane.setVisible(false);
                    popOver.hide();
                }
            });
            backPane.setOnMouseExited(evt ->
            {
                flipper.flipToFront();
            });

            CollectionGridView.this.getModel().getLocalFacade().getGlobalFacade().getApplication().getStage().focusedProperty().addListener((ov, oldVal, newVal) ->
            {
                if(!newVal)
                {
                    popOver.hide();
                }
            });

            flipFrontButton.setOnAction(evt -> 
            {
                flipper.flipToFront();
            });
        }

        private void increaseProgress()
        {
            anime.getUserData().setProgress(anime.getUserData().getProgress() + 1);
            CollectionGridView.this.getModel().callCommand(SetProgressCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, anime), WaveData.build(JrebirthUtils.INTEGER_WAVE_ITEM, anime.getUserData().getProgress()));

            progressLabel.setText(anime.getUserData().getProgress() + "/" + anime.getTotalUnits());
            progressBar.setProgress((double) anime.getUserData().getProgress() / (double) anime.getTotalUnits());
        }

        private void decreaseProgress()
        {
            anime.getUserData().setProgress(anime.getUserData().getProgress() - 1);
            CollectionGridView.this.getModel().callCommand(SetProgressCommand.class, WaveData.build(JrebirthUtils.ANIME_WAVE_ITEM, anime), WaveData.build(JrebirthUtils.INTEGER_WAVE_ITEM, anime.getUserData().getProgress()));

            progressLabel.setText(anime.getUserData().getProgress() + "/" + anime.getTotalUnits());
            progressBar.setProgress((double) anime.getUserData().getProgress() / (double) anime.getTotalUnits());
        }


        @Override
        protected void updateItem(final Anime item, final boolean empty)
        {
            log.trace("Updated grid cell with anime bean {}", item.getSelectedTitle());

            if(empty)
            {
                super.setGraphic(null);
            }
            else
            {
                this.anime = item;

                anime.getUserData().addListener(observable ->
                {
                    updateUserData((AnimeUserData) observable);
                });

                coverImageView.setImage(new Image("file:zenbu/app/covers/" + anime.getId() + "/cover.jpg", true));
                titleLabel.setText(anime.getSelectedTitle());
                totalUnitsLabel.setText(anime.getTotalUnits() + " eps.");
                progressLabel.setText(anime.getUserData().getProgress() + "/" + anime.getTotalUnits());
                progressBar.setProgress((double) anime.getUserData().getProgress() / (double) anime.getTotalUnits());
                scoreLabel.setText(String.valueOf(anime.getUserData().getScore()));
                popOver.update(anime);

                episodeList.getItems().setAll(anime.getUserData().getEpisodes().stream().sorted((a, b) -> Integer.compare(a.getEpisode(), b.getEpisode())).collect(Collectors.toList()));

                super.setGraphic(root);
            }
        }

        private void updateUserData(final AnimeUserData userData)
        {
            Platform.runLater(() ->
            {
                totalUnitsLabel.setText(anime.getTotalUnits() + " eps.");
                progressLabel.setText(anime.getUserData().getProgress() + "/" + anime.getTotalUnits());
                progressBar.setProgress((double) anime.getUserData().getProgress() / (double) anime.getTotalUnits());
                scoreLabel.setText(String.valueOf(anime.getUserData().getScore()));
            });
        }

        public StackPane getFrontPane()
        {
            return frontPane;
        }

        public ImageView getCoverImageView()
        {
            return coverImageView;
        }

        public Rectangle getShadingRectangle()
        {
            return shadingRectangle;
        }

        public BorderPane getToolsPane()
        {
            return toolsPane;
        }

        public ListView<Episode> getEpisodeList()
        {
            return episodeList;
        }

        class EpisodeListCell extends ListCell<Episode>
        {
            private HBox episodeBox;

            private Region spacer;

            private Label episodeLabel;
            private Button infoButton;
            private Region infoGraphic;
            private Button playButton;
            private Region playGraphic;
            private Button deleteButton;
            private Region deleteGraphic;

            private HBox episodeActionBox;

            private Episode episode;

            public EpisodeListCell()
            {
                episodeLabel = new Label();

                infoGraphic = new Region();
                infoGraphic.getStyleClass().add("info-graphic");
                infoButton = new Button();
                infoButton.getStyleClass().add("info-button");
                infoButton.setGraphic(infoGraphic);

                playGraphic = new Region();
                playGraphic.getStyleClass().add("play-graphic");
                playButton = new Button();
                playButton.getStyleClass().add("play-button");
                playButton.setGraphic(playGraphic);

                deleteGraphic = new Region();
                deleteGraphic.getStyleClass().add("delete-graphic");
                deleteButton = new Button();
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setGraphic(deleteGraphic);

                episodeActionBox = new HBox();
                episodeActionBox.getChildren().addAll(infoButton, playButton, deleteButton);
                episodeActionBox.getStyleClass().add("episode-action-box");

                spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                episodeBox = new HBox();
                episodeBox.getStyleClass().add("episode-box");
                episodeBox.getChildren().addAll(episodeLabel, spacer, episodeActionBox);

                super.getStyleClass().add("episode-list-cell");

                infoButton.setOnAction(evt -> CollectionGridView.this.getModel().sendWave(EpisodeInfoPopupWaves.SHOW, JrebirthUtils.buildWaveData(infoButton, episode)));
                playButton.setOnAction(evt -> CollectionGridView.this.getModel().callCommand(PlayEpisodeCommand.class, WaveData.build(JrebirthUtils.EPISODE_WAVE_ITEM, episode)));
                deleteButton.setOnAction(evt ->
                {
                    CollectionGridView.this.getModel().callCommand(DeleteEpisodeCommand.class, WaveData.build(JrebirthUtils.EPISODE_WAVE_ITEM, episode));
                    CollectionGridCell.this.getEpisodeList().getItems().remove(episode);
                });
            }

            @Override
            public void updateItem(final Episode item, final boolean empty)
            {
                super.updateItem(item, empty);

                this.episode = item;

                if(empty)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    episodeLabel.setText(String.valueOf(item.getEpisode()));

                    setGraphic(episodeBox);
                }
            }
        }
    }

    public void forceUpdateCells()
    {
        GridViewSkin skin = (GridViewSkin) getRootNode().getSkin();
        skin.updateGridViewItems();
    }
}

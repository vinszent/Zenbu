package moe.zenbu.app.ui.components.popovers.collection;

import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import moe.zenbu.app.beans.Anime;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.PopOver;

public class CollectionInfoPopOver extends PopOver
{
    private Anime anime;

    private VBox root;
    private Label titleLabel;
    private Label typeLabel;
    private Label genresLabel;
    private Label descriptionLabel;

    public CollectionInfoPopOver()
    {
        /*
         * View
         */
        titleLabel = new Label();
        titleLabel.setId("title");

        typeLabel = new Label();

        genresLabel = new Label();
        genresLabel.setId("genres");

        descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);

        root = new VBox();
        root.getStyleClass().add("root");
        root.getChildren().addAll(titleLabel, typeLabel, genresLabel, descriptionLabel);
        root.setMaxWidth(400);

        setContentNode(root);
        setAutoFix(false);
        getStyleClass().add("collection-info-popover");
        skinProperty().addListener(evt ->
        {
            ((StackPane) getSkin().getNode()).getStylesheets().add("/styles/zenbu.css");
        });
    }

    public void update(final Anime anime)
    {
        this.anime = anime;

        titleLabel.setText(anime.getSelectedTitle());
        typeLabel.setText(anime.getType());
        genresLabel.setText(anime.getGenres().stream().map(g -> g.getGenre()).collect(Collectors.joining(", ")));
        descriptionLabel.setText(StringUtils.abbreviate(anime.getDescription(), 200));;
    }
}

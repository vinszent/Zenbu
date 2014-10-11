package moe.zenbu.app.ui.components.popovers.hamburger;

import java.util.stream.Collectors;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.controls.AutoCompleteField;

import org.controlsfx.control.PopOver;

public class EpisodePopover extends PopOver
{
    private VBox detailBox;
    private AutoCompleteField titleField;
    private TextField episodeField;
    private TextField subgroupsField;
    private TextField videoFlagsField;
    private TextField audioFlagsField;

    private Episode episode;

    public EpisodePopover()
    {
        titleField = new AutoCompleteField(false); 
        titleField.getEditor().setPromptText(I18n.getLocalisedString("popup.addseries.title"));

        // TODO: Create tag bar control

        episodeField = new TextField();
        episodeField.setPromptText(I18n.getLocalisedString("popup.addseries.episodes"));
        episodeField.textProperty().addListener((ov, oldVal, newVal) -> episode.setEpisode(Integer.parseInt(newVal)));

        subgroupsField = new TextField();
        subgroupsField.setPromptText(I18n.getLocalisedString("popup.addseries.subgroups"));
//        subgroupsField.textProperty().addListner((ov, oldVal, newVal) -> episode.setSubgroups(Arrays.stream(newVal.split(", ")).map(s -> new Subgroup(s, episode)).collect(Collectors.toList())));

        videoFlagsField = new TextField();
        videoFlagsField.setPromptText(I18n.getLocalisedString("popup.addseries.videoflags"));
//        videoFlagsField.textProperty().addListener((ov, oldVal, newVal) -> episode.setVideoFlags(Arrays.stream(newVal.split(", ")).map(s -> new VideoFlag(s, episode)).collect(Collectors.toList())));

        audioFlagsField = new TextField();
        audioFlagsField.setPromptText(I18n.getLocalisedString("popup.addseries.audioflags"));
//        audioFlagsField.textProperty().addListener((ov, oldVal, newVal) -> episode.setAudioFlags(Arrays.stream(newVal.split(", ")).map(s -> new AudioFlag(s, episode)).collect(Collectors.toList())));

        detailBox = new VBox();
        detailBox.getStyleClass().add("hamburger-episode-popover-box");
        detailBox.getChildren().addAll(titleField.getRoot(), episodeField, subgroupsField, videoFlagsField, audioFlagsField);

        super.setArrowLocation(ArrowLocation.RIGHT_CENTER);
        super.setContentNode(detailBox);
    }

    public void setEpisode(final Episode episode)
    {
        this.episode = episode;

        titleField.getEditor().setText(episode.getUserData().getAnime().getSelectedTitle());
        episodeField.setText(String.valueOf(episode.getEpisode()));
        subgroupsField.setText(episode.getSubgroups().stream().map(s -> s.getSubgroup()).collect(Collectors.joining(", ")));
        videoFlagsField.setText(episode.getVideoFlags().stream().map(s -> s.getVideoFlag()).collect(Collectors.joining(", ")));
        audioFlagsField.setText(episode.getAudioFlags().stream().map(s -> s.getAudioFlag()).collect(Collectors.joining(", ")));
    }
}

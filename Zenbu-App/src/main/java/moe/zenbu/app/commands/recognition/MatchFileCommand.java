package moe.zenbu.app.commands.recognition;

import moe.zenbu.app.beans.*;
import moe.zenbu.app.commands.recognition.utils.RecognitionMatcher;
import moe.zenbu.app.commands.recognition.utils.RecognitionParser;
import moe.zenbu.app.util.JrebirthUtils;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchFileCommand extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(MatchFileCommand.class);

    protected void initCommand()
    {
        super.initCommand();
    }

    @Override
    protected void perform(Wave wave)
    {
        File file = wave.get(JrebirthUtils.FILE_WAVE_ITEM);

        String filename = file.getName();

        Map parsedData = RecognitionParser.parse(filename);

        Anime anime = RecognitionMatcher.match((String) parsedData.get("parsed_title"));

        List<Episode> episodes = new ArrayList<>();

        List<String> videoFlags = (List<String>) parsedData.get("video_flags");
        List<String> audioFlags = (List<String>) parsedData.get("audio_flags");
        List<String> subgroups = (List<String>) parsedData.get("subgroups");
        String crc32 = (String) parsedData.get("crc32");
        List<Integer> episodeNumbers = (List<Integer>) parsedData.get("episode_numbers");

        episodeNumbers.forEach(e ->
        {
            Episode episode = new Episode();
            episode.setId(anime.getId());
            episode.setEpisode(e);
            episode.setVideoFlags(videoFlags.stream().map(v -> new VideoFlag(anime.getId(), e, v)).collect(Collectors.toList()));
            episode.setAudioFlags(audioFlags.stream().map(a -> new AudioFlag(anime.getId(), e, a)).collect(Collectors.toList()));
            episode.setSubgroups(subgroups.stream().map(s -> new Subgroup(anime.getId(), e, s)).collect(Collectors.toList()));
            episode.setCrc32(crc32);

            try
            {
                episode.setFilepath(file.getCanonicalPath());
            }
            catch(Exception x)
            {
                log.error("Could not get file path", x);
            }

            episodes.add(episode);
        });

        log.info("Finished matching filename {}", filename);

        sendWave(RecognitionCommandWaves.MATCH_FILE_DONE, WaveData.build(JrebirthUtils.FILE_WAVE_ITEM, file), WaveData.build(JrebirthUtils.EPISODE_LIST_WAVE_ITEM, episodes));    }
}

package moe.zenbu.app.commands.recognition;

import moe.zenbu.app.beans.Episode;
import moe.zenbu.app.util.ConcurrentUtils;
import moe.zenbu.app.util.JrebirthUtils;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.jrebirth.af.core.wave.WaveData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BatchMatchFileCommand extends DefaultPoolCommand
{
    private CountDownLatch fileLatch;
    private int total;

    private List<Episode> allEpisodes;

    @Override
    protected void initCommand()
    {
        listen(RecognitionCommandWaves.MATCH_FILE_DONE);

        allEpisodes = new ArrayList<>();
    }

    @Override
    protected void perform(Wave wave)
    {
        List<File> files = wave.getData(JrebirthUtils.FILE_LIST_WAVE_ITEM).getValue();
        total = files.size();
        fileLatch = new CountDownLatch(total);

        files.forEach(f -> callCommand(MatchFileCommand.class, WaveData.build(JrebirthUtils.FILE_WAVE_ITEM, f)));

        ConcurrentUtils.awaitSilenty(fileLatch);

        sendWave(RecognitionCommandWaves.BATCH_MATCH_FILE_DONE, WaveData.build(JrebirthUtils.FILE_LIST_WAVE_ITEM, files), WaveData.build(JrebirthUtils.EPISODE_LIST_WAVE_ITEM, allEpisodes));

        // Cleanup
        unlisten(RecognitionCommandWaves.MATCH_FILENAME_DONE);
    }

    public void doMatchFileDone(final File file, final List<Episode> episodes, final Wave wave)
    {
        allEpisodes.addAll(episodes);

        fileLatch.countDown();
    }
}

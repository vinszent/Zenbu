package moe.zenbu.app.commands.recognition;

import moe.zenbu.app.util.JrebirthUtils;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface RecognitionCommandWaves
{
    WaveType MATCH_FILENAME_DONE = WaveTypeBase.build("MATCH_FILENAME_DONE", JrebirthUtils.STRING_WAVE_ITEM, JrebirthUtils.EPISODE_LIST_WAVE_ITEM);

    WaveType MATCH_FILE_DONE = WaveTypeBase.build("MATCH_FILE_DONE", JrebirthUtils.FILE_WAVE_ITEM, JrebirthUtils.EPISODE_LIST_WAVE_ITEM);

    WaveType BATCH_MATCH_FILE_DONE = WaveTypeBase.build("BATCH_MATCH_FILE_DONE", JrebirthUtils.FILE_LIST_WAVE_ITEM, JrebirthUtils.EPISODE_LIST_WAVE_ITEM);

    WaveType TITLE_LIST_LOADED = WaveTypeBase.build("TITLE_LIST_LOADED");
}

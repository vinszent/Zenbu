package moe.zenbu.app.commands.recognition;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.commands.recognition.utils.RecognitionMatcher;
import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;

import java.util.List;

public class LoadTitleListCommand extends DefaultPoolCommand
{
    @Override
    protected void initCommand()
    {
    }

    @Override
    protected void perform(Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        List<Anime> anime = db.selectList("db.mappers.animemapper.selectAllAnime");

        RecognitionMatcher.loadTitles(anime);

        db.close();

        sendWave(RecognitionCommandWaves.TITLE_LIST_LOADED);
    }
}

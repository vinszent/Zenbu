package moe.zenbu.app.commands.db;

import moe.zenbu.app.util.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.command.DefaultPoolCommand;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.stream.Collectors;

public class CreateDatabase extends DefaultPoolCommand
{
    private static final Logger log = LoggerFactory.getLogger(CreateDatabase.class);

    private Connection conn;

    @Override
    protected void initCommand()
    {
        try
        {
            Class.forName("org.h2.Driver");
        }
        catch(ClassNotFoundException e)
        {
            log.error("Could load database driver");
        }
    }

    @Override
    protected void perform(Wave wave)
    {
        SqlSession db = DbUtils.getSqlSession();

        try
        {
            conn = DriverManager.getConnection("jdbc:h2:./zenbu/app/zenbu_dev", "sa", "");

            String createSql = IOUtils.readLines(getClass().getResourceAsStream("/db/schemas/create_db.sql")).stream().collect(Collectors.joining(""));
            String insertDefaultSettings = IOUtils.readLines(getClass().getResourceAsStream("/db/schemas/default_settings.sql")).stream().collect(Collectors.joining(""));

            conn.createStatement().execute(createSql);
            if(db.selectOne("db.mappers.settingmapper.selectSetting", "date_created") == null)
            {
                conn.createStatement().execute(insertDefaultSettings);
            }

            db.close();

            log.info("Successfully create database tables");
        }
        catch(Exception e)
        {
            log.error("Could not create database", e);
        }
    }
}

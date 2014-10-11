package moe.zenbu.app;

import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import moe.zenbu.app.commands.db.CreateDatabase;
import moe.zenbu.app.commands.recognition.LoadTitleListCommand;
import moe.zenbu.app.enums.Page;
import moe.zenbu.app.resources.Styles;
import moe.zenbu.app.services.hooks.HooksService;
import moe.zenbu.app.services.hooks.HooksWaves;
import moe.zenbu.app.services.rmi.ZenbuRmiService;
import moe.zenbu.app.services.rmi.ZenbuRmiWaves;
import moe.zenbu.app.services.rmi.ZenbuServerInterface;
import moe.zenbu.app.ui.components.pages.PageModel;
import moe.zenbu.app.ui.components.pages.PageWaves;
import moe.zenbu.app.ui.workbench.WorkbenchModel;

import moe.zenbu.app.util.JrebirthUtils;
import org.jrebirth.af.core.application.DefaultApplication;
import org.jrebirth.af.core.ui.Model;
import org.jrebirth.af.core.wave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends DefaultApplication<StackPane>
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(final String[] args)
    {
        log.info("Starting Zenbu");

        Locale.setDefault(Locale.ENGLISH);

        launchNow(args);
    }

    @Override
    protected void preInit()
    {
        super.preInit();
    }
    
    @Override
    protected void postInit()
    {
        super.postInit();
    }

    @Override
    protected void customizeScene(final Scene scene)
    {
        log.info("Loading CSS styles");

        addCSS(scene, Styles.ZENBU);
        addCSS(scene, Styles.UBUNTU);
        addCSS(scene, Styles.ROBOTO);

        scene.setFill(Color.TRANSPARENT);
    }

    @Override
    protected void customizeStage(final Stage stage)
    {
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("Zenbu");
        stage.initStyle(StageStyle.UNDECORATED);
    }

    @Override
    public Class<? extends Model> getFirstModelClass()
    {
        return WorkbenchModel.class;
    }

    @Override
    public List<Wave> getPreBootWaveList()
    {
        log.info("Sending pre boot waves");

        List<Wave> waves = new ArrayList<>();

        waves.add(WaveBuilder.create().waveGroup(WaveGroup.CALL_COMMAND).relatedClass(CreateDatabase.class).build());
        
        return waves;
    }

    @Override
    public List<Wave> getPostBootWaveList()
    {
        log.info("Sending post boot waves");

        List<Wave> waves = new ArrayList<>();
        waves.add(WaveBuilder.create().waveGroup(WaveGroup.RETURN_DATA).relatedClass(ZenbuRmiService.class).waveType(ZenbuRmiWaves.FETCH_ANIME_LIST).build());
        waves.add(WaveBuilder.create().waveGroup(WaveGroup.CALL_COMMAND).relatedClass(LoadTitleListCommand.class).build());
        waves.add(WaveBuilder.create().waveGroup(WaveGroup.UNDEFINED).relatedClass(PageModel.class).waveType(PageWaves.SHOW_PAGE).data(WaveData.build(PageWaves.PAGE, Page.COLLECTION)).build());

        return waves;
    }
}

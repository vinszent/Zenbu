package moe.zenbu.app.services.fxutils;

import javafx.stage.Stage;

import moe.zenbu.app.ui.Waves;

import org.jrebirth.af.core.concurrent.Priority;
import org.jrebirth.af.core.concurrent.RunInto;
import org.jrebirth.af.core.concurrent.RunType;
import org.jrebirth.af.core.concurrent.RunnablePriority;
import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveStageService extends DefaultService
{
    private static final Logger log = LoggerFactory.getLogger(MoveStageService.class);

    private double initX = 0.0;
    private double initY = 0.0;

    @Override
    public void initService()
    {
        log.info("Move stage service started");

        listen(Waves.SET_COORDS);
        listen(Waves.MOVE_COORDS);
    }

    @Priority(RunnablePriority.Highest)
    public void doSetCoords(final Double[] coords, final Wave wave)
    {
        log.trace("Move stage coordinates set to X:{} and Y:{}", coords[0], coords[1]);

        this.initX = coords[0];
        this.initY = coords[1];
    }

    @Priority(RunnablePriority.Highest)
    @RunInto(value = RunType.JAT)
    public void doMoveCoords(final Double[] coords, final Wave wave)
    {
        log.trace("Move stage coordinates moved to X:{} and Y:{}", coords[0], coords[1]);

        Stage stage = getLocalFacade().getGlobalFacade().getApplication().getStage();

        stage.setX(coords[0] - initX);
        stage.setY(coords[1] - initY);
    }
}

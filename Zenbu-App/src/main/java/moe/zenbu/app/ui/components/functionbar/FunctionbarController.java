package moe.zenbu.app.ui.components.functionbar;

import javafx.scene.input.MouseEvent;

import moe.zenbu.app.ui.Waves;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.ui.adapter.MouseAdapter;
import org.jrebirth.af.core.wave.WaveData;

public class FunctionbarController extends DefaultController<FunctionbarModel, FunctionbarView> implements MouseAdapter
{
    public FunctionbarController(final FunctionbarView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventHandlers() throws CoreException
    {
        getView().getRootNode().setOnMousePressed(getHandler(MouseEvent.MOUSE_PRESSED));
        getView().getRootNode().setOnMouseDragged(getHandler(MouseEvent.MOUSE_DRAGGED));
    }

    @Override
    public void mouse(MouseEvent evt)
    {
    }

    @Override
    public void mouseClicked(MouseEvent evt)
    {
    }

    @Override
    public void mouseDragDetected(MouseEvent evt)
    {
    }

    @Override
    public void mouseDragged(final MouseEvent evt)
    {
        getModel().sendWave(Waves.MOVE_COORDS, WaveData.build(Waves.COORDS, new Double[]{evt.getScreenX(), evt.getScreenY()}));
    }

    @Override
    public void mouseEntered(final MouseEvent evt)
    {
    }

    @Override
    public void mouseEnteredTarget(MouseEvent evt)
    {
    }


    @Override
    public void mouseExited(MouseEvent evt)
    {
    }

    @Override
    public void mouseExitedTarget(MouseEvent evt)
    {
    }

    @Override
    public void mouseMoved(MouseEvent evt)
    {
    }

    @Override
    public void mousePressed(final MouseEvent evt)
    {
        getModel().sendWave(Waves.SET_COORDS, WaveData.build(Waves.COORDS, new Double[]{evt.getSceneX(), evt.getY()}));
    }

    @Override
    public void mouseReleased(MouseEvent evt)
    {
    }
}

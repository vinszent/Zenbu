package moe.zenbu.app.ui.components.popups.embedded.episodeinfo;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import moe.zenbu.app.ui.components.pages.collection.CollectionPageModel;
import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.ui.adapter.MouseAdapter;

public class EpisodeInfoPopupController extends DefaultController<EpisodeInfoPopupModel, EpisodeInfoPopupView>
{
    private double initX;
    private double initY;

    public EpisodeInfoPopupController(final EpisodeInfoPopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    public void initEventAdapters() throws CoreException
    {
        addAdapter(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent mouseEvent)
            {
                System.out.println("Entered");
                getModel().getPlayer().play();
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent)
            {
                System.out.println("Exited");
                getModel().getPlayer().stop();
            }

			@Override
			public void mouse(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

			}

			@Override
			public void mouseDragDetected(MouseEvent arg0) {

			}

			@Override
			public void mouseDragged(MouseEvent arg0) {

			}

			@Override
			public void mouseEnteredTarget(MouseEvent arg0) {

			}

			@Override
			public void mouseExitedTarget(MouseEvent arg0) {

			}

			@Override
			public void mouseMoved(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}
        });

        getView().getCanvas().setOnMouseEntered(getHandler(MouseEvent.MOUSE_ENTERED));
        getView().getCanvas().setOnMouseExited(getHandler(MouseEvent.MOUSE_EXITED));

        getView().getCanvas().setOnMouseEntered(evt -> getModel().getPlayer().play());
        getView().getCanvas().setOnMouseExited(evt -> getModel().getPlayer().pause());
        getView().getCanvas().setOnScroll(evt ->
        {
            if(Math.signum(evt.getDeltaY()) == 1.0)
            {
                getModel().getPlayer().skipPosition(0.05f);
            }
            else if(Math.signum(evt.getDeltaY()) == -1.0)
            {
                getModel().getPlayer().skipPosition(-0.05f);
            }
        });

        getView().getCanvas().setOnMousePressed(evt ->
        {
            initX = evt.getX();
            initY = evt.getY();

            evt.consume();
        });
        getView().getCanvas().setOnMouseDragged(evt ->
        {
            double newWidth = getView().getCanvas().getFitWidth() + (evt.getX() - initX);
            double newHeight = getView().getCanvas().getFitHeight() + (evt.getY() - initY);

            if(newWidth < getModel().getModel(CollectionPageModel.class).getRootNode().getWidth() - 100 && newHeight < getModel().getModel(CollectionPageModel.class).getRootNode().getHeight() - 100)
            {
                getView().getCanvas().setFitWidth(newWidth);
                getView().getCanvas().setFitHeight(newHeight);
            }

            initX = evt.getX();
            initY = evt.getY();

            evt.consume();
        });
    }

    @Override
    public void initEventHandlers()
    {
    }

    public void onMouseClickedCanvas(final MouseEvent evt)
    {
        if(evt.getButton() == MouseButton.PRIMARY)
        {
            getView().getCanvas().setFitWidth(800);
            getView().getCanvas().setFitHeight(400);
        }
        else if(evt.getButton() == MouseButton.SECONDARY)
        {
            getModel().getPlayer().pause();
        }
    }
}

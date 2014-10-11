package moe.zenbu.app.ui.components.popovers.embedded.collectionfilterpopup;

import javafx.event.ActionEvent;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class CollectionFilterPopupController extends DefaultController<CollectionFilterPopupModel, CollectionFilterPopupView>
{
    public CollectionFilterPopupController(final CollectionFilterPopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    public void initEventAdapters()
    {
        
    }

    @Override
    public void initEventHandlers()
    {
    }

    public void onFinishedHide(final ActionEvent evt)
    {
        getView().getRootNode().setVisible(false);
    }
}

package moe.zenbu.app.ui.components.functionbar.collection.standard;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;

import moe.zenbu.app.ui.components.popovers.embedded.collectionfilterpopup.CollectionFilterPopupWaves;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.HamburgerPopupWaves;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class StandardCollectionFunctionBarController extends DefaultController<StandardCollectionFunctionBarModel, StandardCollectionFunctionBarView>
{
    public StandardCollectionFunctionBarController(final StandardCollectionFunctionBarView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventAdapters() throws CoreException
    {
        linkWave(getView().getHistoryButton(), ActionEvent.ACTION, HamburgerPopupWaves.SHOW);
    }

    @Override
    protected void initEventHandlers() throws CoreException
    {
    }

    public void onActionShowFilter(final ActionEvent event)
    {
        ToggleButton button = getView().getFilterButton();
        if(button.isSelected())
        {
            //getView().getFilterPanel().flipToBack();
            //getView().getFilterPopover().show(button, FXUtils.getScreenX(button) + button.getWidth() / 2.0, FXUtils.getScreenY(button) + button.getHeight() + 3.0);
            getModel().sendWave(CollectionFilterPopupWaves.SHOW_COLLECTION_FILTER_POPUP);
        }
        else
        {
            //getView().getFilterPanel().flipToFront();
            //getView().getFilterPopover().hide();
            getModel().sendWave(CollectionFilterPopupWaves.HIDE_COLLECTION_FILTER_POPUP);
        }
    }
}

package moe.zenbu.app.ui.components.popovers.embedded.hamburger;

import javafx.event.ActionEvent;

import moe.zenbu.app.enums.HamburgerContent;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class HamburgerPopupController extends DefaultController<HamburgerPopupModel, HamburgerPopupView>
{
    public HamburgerPopupController(final HamburgerPopupView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventAdapters() throws CoreException
    {
    }

    @Override
    protected void initEventHandlers() throws CoreException
    {
        getView().getContentToggles().selectedToggleProperty().addListener((ov, oldVal, newVal) ->
        {
            System.out.println("newval");
            if(newVal != null)
            {
                if(newVal.equals(getView().getAddSeriesButton()))
                {
                    getModel().doSwitchContent(HamburgerContent.ADD_SERIES, null);
                }
                else if(newVal.equals(getView().getHistoryButton()))
                {
                    getModel().doSwitchContent(HamburgerContent.HISTORY, null);
                }
                else if(newVal.equals(getView().getSyncButton()))
                {
                    getModel().doSwitchContent(HamburgerContent.SYNC, null);
                }
            }
        });
    }

    public void onActionAddSeries(final ActionEvent evt)
    {
        getModel().doSwitchContent(HamburgerContent.ADD_SERIES, null);
    }

    public void onActionSync(final ActionEvent evt) 
    {
        getModel().doSwitchContent(HamburgerContent.SYNC, null);
    }

    public void onActionHistory(final ActionEvent evt)
    {
        getModel().doSwitchContent(HamburgerContent.HISTORY, null);
    }        
}


package moe.zenbu.app.ui.components.notifications.inline;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class InlineNotificationController extends DefaultController<InlineNotificationModel, InlineNotificationView>
{
    public InlineNotificationController(final InlineNotificationView view) throws CoreException
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
    }
}

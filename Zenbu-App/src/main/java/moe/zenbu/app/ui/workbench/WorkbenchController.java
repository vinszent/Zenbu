package moe.zenbu.app.ui.workbench;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class WorkbenchController extends DefaultController<WorkbenchModel, WorkbenchView>
{
    public WorkbenchController(final WorkbenchView view) throws CoreException
    {
        super(view);
    }
}

package moe.zenbu.app.ui.components.pages.collection.grid;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class CollectionGridController extends DefaultController<CollectionGridModel, CollectionGridView>
{
    public CollectionGridController(final CollectionGridView view) throws CoreException
    {
        super(view);
    }

    @Override
    protected void initEventHandlers()
    {
    }
}

package moe.zenbu.app.ui.components.pages.collection.list;

import org.jrebirth.af.core.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

public class CollectionListController extends DefaultController<CollectionListModel, CollectionListView>
{
    public CollectionListController(final CollectionListView view) throws CoreException
    {
        super(view);
    }
}

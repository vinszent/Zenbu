package moe.zenbu.app.ui.components.popovers.embedded.collectionfilterpopup;

import javafx.scene.control.ToggleButton;

import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.components.functionbar.collection.standard.StandardCollectionFunctionBarModel;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageModel;

import org.apache.commons.lang3.StringUtils;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;

public class CollectionFilterPopupModel extends DefaultModel<CollectionFilterPopupModel, CollectionFilterPopupView>
{
    @Override
    public void initModel()
    {
        listen(CollectionFilterPopupWaves.SHOW_COLLECTION_FILTER_POPUP);
        listen(CollectionFilterPopupWaves.HIDE_COLLECTION_FILTER_POPUP);
    }

    @Override
    public void bind()
    {
        getView().getStatusBox().valueProperty().addListener((ov, oldVal, newVal) ->
        {
            getModel(CollectionPageModel.class).getFilteredList().setPredicate(a ->
            {
                String status = (String) newVal;
                if(status == null || status.isEmpty())
                {
                    return true;
                }
                else if(status.equals("All"))
                {
                    return true;
                }
                else if(a.getUserData().getStatus().equals(status.toLowerCase()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            });
        });
        getView().getSortBox().valueProperty().addListener((ov, oldVal, newVal) ->
        {
            String order = (String) newVal;
            if(StringUtils.equalsIgnoreCase(order, I18n.getLocalisedString("collection.sort.alphabetic")))
            {
                getModel(CollectionPageModel.class).getSortedList().setComparator((a, b) -> a.getSelectedTitle().compareTo(b.getSelectedTitle()));
            }
            else if(StringUtils.equalsIgnoreCase(order, I18n.getLocalisedString("collection.sort.progress")))
            {
                // TODO: If 0 (both equal) then compare total units
                getModel(CollectionPageModel.class).getSortedList().setComparator((a, b) -> Double.compare((double) b.getUserData().getProgress() / (double) b.getTotalUnits(), (double) a.getUserData().getProgress() / (double) a.getTotalUnits()));
            }
            else if(StringUtils.equalsIgnoreCase(order, I18n.getLocalisedString("collection.sort.score")))
            {
                getModel(CollectionPageModel.class).getSortedList().setComparator((a, b) -> Double.compare(b.getUserData().getScore(), a.getUserData().getScore()));
            }
        });
    }

    public void doShowCollectionFilterPopup(final Wave wave)
    {
        ToggleButton origin = getModel(StandardCollectionFunctionBarModel.class).getView().getFilterButton();
        getView().getRootNode().show(origin);
    }

    public void doHideCollectionFilterPopup(final Wave wave)
    {
        getView().getRootNode().hide();
    }
}

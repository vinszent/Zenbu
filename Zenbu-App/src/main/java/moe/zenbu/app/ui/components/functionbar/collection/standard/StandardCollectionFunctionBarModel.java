package moe.zenbu.app.ui.components.functionbar.collection.standard;

import java.util.stream.Collectors;

import moe.zenbu.app.beans.Title;
import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageModel;
import moe.zenbu.app.ui.components.pages.collection.CollectionPageWaves;
import moe.zenbu.app.ui.components.popovers.embedded.collectionfilterpopup.CollectionFilterPopupModel;
import moe.zenbu.app.ui.components.popovers.embedded.hamburger.HamburgerPopupModel;

import moe.zenbu.app.util.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.jrebirth.af.core.annotation.Component;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardCollectionFunctionBarModel extends DefaultModel<StandardCollectionFunctionBarModel, StandardCollectionFunctionBarView>
{
    private static final Logger log = LoggerFactory.getLogger(StandardCollectionFunctionBarModel.class);

    @Component
    private HamburgerPopupModel hamburgerPopupModel;

    @Component
    private CollectionFilterPopupModel collectionFilterPopupModel;

    @Override
    protected void initModel()
    {
        listen(StandardCollectionFunctionBarWaves.SHOW_FILTER_BOX);
        listen(StandardCollectionFunctionBarWaves.HIDE_FILTER_BOX);

        listen(CollectionPageWaves.REFRESH_DONE);
    }

    @Override
    public void bind()
    {
        SqlSession db = DbUtils.getSqlSession();

        refreshAutoCompleteList();
        getView().getAutoCompleteField().getEditor().textProperty().addListener((ov, oldVal, newVal) ->
        {
            getModel(CollectionPageModel.class).getFilteredList().setPredicate(a ->
            {
                if(newVal == null || newVal.isEmpty())
                {
                    return true;
                }
                else if(StringUtils.containsIgnoreCase(a.getSelectedTitle().replaceAll("[^a-zA-Z0-9\\!\\?\\s]", ""), newVal))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            });
        });
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
        getView().getOrderBox().valueProperty().addListener((ov, oldVal, newVal) ->
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

        db.close();
    }

    public void doShowFilterBox(final Wave wave)
    {
        log.trace("Showing filter box");

        getView().getFilterBox().setManaged(true);
        getView().getFilterBox().setVisible(true);
        getView().getShowFilterAnimation().playFromStart();
    }

    public void doHideFilterBox(final Wave wave)
    {
        log.trace("Hiding filter box");

        getView().getHideFilterAnimation().playFromStart();
    }        

    public void doRefreshDone(final Wave wave)
    {
        refreshAutoCompleteList();
    }

    private void refreshAutoCompleteList()
    {
        log.trace("Refreshing auto complete list");

        SqlSession db = DbUtils.getSqlSession();

        getView().getAutoCompleteField().setItems(db.selectList("db.mappers.titlemapper.selectAllSelectedTitleInCollection").stream().map(o -> ((Title) o).getTitle()).collect(Collectors.toList()));

        db.close();
    }
}

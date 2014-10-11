package moe.zenbu.app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import moe.zenbu.app.resources.I18n;

public class ZenbuUtils
{
    private static ObservableList<String[]> statusList;

    static
    {
        String[][] statusArray = new String[5][2];

        statusArray[0] = new String[]{I18n.getLocalisedString("collection.status.current"), "current"};
        statusArray[1] = new String[]{I18n.getLocalisedString("collection.status.completed"), "completed"};
        statusArray[2] = new String[]{I18n.getLocalisedString("collection.status.on-hold"), "on-hold"};
        statusArray[3] = new String[]{I18n.getLocalisedString("collection.status.planned"), "planned"};
        statusArray[4] = new String[]{I18n.getLocalisedString("collection.status.dropped"), "dropped"};

        statusList = FXCollections.observableArrayList(statusArray);
    }

    public static ObservableList<String[]> getStatusList()
    {
        return statusList;
    }
}

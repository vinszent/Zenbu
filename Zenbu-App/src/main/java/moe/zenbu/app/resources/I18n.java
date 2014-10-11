package moe.zenbu.app.resources;

import java.util.ResourceBundle;

public class I18n
{
    //private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.ZenbuBundle", new Locale("sv"));
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.ZenbuBundle");
    
    public static String getLocalisedString(final String key)
    {
        return bundle.getString(key);
    }

    public static ResourceBundle getBundle()
    {
        return bundle;
    }
}

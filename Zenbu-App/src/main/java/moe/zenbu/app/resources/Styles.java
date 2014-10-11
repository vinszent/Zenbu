package moe.zenbu.app.resources;

import static org.jrebirth.af.core.resource.Resources.create;

import org.jrebirth.af.core.resource.style.StyleSheet;
import org.jrebirth.af.core.resource.style.StyleSheetItem;

public interface Styles
{
    StyleSheetItem ZENBU = create(new StyleSheet("zenbu"));
    
    StyleSheetItem UBUNTU = create(new StyleSheet("ubuntu"));

    StyleSheetItem ROBOTO = create(new StyleSheet("roboto"));

    StyleSheetItem JMETRO = create(new StyleSheet("JMetroLightTheme"));
}

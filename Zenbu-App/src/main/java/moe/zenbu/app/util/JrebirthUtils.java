package moe.zenbu.app.util;

import java.io.File;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

import moe.zenbu.app.beans.Anime;
import moe.zenbu.app.beans.AnimeUserData;
import moe.zenbu.app.beans.Episode;
import org.jrebirth.af.core.wave.WaveData;
import org.jrebirth.af.core.wave.WaveItem;

public class JrebirthUtils
{
    private JrebirthUtils()
    {
    }

    // Basic types Jrebirth wave items
    public final static WaveItem<String> STRING_WAVE_ITEM = new WaveItem<String>(){};

    public final static WaveItem<List<String>> STRING_LIST_WAVE_ITEM = new WaveItem<List<String>>(){};
    
    public final static WaveItem<Integer> INTEGER_WAVE_ITEM = new WaveItem<Integer>(){};

    public final static WaveItem<Double> DOUBLE_WAVE_ITEM = new WaveItem<Double>(){};

    public final static WaveItem<Boolean> BOOLEAN_WAVE_ITEM = new WaveItem<Boolean>(){};

    public final static WaveItem<Object[]> OBJECTARR_WAVE_ITEM = new WaveItem<Object[]>(){};

    public final static WaveItem<File> FILE_WAVE_ITEM = new WaveItem<File>(){};

    public final static WaveItem<List<File>> FILE_LIST_WAVE_ITEM = new WaveItem<List<File>>(){};

    // Zenbu type Jrebirth wave items
    public final static WaveItem<Anime> ANIME_WAVE_ITEM = new WaveItem<Anime>(){};

    public final static WaveItem<List<Anime>> ANIME_LIST_WAVE_ITEM = new WaveItem<List<Anime>>(){};

    public final static WaveItem<AnimeUserData> ANIME_USER_DATA_WAVE_ITEM = new WaveItem<AnimeUserData>(){};

    public final static WaveItem<List<AnimeUserData>> ANIME_USER_DATA_LIST_WAVE_ITEM = new WaveItem<List<AnimeUserData>>(){};

    public final static WaveItem<Episode> EPISODE_WAVE_ITEM = new WaveItem<Episode>(){};

    public final static WaveItem<List<Episode>> EPISODE_LIST_WAVE_ITEM = new WaveItem<List<Episode>>(){};

    // JavaFX type Jrebirth wave items
    public final static WaveItem<Node> NODE_WAVE_ITEM = new WaveItem<Node>(){};

    public final static WaveItem<StringProperty> STRING_PROPERTY_WAVE_ITEM = new WaveItem<StringProperty>(){};

    public final static WaveItem<DoubleProperty> DOUBLE_PROPERTY_WAVE_ITEM = new WaveItem<DoubleProperty>(){};

    public static <T> WaveData buildWaveData(final T data)
    {
        WaveItem<T> item = new WaveItem<T>(){};

        return WaveData.build(item, data);
    }

    public static <T> WaveData[] buildWaveData(final T... data)
    {
        WaveData[] items = new WaveData[data.length];

        for(int i = 0; i < data.length; i++)
        {
            items[i] = buildWaveData(data[i]);
        }

        return items;
    }
}

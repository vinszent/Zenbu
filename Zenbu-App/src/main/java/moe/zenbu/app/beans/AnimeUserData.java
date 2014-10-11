package moe.zenbu.app.beans;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import moe.zenbu.app.util.DbUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AnimeUserData implements Observable
{
    public static final String STATUS_CURRENT = "current";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_PLANNED = "planned";
    public static final String STATUS_ON_HOLD = "on-hold";
    public static final String STATUS_DROPPED = "dropped";

    private List<InvalidationListener> listeners = new ArrayList<>();

    private int id;

    private String status;

    private double score;

    private int progress;

    private Date lastChanged;

    private List<Episode> episodes = Collections.emptyList();

    private Anime anime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
        notifyListeners();
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
        notifyListeners();
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
        notifyListeners();
    }

    public Date getLastChanged()
    {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged)
    {
        this.lastChanged = lastChanged;
    }

    public List<Episode> getEpisodes()
    {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes)
    {
        this.episodes = episodes;
    }

    public Anime getAnime()
    {
        if(anime == null)
        {
            anime = DbUtils.getSqlSession().selectOne("db.mappers.animemapper.selectAnime", id);
        }

        return anime;
    }

    public void setAnime(Anime anime)
    {
        this.anime = anime;
    }

    // Util methods
    public boolean isUserDataDifferent(final AnimeUserData other)
    {
        return other.getProgress() == progress && other.getStatus().equals(status) && other.getScore() == score;
    }

    @Override public void addListener(InvalidationListener listener)
    {
        if(!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    @Override public void removeListener(InvalidationListener listener)
    {
        listeners.add(listener);
    }

    private void notifyListeners()
    {
        listeners.forEach(l -> l.invalidated(this));
    }
}

package moe.zenbu.app.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Anime implements Serializable
{
    private static final long serialVersionUID = -1781658712677377479L;

    private int id;

    private int malId;

    private int hbId;

    private int alId;

    private int adbId;

    private String description;

    private String type;

    private int totalUnits;

    private String malImageUrl;

    private String hbImageUrl;

    private String alImageUrl;

    private String adbImageUrl;

    private List<Title> titles;

    private List<Genre> genres;

    private Date lastUpdated;

    public Anime()
    {

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getMalId()
    {
        return malId;
    }

    public void setMalId(int malId)
    {
        this.malId = malId;
    }

    public int getHbId()
    {
        return hbId;
    }

    public void setHbId(int hbId)
    {
        this.hbId = hbId;
    }

    public int getAlId()
    {
        return alId;
    }

    public void setAlId(int alId)
    {
        this.alId = alId;
    }

    public int getAdbId()
    {
        return adbId;
    }

    public void setAdbId(int adbId)
    {
        this.adbId = adbId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getTotalUnits()
    {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits)
    {
        this.totalUnits = totalUnits;
    }

    public String getMalImageUrl()
    {
        return malImageUrl;
    }

    public void setMalImageUrl(String malImageUrl)
    {
        this.malImageUrl = malImageUrl;
    }

    public String getHbImageUrl()
    {
        return hbImageUrl;
    }

    public void setHbImageUrl(String hbImageUrl)
    {
        this.hbImageUrl = hbImageUrl;
    }

    public String getAlImageUrl()
    {
        return alImageUrl;
    }

    public void setAlImageUrl(String alImageUrl)
    {
        this.alImageUrl = alImageUrl;
    }

    public String getAdbImageUrl()
    {
        return adbImageUrl;
    }

    public void setAdbImageUrl(String adbImageUrl)
    {
        this.adbImageUrl = adbImageUrl;
    }

    public List<Title> getTitles()
    {
        return titles;
    }

    public void setTitles(List<Title> titles)
    {
        this.titles = titles;
    }

    public List<Genre> getGenres()
    {
        return genres;
    }

    public void setGenres(List<Genre> genres)
    {
        this.genres = genres;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }
}

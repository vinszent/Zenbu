package moe.zenbu.app.beans;

import moe.zenbu.server.util.DbUtils;

import java.io.Serializable;

public class Title implements Serializable
{
    private static final long serialVersionUID = -8010084004288937751L;

    private int id;

    private String title;

    private boolean selected;

    public Title()
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public Anime getAnime()
    {
        return DbUtils.getSqlSession().selectOne("db.mappers.animemapper.selectAnime", id);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Title title1 = (Title) o;

        if(id != title1.id) return false;
        if(!title.equals(title1.title)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + title.hashCode();
        return result;
    }
}

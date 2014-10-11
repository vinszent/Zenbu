package moe.zenbu.app.beans;

import moe.zenbu.app.util.DbUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;

public class Title implements Serializable
{
    private static final long serialVersionUID = -8010084004288937751L;

    private int id;

    private String title;

    private boolean selected;

    private Anime anime;

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
        if(anime == null)
        {
            SqlSession db = DbUtils.getSqlSession();
            anime = db.selectOne("db.mappers.animemapper.selectAnime", id);
            db.close();
        }

        return anime;
    }
}

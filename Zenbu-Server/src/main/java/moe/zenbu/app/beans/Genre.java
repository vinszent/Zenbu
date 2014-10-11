package moe.zenbu.app.beans;

import java.io.Serializable;

public class Genre implements Serializable
{
    private static final long serialVersionUID = -5428480361102037742L;

    private int id;

    private String genre;

    public Genre()
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

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Genre genre1 = (Genre) o;

        if(id != genre1.id) return false;
        if(genre != null ? !genre.equals(genre1.genre) : genre1.genre != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }
}

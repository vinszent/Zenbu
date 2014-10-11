/*
 * Zenbu is a cross-platform, multi functional anime/manga management client with a modern UI and exceptional features
 * Copyright (C) 2014 Vincent Szolnoky a.k.a. Ippytraxx
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU General Public License as published by
 * Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
}

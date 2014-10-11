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

public class Setting implements Serializable
{
    private static final long serialVersionUID = 4766638424451191163L;

    private String setting;

    private String value;

    public Setting()
    {

    }

    public String getSetting()
    {
        return setting;
    }

    public void setSetting(String setting)
    {
        this.setting = setting;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    // Util methods
    public boolean getValueAsBoolean()
    {
        if(value.equals("true"))
        {
            return true;
        }
        else if(value.equals("false"))
        {
            return false;
        }
        else
        {
            throw new UnsupportedOperationException("Value could not be converted to boolean");
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Setting setting1 = (Setting) o;

        if(setting != null ? !setting.equals(setting1.setting) : setting1.setting != null) return false;
        if(value != null ? !value.equals(setting1.value) : setting1.value != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = setting != null ? setting.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}

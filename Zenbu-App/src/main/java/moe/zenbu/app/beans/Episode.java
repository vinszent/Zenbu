package moe.zenbu.app.beans;

import moe.zenbu.app.util.DbUtils;

import java.util.List;

public class Episode
{
    private int id;

    private int episode;

    private String crc32;

    private String filepath;

    private List<Subgroup> subgroups;

    private List<VideoFlag> videoFlags;

    private List<AudioFlag> audioFlags;

    private AnimeUserData userData;

    public Episode()
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

    public int getEpisode()
    {
        return episode;
    }

    public void setEpisode(int episode)
    {
        this.episode = episode;
    }

    public String getCrc32()
    {
        return crc32;
    }

    public void setCrc32(String crc32)
    {
        this.crc32 = crc32;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
    }

    public List<Subgroup> getSubgroups()
    {
        return subgroups;
    }

    public void setSubgroups(List<Subgroup> subgroups)
    {
        this.subgroups = subgroups;
    }

    public List<VideoFlag> getVideoFlags()
    {
        return videoFlags;
    }

    public void setVideoFlags(List<VideoFlag> videoFlags)
    {
        this.videoFlags = videoFlags;
    }

    public List<AudioFlag> getAudioFlags()
    {
        return audioFlags;
    }

    public void setAudioFlags(List<AudioFlag> audioFlags)
    {
        this.audioFlags = audioFlags;
    }

    public AnimeUserData getUserData()
    {
        if(userData == null)
        {
            userData = DbUtils.getSqlSession().selectOne("db.mappers.animeuserdatamapper.selectAnimeUserData", id);
        }

        return userData;
    }

    public void setUserData(AnimeUserData userData)
    {
        this.userData = userData;
    }
}

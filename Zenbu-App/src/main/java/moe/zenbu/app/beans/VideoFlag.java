package moe.zenbu.app.beans;

public class VideoFlag
{
    private int id;

    private int episode;

    private String videoFlag;

    public VideoFlag()
    {

    }

    public VideoFlag(int id, int episode, String videoFlag)
    {
        this.id = id;
        this.episode = episode;
        this.videoFlag = videoFlag;
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

    public String getVideoFlag()
    {
        return videoFlag;
    }

    public void setVideoFlag(String videoFlag)
    {
        this.videoFlag = videoFlag;
    }
}

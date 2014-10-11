package moe.zenbu.app.beans;

public class AudioFlag
{
    private int id;

    private int episode;

    private String audioFlag;

    public AudioFlag()
    {

    }

    public AudioFlag(int id, int episode, String audioFlag)
    {
        this.id = id;
        this.episode = episode;
        this.audioFlag = audioFlag;
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

    public String getAudioFlag()
    {
        return audioFlag;
    }

    public void setAudioFlag(String audioFlag)
    {
        this.audioFlag = audioFlag;
    }
}

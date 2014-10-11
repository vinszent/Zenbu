package moe.zenbu.app.commands.recognition.utils;


class TitleNumber
{
    private String value;
    
    private int position;
    
    private boolean touched = false;

    public TitleNumber(String value, int position)
    {
        this.value = value;
        this.position = position;
    }

    public String getValue()
    {
        return value;
    }

    public int getPosition()
    {
        return position;
    }

    public boolean isTouched()
    {
        return touched;
    }

    public void setTouched(boolean touched)
    {
        this.touched = touched;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }
}

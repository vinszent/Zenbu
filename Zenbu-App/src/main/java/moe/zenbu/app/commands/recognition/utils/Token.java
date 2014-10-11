package moe.zenbu.app.commands.recognition.utils;

class Token
{
    private String token;
    private boolean touched = false;
    private boolean enclosed = false;

    public Token(final String token, final boolean enclosed)
    {
        this.token = token;
        this.enclosed = enclosed;

        if(enclosed)
        {
            this.token = token.substring(1, token.length() - 1);
        }
    }

    public boolean isTouched()
    {
        return touched;
    }

    public void setTouched(boolean touched)
    {
        this.touched = touched;
    }

    public String getToken()
    {
        return token;
    }

    public boolean isEnclosed()
    {
        return enclosed;
    }

    public void setEnclosed(boolean enclosed)
    {
        this.enclosed = enclosed;
    }
}

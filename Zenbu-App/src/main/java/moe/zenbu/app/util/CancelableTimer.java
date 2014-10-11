package moe.zenbu.app.util;

public class CancelableTimer implements Runnable
{
    private long runTime;
    private boolean cancel;
    private Runnable action;

    public CancelableTimer(final long runTime, final Runnable action)
    {
        this.runTime = runTime;
        this.action = action;
    }

    @Override
    public void run()
    {
        cancel = false;

        try
        {
            Thread.sleep(runTime);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(cancel)
        {
            return;
        }


        action.run();
    }

    public void cancel()
    {
        cancel = true; 
    }        
}

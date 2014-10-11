package moe.zenbu.app.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentUtils
{
    private static final Logger log = LoggerFactory.getLogger(ConcurrentUtils.class);

    public static void awaitSilenty(final CountDownLatch latch)
    {
        try
        {
            latch.await();
        }
        catch(Exception e)
        {
            log.error("Interrupted while waiting", e);
        }
    }

    public static void awaitSilenty(final Condition cond)
    {
        try
        {
            cond.await();
        }
        catch(Exception e)
        {
            log.error("Interrupted while waiting", e);
        }
    }

    public static void sleepSilentlyMillis(final long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(Exception e)
        {
            log.error("Interrupted while sleeping", e);
        }
    }
}

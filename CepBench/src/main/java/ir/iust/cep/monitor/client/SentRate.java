package ir.iust.cep.monitor.client;

import ir.iust.cep.config.ClientConfig;
import org.apache.log4j.Logger;

/**
 * Created by Hesam on 9/24/16.
 */
public class SentRate
{

    private final static Logger log = Logger.getLogger("sent_rate");

    private static int eventSentPerSec;

    static
    {
        log.setLevel(ClientConfig.SENT_RATE_LOG_LEVEL);
    }

    public static void doCalculate ()
    {
        eventSentPerSec++;
    }

    public static void restEventSent ()
    {
        eventSentPerSec = 0;
    }

    public static void doLog ()
    {
        log.info("sent_rate," + eventSentPerSec);
    }
}

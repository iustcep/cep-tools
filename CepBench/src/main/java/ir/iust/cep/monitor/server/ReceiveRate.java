package ir.iust.cep.monitor.server;

import ir.iust.cep.config.ServerConfig;
import org.apache.log4j.Logger;

/**
 * Created by Hesam on 9/25/16.
 */
public class ReceiveRate
{

    private final static Logger log = Logger.getLogger("receive_rate");

    private static int eventReceivedPerSec;

    static
    {
        log.setLevel(ServerConfig.RECEIVE_RATE_LOG_LEVEL);
    }

    public static void doCalculate ()
    {
        eventReceivedPerSec++;
    }

    public static void restEventReceive ()
    {
        eventReceivedPerSec = 0;
    }

    public static void doLog ()
    {
        log.info("received_rate," + eventReceivedPerSec);
    }
}

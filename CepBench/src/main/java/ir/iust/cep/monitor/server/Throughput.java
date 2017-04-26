package ir.iust.cep.monitor.server;

import ir.iust.cep.business.engine.EngineFactory;
import ir.iust.cep.config.ServerConfig;
import org.apache.log4j.Logger;

/**
 * Created by Hesam on 9/25/16.
 */
public class Throughput
{

    private final static Logger log = Logger.getLogger("throughput");

    private static int[] throughputPerSec;

    static
    {
        log.setLevel(ServerConfig.THROUGHPUT_LOG_LEVEL);
        throughputPerSec = new int[EngineFactory.getRulesNumber(ServerConfig.ENGINE_NAME)];

    }

    public static void doCalculate (int ruleId)
    {
        throughputPerSec[ruleId - 1]++;
    }

    public static void resetThroughput ()
    {
        for (int i = 0; i < throughputPerSec.length; i++)
        {
            throughputPerSec[i] = 0;
        }
    }

    public static void doLog ()
    {
        for (int i = 0; i < throughputPerSec.length; i++)
        {
            if (throughputPerSec[i] != 0)
            {
                log.info((i + 1) + "," + throughputPerSec[i]);
            }
        }
    }
}

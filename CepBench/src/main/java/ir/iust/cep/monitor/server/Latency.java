package ir.iust.cep.monitor.server;

import ir.iust.cep.business.engine.EngineFactory;
import ir.iust.cep.config.ServerConfig;
import org.apache.log4j.Logger;

/**
 * Created by Hesam on 11/25/16.
 */
public class Latency
{

    private final static Logger log = Logger.getLogger("latency");

    private static long[] eachRulesLatencySum;
    private static int[] eachRulesObservationCount;

    static
    {
        log.setLevel(ServerConfig.LATENCY_LOG_LEVEL);
        eachRulesLatencySum = new long[EngineFactory.getRulesNumber(ServerConfig.ENGINE_NAME)];
        eachRulesObservationCount = new int[EngineFactory.getRulesNumber(ServerConfig.ENGINE_NAME)];
    }

    public static void doCalculate (int ruleId, long latency)
    {
        eachRulesLatencySum[ruleId - 1] += latency;
        eachRulesObservationCount[ruleId - 1]++;
    }

    public static void resetLatency ()
    {
        for (int i = 0; i < eachRulesLatencySum.length; i++)
        {
            eachRulesLatencySum[i] = 0;
            eachRulesObservationCount[i] = 0;
        }
    }

    public static void doLog()
    {
        for (int i = 0; i < eachRulesLatencySum.length; i++)
        {
            if (eachRulesLatencySum[i] != 0)
            {
                log.info((i + 1) + "," + (eachRulesLatencySum[i] / eachRulesObservationCount[i]));
            }
        }
    }
}

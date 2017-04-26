package ir.iust.cep.monitor.server;

import java.util.TimerTask;

/**
 * Created by Hesam on 9/25/16.
 */
public class ServerScheduledTask extends TimerTask
{

    @Override
    public void run ()
    {
        ReceiveRate.doLog();
        ReceiveRate.restEventReceive();

        Throughput.doLog();
        Throughput.resetThroughput();

        Latency.doLog();
        Latency.resetLatency();
    }
}

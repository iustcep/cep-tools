package ir.iust.cep.monitor.client;

import java.util.TimerTask;

/**
 * Created by Hesam on 9/24/16.
 */
public class ClientScheduledTask extends TimerTask
{

    @Override
    public void run ()
    {
        SentRate.doLog();
        SentRate.restEventSent();
    }
}

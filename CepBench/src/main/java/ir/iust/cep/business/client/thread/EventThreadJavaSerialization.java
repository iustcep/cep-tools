package ir.iust.cep.business.client.thread;

import ir.iust.cep.business.client.EventProducer;
import ir.iust.cep.business.client.queue.QueueEventJava;
import ir.iust.cep.data.model.EventType;
import ir.iust.cep.monitor.client.SentRate;


/**
 * Created by Hesam on 9/24/16.
 */
public class EventThreadJavaSerialization extends Thread
{

    private EventType eventType;

    private long numberOfPacketSend;

    public EventThreadJavaSerialization (EventType eventType)
    {
        this.eventType = eventType;
        this.numberOfPacketSend = 0l;
    }

    @Override
    public void run ()
    {
        long startTime = System.currentTimeMillis();
        while (true)
        {
            try
            {
                if (this.eventType.getRate() == -1)
                {
                    putNewEventInQueue();
                }
                else
                {
                    for (int i = 0; i < (((System.currentTimeMillis() - startTime) / 1000) * this.eventType.getRate()) - numberOfPacketSend; i++)
                    {
                        putNewEventInQueue();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void putNewEventInQueue ()
    {
        QueueEventJava.putEventInQueue(EventProducer.createJavaEvent(this.eventType.getName()));
        this.numberOfPacketSend++;
        SentRate.doCalculate();
    }
}

package ir.iust.cep.business.client.queue;

import ir.iust.cep.data.model.EventJava;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Hesam on 9/24/16.
 */
public class QueueEventJava
{

    private final static int QUEUE_SIZE = 1024 * 1024;

    private static BlockingQueue<EventJava> events;

    public static void initQueue ()
    {
        events = new ArrayBlockingQueue<>(QUEUE_SIZE);
    }

    public static void putEventInQueue (EventJava event)
    {
        try
        {
            events.put(event);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static EventJava takeEventFromQueue ()
    {
        try
        {
            return events.take();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

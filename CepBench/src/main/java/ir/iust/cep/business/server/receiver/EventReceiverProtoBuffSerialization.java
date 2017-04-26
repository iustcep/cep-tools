package ir.iust.cep.business.server.receiver;


import ir.iust.cep.data.gen.Generated.EventProtocolBuffer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Hesam on 9/25/16.
 */
public class EventReceiverProtoBuffSerialization extends Thread
{

    public static BlockingQueue<EventProtocolBuffer> receivedEvents = new ArrayBlockingQueue<EventProtocolBuffer>(1024 * 1024);

    private Socket socket;

    public EventReceiverProtoBuffSerialization (Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run ()
    {
        try
        {
            while (true)
            {
                EventProtocolBuffer event = EventProtocolBuffer.parseDelimitedFrom(this.socket.getInputStream());
                event.addTimeStamp();
                receivedEvents.put(event);
            }
        }
        catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }


    }
}

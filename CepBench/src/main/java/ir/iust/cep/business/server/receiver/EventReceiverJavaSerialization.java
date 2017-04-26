package ir.iust.cep.business.server.receiver;

import ir.iust.cep.data.model.EventJava;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Hesam on 9/25/16.
 */
public class EventReceiverJavaSerialization extends Thread
{

    public static BlockingQueue<EventJava> receivedEvents = new ArrayBlockingQueue<EventJava>(1024 * 1024);

    private Socket socket;

    public EventReceiverJavaSerialization (Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run ()
    {
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            while (true)
            {
                EventJava event = (EventJava) objectInputStream.readObject();
                event.addTimeStamp();
                receivedEvents.put(event);

            }
        }
        catch (IOException | InterruptedException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}

package ir.iust.cep.business.client.sender;

import ir.iust.cep.business.client.Client;
import ir.iust.cep.business.client.thread.EventThreadJavaSerialization;
import ir.iust.cep.business.client.queue.QueueEventJava;
import ir.iust.cep.config.ClientConfig;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Hesam on 9/24/16.
 */
public class EventSenderJavaSerialization extends Thread
{

    private Client client;

    public EventSenderJavaSerialization (Client client)
    {
        this.client = client;

        EventThreadJavaSerialization[] eventThreads = new EventThreadJavaSerialization[ClientConfig.eventTypes.size()];

        for (int i = 0; i < ClientConfig.eventTypes.size(); i++)
        {
            eventThreads[i] = new EventThreadJavaSerialization(ClientConfig.eventTypes.get(i));
            eventThreads[i].start();
            System.out.println("*** Event Thread " + (i + 1) + " Has Been Started");
        }
    }

    @Override
    public void run()
    {
        Socket socket = null;

        System.out.println("*** Try to Connect to the Host");
        System.out.println("*** Try to Connect to the Host.");
        System.out.println("*** Try to Connect to the Host..");
        System.out.println("*** Try to Connect to the Host...");

        try
        {
            socket = new Socket(this.client.getHost(), this.client.getPort());

            System.out.println("*** Client Has Been Connected to " + this.client.getHost() + ":" + this.client.getPort() + " ***");
            System.out.println("*** Sending Event to the Server Has Been Started ***");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            while (true)
            {
                objectOutputStream.writeObject(QueueEventJava.takeEventFromQueue());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

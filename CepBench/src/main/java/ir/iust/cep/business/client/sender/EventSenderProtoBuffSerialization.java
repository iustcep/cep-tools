package ir.iust.cep.business.client.sender;

import ir.iust.cep.business.client.Client;
import ir.iust.cep.business.client.thread.EventThreadProtoBuffSerialization;
import ir.iust.cep.business.client.queue.QueueEventProtoBuff;
import ir.iust.cep.config.ClientConfig;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Hesam on 9/24/16.
 */
public class EventSenderProtoBuffSerialization extends Thread
{

    private Client client;

    public EventSenderProtoBuffSerialization (Client client)
    {
        this.client = client;

        EventThreadProtoBuffSerialization[] eventThreads = new EventThreadProtoBuffSerialization[ClientConfig.eventTypes.size()];

        for (int i = 0; i < ClientConfig.eventTypes.size(); i++)
        {
            eventThreads[i] = new EventThreadProtoBuffSerialization(ClientConfig.eventTypes.get(i));
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

            while (true)
            {
                QueueEventProtoBuff.takeEventFromQueue().writeDelimitedTo(socket.getOutputStream());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

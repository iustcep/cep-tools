package ir.iust.cep.business.server;

import ir.iust.cep.business.engine.ICEPProvider;
import ir.iust.cep.business.server.receiver.EventReceiverJavaSerialization;
import ir.iust.cep.business.server.receiver.EventReceiverProtoBuffSerialization;
import ir.iust.cep.config.AppConfig;
import ir.iust.cep.data.gen.Generated.EventProtocolBuffer;
import ir.iust.cep.data.model.EventJava;
import ir.iust.cep.monitor.server.ReceiveRate;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Hesam on 9/25/16.
 */
public class ClientConnection extends Thread
{

    public ICEPProvider provider;
    private Socket socket;
    private int clientId;

    public ClientConnection (ICEPProvider provider, Socket socket, int clientId)
    {
        this.provider = provider;
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("*** Receiving Event From Client " + this.clientId + " ***");

            if (AppConfig.SERIALIZATION_TYPE.equals("JAVA"))
            {
                EventReceiverJavaSerialization eventReceiver = new EventReceiverJavaSerialization(this.socket);
                eventReceiver.start();
                do
                {
                    EventJava event = EventReceiverJavaSerialization.receivedEvents.take();
                    if (event != null)
                    {
                        this.provider.sendEvent(event);
                        ReceiveRate.doCalculate();
                    }
                }
                while (true);
            }
            else if (AppConfig.SERIALIZATION_TYPE.equals("PROTOCOL_BUFFER"))
            {
                EventReceiverProtoBuffSerialization eventReceiver = new EventReceiverProtoBuffSerialization(this.socket);
                eventReceiver.start();
                do
                {
                    EventProtocolBuffer event = EventReceiverProtoBuffSerialization.receivedEvents.take();
                    if (event != null)
                    {
                        this.provider.sendEvent(event);
                        ReceiveRate.doCalculate();
                    }
                }
                while (true);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (this.socket != null)
            {
                try
                {
                    this.socket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

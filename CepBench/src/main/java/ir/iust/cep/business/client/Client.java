package ir.iust.cep.business.client;

import ir.iust.cep.business.client.queue.QueueEventJava;
import ir.iust.cep.business.client.queue.QueueEventProtoBuff;
import ir.iust.cep.business.client.sender.EventSenderJavaSerialization;
import ir.iust.cep.business.client.sender.EventSenderProtoBuffSerialization;
import ir.iust.cep.config.AppConfig;
import ir.iust.cep.config.ClientConfig;
import ir.iust.cep.monitor.client.ClientScheduledTask;

import java.util.Timer;

/**
 * Created by Hesam on 9/20/16.
 */
public class Client
{
    private String host;
    private int port;

    public Client (String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public String getHost ()
    {
        return host;
    }

    public int getPort ()
    {
        return port;
    }

    public static void main (String[] args)
    {
        if ((args != null) && (args.length > 0))
        {
            ClientConfig.load(args[0]);
            AppConfig.load(args[1]);
        }
        else
        {
            ClientConfig.load();
            AppConfig.load();
        }

        Client myClient = new Client(ClientConfig.DEFAULT_HOST, ClientConfig.DEFAULT_PORT);

        if (AppConfig.SERIALIZATION_TYPE.equals("JAVA"))
        {
            QueueEventJava.initQueue();
            EventSenderJavaSerialization eventSender = new EventSenderJavaSerialization(myClient);
            eventSender.start();

            Timer monitorTimer = new Timer();
            ClientScheduledTask periodicTask = new ClientScheduledTask();
            monitorTimer.schedule(periodicTask, 0, 1000);

            try
            {
                eventSender.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        else if (AppConfig.SERIALIZATION_TYPE.equals("PROTOCOL_BUFFER"))
        {
            QueueEventProtoBuff.initQueue();
            EventSenderProtoBuffSerialization eventSender = new EventSenderProtoBuffSerialization(myClient);
            eventSender.start();

            Timer monitorTimer = new Timer();
            ClientScheduledTask periodicTask = new ClientScheduledTask();
            monitorTimer.schedule(periodicTask, 0, 1000);

            try
            {
                eventSender.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        else
        {
            System.out.println("*** There is a problem in application configuration file, please check it ***");
            System.exit(0);
        }
    }
}

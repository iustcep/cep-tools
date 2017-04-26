package ir.iust.cep.business.server;

import ir.iust.cep.business.engine.EngineFactory;
import ir.iust.cep.business.engine.ICEPProvider;
import ir.iust.cep.config.AppConfig;
import ir.iust.cep.config.ServerConfig;
import ir.iust.cep.monitor.server.ServerScheduledTask;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

/**
 * Created by Hesam on 9/25/16.
 */
public class Server extends Thread
{

    private int port;
    private ICEPProvider cepProvider;

    public Server (int port)
    {
        this.port = port;
    }

    @Override
    public synchronized void start ()
    {
        System.out.println("*** Server has been started ***");
        this.cepProvider = EngineFactory.getCepProvider(ServerConfig.ENGINE_NAME);
        System.out.println("*** Engine " + ServerConfig.ENGINE_NAME + " ***");
        this.cepProvider.init();
        super.start();
    }

    @Override
    public void run ()
    {
        this.startupServer();
    }

    public void startupServer ()
    {
        ServerSocket serverSocket = null;
        Socket socket = null;

        Timer monitorTimer = new Timer();
        ServerScheduledTask periodicTask = new ServerScheduledTask();
        monitorTimer.schedule(periodicTask, 0, 1000);

        try
        {
            serverSocket = new ServerSocket(this.port);
            while (true)
            {
                socket = serverSocket.accept();
                System.out.println("*** New client connected to server ****");

                ConnectionPool.establishConnection(socket, this.cepProvider);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main (String[] args)
    {
        if ((args != null) && (args.length > 0))
        {
            ServerConfig.load(args[0]);
            AppConfig.load(args[1]);
        }
        else
        {
            ServerConfig.load();
            AppConfig.load();
        }

        Server server = new Server(ServerConfig.DEFAULT_PORT);
        server.start();

//        Runtime.getRuntime().addShutdownHook(new LogHandler());
    }
}

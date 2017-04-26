package ir.iust.cep.business.server;


import ir.iust.cep.business.engine.ICEPProvider;

import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hesam on 5/12/16.
 */
public class ConnectionPool
{

    private static int connectionNumber = 0;
    private static Map<Integer, ClientConnection> CLIENT_CONNECTIONS = Collections.synchronizedMap(new HashMap<Integer, ClientConnection>());

    public static void establishConnection (Socket socket, ICEPProvider provider)
    {
        connectionNumber++;
        ClientConnection tmp = new ClientConnection(provider, socket, connectionNumber);

        CLIENT_CONNECTIONS.put(connectionNumber, tmp);

        tmp.start();
    }
}

package ir.iust.cep.config;

import ir.iust.cep.data.model.EventType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesam on 9/20/16.
 */
public class ClientConfig
{

    private final static String DEFAULT_CONFIG_FILE_PATH = "src/main/resources/config/client.cfg.xml";

    public static String DEFAULT_HOST;
    public static int DEFAULT_PORT;
//    public static String SENT_RATE_LOG_PATH;
    public static Level SENT_RATE_LOG_LEVEL;

    public static List<EventType> eventTypes = new ArrayList<>();


    public static void load ()
    {
        readConfig(new File(DEFAULT_CONFIG_FILE_PATH));
    }

    public static void load (String configFilePath)
    {
        String folder = "";
        try
        {
            folder = StringUtils.substringBeforeLast(ClientConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/") + "/";
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        readConfig(new File(folder + configFilePath));
    }

    private static void readConfig (File configFile)
    {
        try
        {
            System.out.println("*** Reading Server Configuration ***");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document configDoc = documentBuilder.parse(configFile);
            configDoc.getDocumentElement().normalize();

            DEFAULT_HOST = configDoc.getElementsByTagName("defaultHost").item(0).getTextContent();
            DEFAULT_PORT = Integer.parseInt(configDoc.getElementsByTagName("defaultPort").item(0).getTextContent());
//            SENT_RATE_LOG_PATH = (configDoc.getElementsByTagName("sentRateLogPath").item(0)).getTextContent();
            SENT_RATE_LOG_LEVEL = (configDoc.getElementsByTagName("sentRateLog").item(0).getTextContent().equals("OFF")) ? Level.OFF : Level.INFO;

            NodeList eventsNode = configDoc.getElementsByTagName("event");
            for (int i = 0; i < eventsNode.getLength(); i++)
            {
                Node event = eventsNode.item(i);
                if (event.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eventElement = (Element) event;
                    String name = eventElement.getElementsByTagName("eventTypeName").item(0).getTextContent();
                    int rate = Integer.parseInt(eventElement.getElementsByTagName("eventRate").item(0).getTextContent());
                    eventTypes.add(new EventType(name, rate));
                }
            }

            System.out.println("*** Reading Server Configuration has been Finished ***");
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
    {
        ClientConfig.load();
        System.out.println(eventTypes.size());
    }
}

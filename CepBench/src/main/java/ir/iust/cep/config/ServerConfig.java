package ir.iust.cep.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Hesam on 9/20/16.
 */
public class ServerConfig
{

    private final static String DEFAULT_CONFIG_FILE_PATH = "src/main/resources/config/server.cfg.xml";

    public static int DEFAULT_PORT;
    public static String ENGINE_NAME;
    public static String RULES_PATH;
    public static int RULES_NUMBER;

    public static Level RECEIVE_RATE_LOG_LEVEL;
    public static Level THROUGHPUT_LOG_LEVEL;
    public static Level LATENCY_LOG_LEVEL;

    public static void load ()
    {
        readConfig(new File(DEFAULT_CONFIG_FILE_PATH));
    }

    public static void load (String configFilePath)
    {
        String folder = "";
        try
        {
            folder = StringUtils.substringBeforeLast(ServerConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/") + "/";
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

            DEFAULT_PORT = Integer.parseInt(configDoc.getElementsByTagName("defaultPort").item(0).getTextContent());
            ENGINE_NAME = configDoc.getElementsByTagName("engineName").item(0).getTextContent();
            RULES_PATH = configDoc.getElementsByTagName("rulesPath").item(0).getTextContent();
            RULES_NUMBER = Integer.parseInt(configDoc.getElementsByTagName("rulesNumber").item(0).getTextContent());
            RECEIVE_RATE_LOG_LEVEL = (configDoc.getElementsByTagName("receiveRateLog").item(0).getTextContent().equals("OFF")) ? Level.OFF : Level.INFO;
            THROUGHPUT_LOG_LEVEL = (configDoc.getElementsByTagName("throughput").item(0).getTextContent().equals("OFF")) ? Level.OFF : Level.INFO;
            LATENCY_LOG_LEVEL = (configDoc.getElementsByTagName("latency").item(0).getTextContent().equals("OFF")) ? Level.OFF : Level.INFO;


            System.out.println("*** Reading Server Configuration has been Finished ***");
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
    {
        ServerConfig.load();
        System.out.println(DEFAULT_PORT);
    }
}

package ir.iust.cep.config;

import org.apache.commons.lang3.StringUtils;
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
public class AppConfig
{

    private final static String DEFAULT_CONFIG_FILE_PATH = "src/main/resources/config/app.cfg.xml";

    public static String SERIALIZATION_TYPE;
    public static String EVENT_MODEL;

    public static void load ()
    {
        readConfig(new File(DEFAULT_CONFIG_FILE_PATH));
    }

    public static void load (String configFilePath)
    {
        String folder = "";
        try
        {
            folder = StringUtils.substringBeforeLast(AppConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/") + "/";
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
            System.out.println("*** Reading Application Configuration ***");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document configDoc = documentBuilder.parse(configFile);
            configDoc.getDocumentElement().normalize();

            SERIALIZATION_TYPE = configDoc.getElementsByTagName("serializationType").item(0).getTextContent();
            EVENT_MODEL = configDoc.getElementsByTagName("eventModel").item(0).getTextContent();

            System.out.println("*** Reading Application Configuration has been Finished ***");
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
    {
        AppConfig.load();
        System.out.println(SERIALIZATION_TYPE);
    }
}

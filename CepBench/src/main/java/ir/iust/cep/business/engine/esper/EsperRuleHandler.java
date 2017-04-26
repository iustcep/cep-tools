package ir.iust.cep.business.engine.esper;

import ir.iust.cep.config.ServerConfig;
import ir.iust.cep.data.model.Rule;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Hesam on 9/25/16.
 */
public class EsperRuleHandler
{

    public final static Properties prop = new Properties();

    private final static String RULE_PATH = ServerConfig.RULES_PATH + "rules.erl";
    public static int rulesNumber;
    public static Rule[] rules;

    static
    {
        try
        {
            prop.load(new FileInputStream(RULE_PATH));
//            rulesNumber = Integer.parseInt(prop.getProperty("RULES_NUMBER"));
            rulesNumber = ServerConfig.RULES_NUMBER;
            rules = new Rule[rulesNumber];

            for (int i = 0; i < rulesNumber; i++)
            {
                rules[i] = new Rule((i + 1), "RULE_" + (i + 1));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

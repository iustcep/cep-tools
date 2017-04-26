package ir.iust.cep.business.engine.drools;

import ir.iust.cep.config.ServerConfig;
import ir.iust.cep.data.model.Rule;

/**
 * Created by Hesam on 9/25/16.
 */
public class DroolsRuleHandler
{

    public final static int rulesNumber;
    public final static Rule[] rules;

    static
    {
        rulesNumber = ServerConfig.RULES_NUMBER;
        rules = new Rule[rulesNumber];
        for (int i = 0; i < rulesNumber; i++)
        {
            String addr = ServerConfig.RULES_PATH + (i + 1) + ".drl";
            rules[i] = new Rule(i + 1, addr);
        }
    }
}

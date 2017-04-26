package ir.iust.cep.business.engine;

import ir.iust.cep.business.engine.drools.Drools;
import ir.iust.cep.business.engine.drools.DroolsRuleHandler;
import ir.iust.cep.business.engine.esper.Esper;
import ir.iust.cep.business.engine.esper.EsperRuleHandler;

/**
 * Created by Hesam on 9/24/16.
 */
public class EngineFactory
{

    public static ICEPProvider getCepProvider (String engineName)
    {
        if (engineName.compareTo("DROOLS") == 0)
        {
            String className = Drools.class.getName();
            try
            {
                Class<?> aClass = Class.forName(className);
                return (ICEPProvider) aClass.newInstance();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else if (engineName.compareTo("ESPER") == 0)
        {
            String className = Esper.class.getName();
            try
            {
                Class<?> aClass = Class.forName(className);
                return (ICEPProvider) aClass.newInstance();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static int getRulesNumber (String engineName)
    {
        if (engineName.compareTo("DROOLS") == 0)
        {
            return DroolsRuleHandler.rulesNumber;
        }
        else if (engineName.compareTo("ESPER") == 0)
        {
            return EsperRuleHandler.rulesNumber;
        }
        return -1;
    }
}

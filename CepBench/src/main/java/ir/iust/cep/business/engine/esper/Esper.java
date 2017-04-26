package ir.iust.cep.business.engine.esper;

import ir.iust.cep.business.engine.ICEPProvider;
import ir.iust.cep.config.AppConfig;
import ir.iust.cep.data.model.EventJava;
import com.espertech.esper.client.*;
import ir.iust.cep.data.gen.Generated;

/**
 * Created by Hesam on 9/25/16.
 */
public class Esper implements ICEPProvider
{

    private EPAdministrator epAdministrator;
    private EPRuntime epRuntime;
    private Configuration configuration;
    private Subscribers subscribers;

    @Override
    public void init ()
    {
        this.engineConfiguration();
        this.addEvents();
        EPServiceProvider epServiceProvider = EPServiceProviderManager.getProvider("ESPER", this.configuration);
        this.epAdministrator = epServiceProvider.getEPAdministrator();
        this.epRuntime = epServiceProvider.getEPRuntime();
        this.subscribers = new Subscribers();
        this.registerStatement();
    }

    @Override
    public void sendEvent (Object event)
    {
        this.epRuntime.sendEvent(event);
    }

    private void engineConfiguration ()
    {
        this.configuration = new Configuration();
        this.configuration.getEngineDefaults().getTimeSource().setTimeSourceType(ConfigurationEngineDefaults.TimeSourceType.NANO);
    }

    private void addEvents ()
    {
        if (AppConfig.SERIALIZATION_TYPE.equals("JAVA"))
        {
            this.configuration.addEventType("event", EventJava.class);
        }
        else if (AppConfig.SERIALIZATION_TYPE.equals("PROTOCOL_BUFFER"))
        {
            this.configuration.addEventType("event", Generated.EventProtocolBuffer.class);
        }
    }

    private void registerStatement ()
    {
        for (int i = 0; i < EsperRuleHandler.rulesNumber; i++)
        {

            EPStatement stmt = this.epAdministrator.createEPL(EsperRuleHandler.prop.getProperty(EsperRuleHandler.rules[i].getAddress()));
            System.out.println("Rule number " + (i + 1) + " register");
            this.setSubscribers(stmt, (i + 1));
        }
    }

    private void setSubscribers (EPStatement statement, int statementId)
    {
        statement.setSubscriber(this.subscribers, "update" + statementId);
    }
}

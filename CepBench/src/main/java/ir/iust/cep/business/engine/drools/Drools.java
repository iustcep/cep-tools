package ir.iust.cep.business.engine.drools;

import ir.iust.cep.business.engine.ICEPProvider;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

/**
 * Created by Hesam on 9/25/16.
 */
public class Drools implements ICEPProvider
{
    private StatefulKnowledgeSession knowledgeSession;
    private WorkingMemoryEntryPoint entryPointOne;


    @Override
    public void init ()
    {
        System.out.println("*** Initializing the engine has been started");
        this.knowledgeSession = this.createKnowledgeBase().newStatefulKnowledgeSession();

        this.entryPointOne = this.knowledgeSession.getWorkingMemoryEntryPoint("ONE");
    }

    @Override
    public void sendEvent (Object event)
    {
        this.entryPointOne.insert(event);
        this.knowledgeSession.fireAllRules();
    }

    private KnowledgeBase createKnowledgeBase ()
    {
        KnowledgeBuilder builder;
        builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        for (int i = 0; i < DroolsRuleHandler.rulesNumber; i++)
        {
            builder.add(ResourceFactory.newFileResource(DroolsRuleHandler.rules[i].getAddress()), ResourceType.DRL);
            System.out.println("Rule number " + (i + 1) + " register");
        }
        if (builder.hasErrors())
        {
            System.out.println(builder.getErrors());
        }

        KnowledgeBaseConfiguration configuration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        configuration.setOption(org.drools.conf.EventProcessingOption.STREAM);

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(configuration);
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
        return knowledgeBase;
    }
}

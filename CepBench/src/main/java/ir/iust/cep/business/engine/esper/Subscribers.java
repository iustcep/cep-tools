package ir.iust.cep.business.engine.esper;

import ir.iust.cep.monitor.server.Throughput;

/**
 * Created by Hesam on 9/25/16.
 */
public class Subscribers
{

    public void update1 ()
    {
        Throughput.doCalculate(1);
    }

    public void update2 ()
    {
        Throughput.doCalculate(2);
    }

    public void update3 ()
    {
        Throughput.doCalculate(3);
    }

    public void update4 ()
    {
        Throughput.doCalculate(4);
    }

    public void update5 ()
    {
        Throughput.doCalculate(5);

    }
}

package ir.iust.cep.business.engine;

/**
 * Created by Hesam on 9/24/16.
 */
public interface ICEPProvider
{

    void init();
    void sendEvent(Object event);
}

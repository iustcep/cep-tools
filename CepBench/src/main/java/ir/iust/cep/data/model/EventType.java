package ir.iust.cep.data.model;

/**
 * Created by Hesam on 9/20/16.
 */
public class EventType
{

    private String name;
    private int rate;

    public EventType (String name, int rate)
    {
        this.name = name;
        this.rate = rate;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public int getRate ()
    {
        return rate;
    }

    public void setRate (int rate)
    {
        this.rate = rate;
    }
}

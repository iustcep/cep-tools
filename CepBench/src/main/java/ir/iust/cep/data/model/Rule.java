package ir.iust.cep.data.model;

/**
 * Created by Hesam on 9/25/16.
 */
public class Rule
{

    private int id;
    private String address;

    public Rule (int id, String address)
    {
        this.id = id;
        this.address = address;
    }

    public int getId ()
    {
        return id;
    }

    public String getAddress ()
    {
        return address;
    }
}

package ir.iust.cep.utility;

import java.util.Random;

/**
 * Created by Hesam on 9/20/16.
 */
public class RandomOperation
{

    private final static int MAXIMUM_VALUE = 100;
    private static Random RAND = new Random();

    public static int getRandomValue ()
    {
        return RAND.nextInt(MAXIMUM_VALUE);
    }
}

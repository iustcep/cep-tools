package ir.iust.cep.test;

import ir.iust.cep.data.gen.Generated;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Hesam on 9/24/16.
 */

public class ProtocolBuffer
{


    private static void write ()
    {
        Generated.EventProtocolBuffer.MapFieldEntry entry1 = Generated.EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey("test1")
                .setValue("test1")
                .build();

        Generated.EventProtocolBuffer.MapFieldEntry entry2 = Generated.EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey("test2")
                .setValue("test2")
                .build();

        Generated.EventProtocolBuffer event = Generated.EventProtocolBuffer
                .newBuilder()
                .setType("type")
                .addItems(entry1)
                .addItems(entry2)
                .build();

        try
        {
            FileOutputStream outputStream = new FileOutputStream("target/test.ser");
            event.writeTo(outputStream);
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void read ()
    {
        try
        {
            Generated.EventProtocolBuffer event = Generated.EventProtocolBuffer.parseFrom(new FileInputStream("target/test.ser"));
//            System.out.println(event.getFiledValue(1));
//            System.out.println(event);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
    {

        write();
        read();
    }
}

package ir.iust.cep.business.client;

import ir.iust.cep.data.gen.Generated.EventProtocolBuffer;
import ir.iust.cep.data.model.EventJava;
import ir.iust.cep.utility.RandomOperation;

import java.util.ArrayList;


/**
 * Created by Hesam on 9/24/16.
 */
public class EventProducer
{

    private final static String EVENT_ID_FILED_KEY = "id";
    private final static String EVENT_PRICE_FILED_KEY = "price";
    private final static String EVENT_WEIGHT_FILED_KEY = "weight";
    private final static String EVENT_COUNT_FILED_KEY = "count";
    private final static String EVENT_DISCOUNT_FILED_KEY = "discount";
    private final static String EVENT_TIME_STAMP_FILED_KEY = "time_stamp";


    public static EventJava createJavaEvent (String eventType)
    {
        EventJava event = new EventJava(eventType, new ArrayList<>());
        EventJava.Item itemId = new EventJava.Item(EVENT_ID_FILED_KEY, String.valueOf(RandomOperation.getRandomValue()));
        EventJava.Item itemPrice = new EventJava.Item(EVENT_PRICE_FILED_KEY, String.valueOf(RandomOperation.getRandomValue()));
        EventJava.Item itemWeight = new EventJava.Item(EVENT_WEIGHT_FILED_KEY, String.valueOf(RandomOperation.getRandomValue()));
        EventJava.Item itemCount = new EventJava.Item(EVENT_COUNT_FILED_KEY, String.valueOf(RandomOperation.getRandomValue()));
        EventJava.Item itemDiscount = new EventJava.Item(EVENT_DISCOUNT_FILED_KEY, String.valueOf(RandomOperation.getRandomValue()));
        EventJava.Item itemTimeStamp = new EventJava.Item(EVENT_TIME_STAMP_FILED_KEY, String.valueOf(RandomOperation.getRandomValue()));

        event.getItems().add(itemId);
        event.getItems().add(itemPrice);
        event.getItems().add(itemWeight);
        event.getItems().add(itemCount);
        event.getItems().add(itemDiscount);
        event.getItems().add(itemTimeStamp);

        return event;
    }

    public static EventProtocolBuffer createEventProtocolBuffer (String eventType)
    {
        EventProtocolBuffer.MapFieldEntry itemId = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_ID_FILED_KEY)
                .setValue(String.valueOf(RandomOperation.getRandomValue()))
                .build();
        EventProtocolBuffer.MapFieldEntry itemPrice = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_PRICE_FILED_KEY)
                .setValue(String.valueOf(RandomOperation.getRandomValue()))
                .build();
        EventProtocolBuffer.MapFieldEntry itemWeight = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_WEIGHT_FILED_KEY)
                .setValue(String.valueOf(RandomOperation.getRandomValue()))
                .build();
        EventProtocolBuffer.MapFieldEntry itemCount = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_COUNT_FILED_KEY)
                .setValue(String.valueOf(RandomOperation.getRandomValue()))
                .build();
        EventProtocolBuffer.MapFieldEntry itemDiscount = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_DISCOUNT_FILED_KEY)
                .setValue(String.valueOf(RandomOperation.getRandomValue()))
                .build();
        EventProtocolBuffer.MapFieldEntry itemTimeStamp = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_TIME_STAMP_FILED_KEY)
                .setValue("")
                .build();

        return EventProtocolBuffer.newBuilder()
                .setType(eventType)
                .addItems(itemId)
                .addItems(itemPrice)
                .addItems(itemWeight)
                .addItems(itemCount)
                .addItems(itemDiscount)
                .addItems(itemTimeStamp)
                .build();
    }

//    public static EventProtocolBuffer createComplexEvent (String eventType)
//    {
//        EventProtocolBuffer.MapFieldEntry itemTimeStamp = EventProtocolBuffer.MapFieldEntry.newBuilder()
//                .setKey(EVENT_TIME_STAMP_FILED_KEY)
//                .setValue(System.currentTimeMillis() + "")
//                .build();
//
//        return EventProtocolBuffer.newBuilder()
//                .setType(eventType)
//                .addItems(itemTimeStamp)
//                .build();
//    }

    /*public static EventProtocolBuffer createEventProtocolBuffer
            (String eventType, String id, String price, String weight, String count, String discount)
    {
        EventProtocolBuffer.MapFieldEntry itemId = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_ID_FILED_KEY)
                .setValue(id)
                .build();
        EventProtocolBuffer.MapFieldEntry itemPrice = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_PRICE_FILED_KEY)
                .setValue(price)
                .build();
        EventProtocolBuffer.MapFieldEntry itemWeight = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_WEIGHT_FILED_KEY)
                .setValue(weight)
                .build();
        EventProtocolBuffer.MapFieldEntry itemCount = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_COUNT_FILED_KEY)
                .setValue(count)
                .build();
        EventProtocolBuffer.MapFieldEntry itemDiscount = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_DISCOUNT_FILED_KEY)
                .setValue(discount)
                .build();
        EventProtocolBuffer.MapFieldEntry itemTimeStamp = EventProtocolBuffer.MapFieldEntry.newBuilder()
                .setKey(EVENT_TIME_STAMP_FILED_KEY)
                .setValue(System.currentTimeMillis() + "")
                .build();

        return EventProtocolBuffer.newBuilder()
                .setType(eventType)
                .addItems(itemId)
                .addItems(itemPrice)
                .addItems(itemWeight)
                .addItems(itemCount)
                .addItems(itemDiscount)
                .addItems(itemTimeStamp)
                .build();
    }*/
}

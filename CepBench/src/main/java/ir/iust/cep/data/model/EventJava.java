package ir.iust.cep.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hesam on 9/24/16.
 */
public class EventJava implements Serializable
{

    private String eventType;
    private List<Item> items;

    public EventJava (String eventType, List<Item> items)
    {
        this.eventType = eventType;
        this.items = items;
    }

    public String getEventType ()
    {
        return eventType;
    }

    public void setEventType (String eventType)
    {
        this.eventType = eventType;
    }

    public List<Item> getItems ()
    {
        return items;
    }

    public void setItems (List<Item> items)
    {
        this.items = items;
    }

    public String getFiledValueByTag(String filedKey)
    {
        for (Item item : this.items)
        {
            if (item.getKey().compareTo(filedKey) == 0)
            {
                return item.getValue();
            }
        }
        return "";
    }

    public String getFiledValueById (int index)
    {
        if (this.items.size() <= index)
        {
            return "";
        }
        return this.items.get(index).getValue();
    }

    public void addTimeStamp()
    {
        for (Item item : this.items)
        {
            if (item.getKey().compareTo("time_stamp") == 0)
            {
                item.setValue(System.currentTimeMillis() + "");
            }
        }
    }

    public static class Item implements Serializable
    {
        private String key;
        private String value;

        public Item (String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey ()
        {
            return key;
        }

        public void setKey (String key)
        {
            this.key = key;
        }

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }
    }
}

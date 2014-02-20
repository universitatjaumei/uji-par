package es.uji.apps.par.sync.rss.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Channel
{
    private List<Item> items;

    public Channel()
    {
        items = new ArrayList<Item>();
    }

    public List<Item> getItems()
    {
        return items;
    }

    @XmlElement(name = "item")
    public void setItems(List<Item> items)
    {
        this.items = items;
    }

}

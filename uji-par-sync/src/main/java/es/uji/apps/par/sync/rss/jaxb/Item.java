package es.uji.apps.par.sync.rss.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item
{
    private String title;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}

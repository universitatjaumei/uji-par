package es.uji.apps.par.sync.rss.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Enclosure
{
    private String url;

    private String type;

    public String getUrl()
    {
        return url;
    }

    @XmlAttribute
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getType()
    {
        return type;
    }

    @XmlAttribute
    public void setType(String type)
    {
        this.type = type;
    }
}

package es.uji.apps.par.sync.rss.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sesion
{
    private String fecha;
    private String id;

    public String getFecha()
    {
        return fecha;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
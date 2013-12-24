package es.uji.apps.par.sync.rss.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sesion
{
    private String fecha;

    public String getFecha()
    {
        return fecha;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }
}
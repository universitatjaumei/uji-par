package es.uji.apps.par.sync.rss.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sesiones
{
    private List<Sesion> sesiones;

    public List<Sesion> getSesiones()
    {
        return sesiones;
    }

    @XmlElement(name = "sesion")
    public void setSesiones(List<Sesion> sesiones)
    {
        this.sesiones = sesiones;
    }

}
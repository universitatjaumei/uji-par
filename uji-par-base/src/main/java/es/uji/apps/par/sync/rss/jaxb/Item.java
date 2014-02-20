package es.uji.apps.par.sync.rss.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item
{
    private String title;
    private List<Enclosure> enclosures;
    private String contenido;
    private String seientsNumerats;
    private String tipo;
    private String companyia;
    private String duracio;
    private String resumen;
    private String contenidoId;
    private String idioma;
    private String esquema;
    private Sesiones sesiones;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<Enclosure> getEnclosures()
    {
        return enclosures;
    }

    @XmlElement(name = "enclosure")
    public void setEnclosures(List<Enclosure> enclosures)
    {
        this.enclosures = enclosures;
    }
    
    public Sesiones getSesiones()
    {
        return sesiones;
    }

    public void setSesiones(Sesiones sesiones)
    {
        this.sesiones = sesiones;
    }

    public String getContenido()
    {
        return contenido;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setContenido(String contenido)
    {
        this.contenido = contenido;
    }

    public String getSeientsNumerats()
    {
        return seientsNumerats;
    }

    @XmlElement(name = "seientsnumerats", namespace = "http://www.uji.es/namespaces/rss#")
    public void setSeientsNumerats(String seientsNumerats)
    {
        this.seientsNumerats = seientsNumerats;
    }

    public String getTipo()
    {
        return tipo;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getCompanyia()
    {
        return companyia;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setCompanyia(String companyia)
    {
        this.companyia = companyia;
    }

    public String getDuracio()
    {
        return duracio;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setDuracio(String duracio)
    {
        this.duracio = duracio;
    }

    public String getResumen()
    {
        return resumen;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setResumen(String resumen)
    {
        this.resumen = resumen;
    }

    public String getContenidoId()
    {
        return contenidoId;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setContenidoId(String contenidoId)
    {
        this.contenidoId = contenidoId;
    }

    public String getIdioma()
    {
        return idioma;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setIdioma(String idioma)
    {
        this.idioma = idioma;
    }

    public String getEsquema()
    {
        return esquema;
    }

    @XmlElement(namespace = "http://www.uji.es/namespaces/rss#")
    public void setEsquema(String esquema)
    {
        this.esquema = esquema;
    }
}

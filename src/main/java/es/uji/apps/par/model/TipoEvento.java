package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.TipoEventoDTO;

@XmlRootElement
public class TipoEvento
{
    private long id;
    private String nombreEs;
    private String nombreVa;

    public TipoEvento()
    {
    }

    public TipoEvento(long id)
    {
        this.id = id;
    }

    public TipoEvento(TipoEventoDTO tipoEventoDTO)
    {
        this.id = tipoEventoDTO.getId();
        this.nombreEs = tipoEventoDTO.getNombreEs();
        this.nombreVa = tipoEventoDTO.getNombreVa();
    }

    public TipoEvento(String nombreEs)
    {
        this.nombreEs = nombreEs;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getNombreEs()
    {
        return nombreEs;
    }

    public void setNombreEs(String nombreEs)
    {
        this.nombreEs = nombreEs;
    }

    public String getNombreVa()
    {
        return nombreVa;
    }

    public void setNombreVa(String nombreVa)
    {
        this.nombreVa = nombreVa;
    }

}
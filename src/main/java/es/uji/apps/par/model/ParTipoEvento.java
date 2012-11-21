package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ParTipoEventoDTO;

@XmlRootElement
public class ParTipoEvento
{
    private long id;
    private String nombreEs;
    private String nombreEn;
    private String nombreVa;

    public ParTipoEvento()
    {
    }
    
    public ParTipoEvento(long id) {
    	this.id = id;
    }
    
    public ParTipoEvento(ParTipoEventoDTO tipoEventoDTO)
    {
        this.id = tipoEventoDTO.getId();
        this.nombreEs = tipoEventoDTO.getNombreEs();
        this.nombreEn = tipoEventoDTO.getNombreEn();
        this.nombreVa = tipoEventoDTO.getNombreVa();
    }
    
    public ParTipoEvento(String nombreEs) {
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

	public String getNombreEs() {
		return nombreEs;
	}

	public void setNombreEs(String nombreEs) {
		this.nombreEs = nombreEs;
	}

	public String getNombreEn() {
		return nombreEn;
	}

	public void setNombreEn(String nombreEn) {
		this.nombreEn = nombreEn;
	}

	public String getNombreVa() {
		return nombreVa;
	}

	public void setNombreVa(String nombreVa) {
		this.nombreVa = nombreVa;
	}
    
    
}
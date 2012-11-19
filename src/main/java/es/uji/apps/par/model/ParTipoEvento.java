package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ParTipoEventoDTO;

@XmlRootElement
public class ParTipoEvento
{
    private long id;
    private String nombre;

    public ParTipoEvento()
    {
    }
    
    public ParTipoEvento(long id) {
    	this.id = id;
    }
    
    public ParTipoEvento(ParTipoEventoDTO tipoEventoDTO)
    {
        this.id = tipoEventoDTO.getId();
        this.nombre = tipoEventoDTO.getNombre();
    }
    
    public ParTipoEvento(String nombre) {
    	this.nombre = nombre;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
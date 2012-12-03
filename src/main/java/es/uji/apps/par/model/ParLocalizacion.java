package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ParLocalizacionDTO;

@XmlRootElement
public class ParLocalizacion
{
    private long id;
    private String nombre;
    private int totalEntradas;

    public ParLocalizacion()
    {
    }
    
    public ParLocalizacion(long id) {
    	this.id = id;
    }
    
    public ParLocalizacion(ParLocalizacionDTO localizacionDTO)
    {
        this.id = localizacionDTO.getId();
        this.nombre = localizacionDTO.getNombre();
        this.totalEntradas = localizacionDTO.getTotalEntradas().intValue();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTotalEntradas() {
		return totalEntradas;
	}

	public void setTotalEntradas(int total_entradas) {
		this.totalEntradas = total_entradas;
	}
       
    
}
package es.uji.apps.par.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.LocalizacionDTO;

@XmlRootElement
public class Localizacion
{
    private long id;
    private String nombreEs;
    private String nombreVa;
    private int totalEntradas;

    public static Localizacion localizacionDTOtoLocalizacion(LocalizacionDTO localizacionDTO) {
    	Localizacion localizacion = new Localizacion();
    	localizacion.setId(localizacionDTO.getId());
    	localizacion.setNombreEs(localizacionDTO.getNombreEs());
    	localizacion.setNombreVa(localizacionDTO.getNombreVa());
    	localizacion.setTotalEntradas(localizacionDTO.getTotalEntradas().intValue());
    	
    	return localizacion;
    }
    
    public static LocalizacionDTO localizacionToLocalizacionDTO(Localizacion localizacion) {
    	LocalizacionDTO localizacionDTO = new LocalizacionDTO();
    	localizacionDTO.setId(localizacion.getId());
    	localizacionDTO.setNombreEs(localizacion.getNombreEs());
    	localizacionDTO.setNombreVa(localizacion.getNombreVa());
    	localizacionDTO.setTotalEntradas(new BigDecimal(localizacion.getTotalEntradas()));
    	
    	return localizacionDTO;
    }
    
    public Localizacion()
    {
    }
    
    public Localizacion(long id)
    {
        this.id = id;
    }

    public Localizacion(LocalizacionDTO localizacionDTO)
    {
        this.id = localizacionDTO.getId();
        this.nombreEs = localizacionDTO.getNombreEs();
        this.nombreVa = localizacionDTO.getNombreVa();
        this.totalEntradas = localizacionDTO.getTotalEntradas().intValue();
    }

    public Localizacion(String nombreEs)
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

    public int getTotalEntradas()
    {
        return totalEntradas;
    }

    public void setTotalEntradas(int total_entradas)
    {
        this.totalEntradas = total_entradas;
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
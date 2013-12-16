package es.uji.apps.par.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.utils.DateUtils;

@XmlRootElement
public class Sesion
{
    private long id;
    private Evento evento;
    private Date fechaCelebracion;
    private Date fechaInicioVentaOnline;
    private Date fechaFinVentaOnline;
    private String horaApertura;
    private Boolean canalInternet;
    private Boolean canalTaquilla;
    private String horaCelebracion;
    private String horaInicioVentaOnline;
    private String horaFinVentaOnline;
    private Plantilla plantillaPrecios;
    private List<PreciosSesion> preciosSesion;
    private long butacasVendidas;
    private String nombre;
    private String formato;
    private Sala sala;

    public Sesion()
    {

    }

    public Sesion(SesionDTO sesionDTO)
    {
        this.id = sesionDTO.getId();
        this.evento = Evento.eventoDTOtoEvento(sesionDTO.getParEvento());
        this.fechaCelebracion = new Date(sesionDTO.getFechaCelebracion().getTime());
        this.fechaInicioVentaOnline = new Date(
                sesionDTO.getFechaInicioVentaOnline().getTime());
        this.fechaFinVentaOnline = new Date(sesionDTO.getFechaFinVentaOnline().getTime());
        this.horaApertura = sesionDTO.getHoraApertura();
        this.canalInternet = sesionDTO.getCanalInternet();
        this.canalTaquilla = sesionDTO.getCanalTaquilla();
        this.plantillaPrecios = Plantilla.plantillaPreciosDTOtoPlantillaPrecios(sesionDTO.getParPlantilla());

        this.horaCelebracion = DateUtils.getHourAndMinutesWithLeadingZeros(sesionDTO.getFechaCelebracion());
        this.horaInicioVentaOnline = DateUtils.getHourAndMinutesWithLeadingZeros(sesionDTO.getFechaInicioVentaOnline());
        this.horaFinVentaOnline = DateUtils.getHourAndMinutesWithLeadingZeros(sesionDTO.getFechaFinVentaOnline());
        
        this.nombre = sesionDTO.getNombre();
        this.formato = sesionDTO.getFormato();
        
        if (sesionDTO.getParSala() != null)
            this.sala = new Sala(sesionDTO.getParSala());
    }
    
    public static List<Long> getIdsSesiones(List<Sesion> sesiones)
    {
        List<Long> ids = new ArrayList<Long>();
        
        for (Sesion sesion : sesiones)
            ids.add(sesion.getId());
        
        return ids ;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Evento getEvento()
    {
        return evento;
    }

    public void setEvento(Evento evento)
    {
        this.evento = evento;
    }

    public Date getFechaCelebracion()
    {
        return fechaCelebracion;
    }

    public Date getFechaInicioVentaOnline()
    {
        return fechaInicioVentaOnline;
    }

    public Date getFechaFinVentaOnline()
    {
        return fechaFinVentaOnline;
    }

    public String getHoraApertura()
    {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura)
    {
        this.horaApertura = horaApertura;
    }

    public Boolean getCanalInternet()
    {
        return canalInternet;
    }

    public void setCanalInternet(String canalInternet)
    {
        this.canalInternet = (canalInternet != null && canalInternet.equals("1"));
    }

    public Boolean getCanalTaquilla()
    {
        return canalTaquilla;
    }

    public void setCanalTaquilla(String canalTaquilla)
    {
        this.canalTaquilla = (canalTaquilla != null && canalTaquilla.equals("1"));
    }

    // TODO
    // definidos porque desde el ext se envía automaticamente la fecha con formato dd/mm/YYYY y no
    // serializa bien si el parametro es un Date
    public void setFechaCelebracion(String fechaCelebracion)
    {
        this.fechaCelebracion = DateUtils.spanishStringToDate(fechaCelebracion);
    }

    public void setFechaCelebracionWithDate(Date fechaCelebracion)
    {
        this.fechaCelebracion = fechaCelebracion;
    }

    public void setFechaInicioVentaOnline(String fechaInicioVentaOnline)
    {
        this.fechaInicioVentaOnline = DateUtils.spanishStringToDate(fechaInicioVentaOnline);
    }
    
    public void setFechaInicioVentaOnlineWithDate(Date fechaInicioVentaOnline)
    {
        this.fechaInicioVentaOnline = fechaInicioVentaOnline;
    }

    public void setFechaFinVentaOnline(String fechaFinVentaOnline)
    {
        this.fechaFinVentaOnline = DateUtils.spanishStringToDate(fechaFinVentaOnline);
    }
    
    public void setFechaFinVentaOnlineWithDate(Date fechaFinVentaOnline)
    {
        this.fechaFinVentaOnline = fechaFinVentaOnline;
    }

    public String getHoraCelebracion()
    {
        return horaCelebracion;
    }

    public void setHoraCelebracion(String horaCelebracion)
    {
        this.horaCelebracion = horaCelebracion;
    }

	public static Sesion SesionDTOToSesion(SesionDTO sesionDTO) {
		Sesion sesion = new Sesion();
		
		if (sesionDTO.getCanalInternet() != null)
		    sesion.setCanalInternet(sesionDTO.getCanalInternet().toString());
		
		if (sesionDTO.getCanalTaquilla() != null) 
		    sesion.setCanalTaquilla(sesionDTO.getCanalTaquilla().toString());
		
		sesion.setEvento(Evento.eventoDTOtoEvento(sesionDTO.getParEvento()));
		sesion.setFechaCelebracionWithDate(sesionDTO.getFechaCelebracion());
		sesion.setFechaFinVentaOnline(DateUtils.dateToSpanishString(sesionDTO.getFechaFinVentaOnline()));
		sesion.setFechaInicioVentaOnline(DateUtils.dateToSpanishString(sesionDTO.getFechaInicioVentaOnline()));
		
		sesion.setHoraApertura(sesionDTO.getHoraApertura());
		sesion.setHoraInicioVentaOnline(DateUtils.getHourAndMinutesWithLeadingZeros(sesionDTO.getFechaInicioVentaOnline()));
		sesion.setHoraFinVentaOnline(DateUtils.getHourAndMinutesWithLeadingZeros(sesionDTO.getFechaFinVentaOnline()));
		
		sesion.setHoraCelebracion(DateUtils.getHourAndMinutesWithLeadingZeros(sesionDTO.getFechaCelebracion()));
		sesion.setId(sesionDTO.getId());
		sesion.setPlantillaPrecios(Plantilla.plantillaPreciosDTOtoPlantillaPrecios(sesionDTO.getParPlantilla()));
		
		sesion.setNombre(sesionDTO.getNombre());
		sesion.setFormato(sesionDTO.getFormato());
		
		sesion.setSala(new Sala(sesionDTO.getParSala()));
		
		return sesion;
	}
	
	public static Sesion SesionDTOToSesionSinEvento(SesionDTO sesionDTO) {
	    Sesion sesion = SesionDTOToSesion(sesionDTO);
	    sesion.setEvento(null);
	    
	    return sesion;
	}
	
	public static SesionDTO SesionToSesionDTO(Sesion sesion) {
		SesionDTO sesionDTO = new SesionDTO();
		sesionDTO.setCanalInternet(true);
        sesionDTO.setCanalTaquilla(true);
		/*sesionDTO.setCanalInternet(sesion.getCanalInternet());
		sesionDTO.setCanalTaquilla(sesion.getCanalTaquilla());*/
		sesionDTO.setParEvento(Evento.eventoToEventoDTO(sesion.getEvento()));
		sesionDTO.setFechaCelebracion(DateUtils.dateToTimestampSafe(sesion.getFechaCelebracion()));
		sesionDTO.setFechaFinVentaOnline(DateUtils.dateToTimestampSafe(sesion.getFechaFinVentaOnline()));
		sesionDTO.setFechaInicioVentaOnline(DateUtils.dateToTimestampSafe(sesion.getFechaInicioVentaOnline()));
		
		sesionDTO.setHoraApertura(sesion.getHoraApertura());
		sesionDTO.setId(sesion.getId());
		sesionDTO.setParPlantilla(Plantilla.plantillaPreciosToPlantillaPreciosDTO(sesion.getPlantillaPrecios()));
		
		if (sesion.getPreciosSesion() != null) {
			for (PreciosSesion preciosSesion: sesion.getPreciosSesion()) {
				sesionDTO.addParPreciosSesion(PreciosSesion.precioSesionToPrecioSesionDTO(preciosSesion));
			}
		}
		
		if (sesion.getSala() != null && sesion.getSala().getId()!=0)
		    sesionDTO.setParSala(Sala.salaToSalaDTO(sesion.getSala()));
		
		return sesionDTO;
	}
	
	public static List<SesionDTO> sesionsToSesionsDTO(List<Sesion> sesiones)
	{
	    List<SesionDTO> sesionesDTO = new ArrayList<SesionDTO>();
	    
	    for (Sesion sesion: sesiones)
        {
            sesionesDTO.add(Sesion.SesionToSesionDTO(sesion));
        }
	    
        return sesionesDTO;
	}
	
	public Plantilla getPlantillaPrecios() {
		return plantillaPrecios;
	}

	public void setPlantillaPrecios(Plantilla plantillaPrecios) {
		this.plantillaPrecios = plantillaPrecios;
	}

	public String getHoraInicioVentaOnline() {
		return horaInicioVentaOnline;
	}

	public void setHoraInicioVentaOnline(String horaInicioVentaOnline) {
		this.horaInicioVentaOnline = horaInicioVentaOnline;
	}

	public String getHoraFinVentaOnline() {
		return horaFinVentaOnline;
	}

	public void setHoraFinVentaOnline(String horaFinVentaOnline) {
		this.horaFinVentaOnline = horaFinVentaOnline;
	}

	public List<PreciosSesion> getPreciosSesion() {
		return preciosSesion;
	}

	@JsonIgnore
	public void setPreciosSesion(List<PreciosSesion> preciosSesion) {
		this.preciosSesion = preciosSesion;
	}
	
	@JsonProperty("preciosSesion")
	public void setPreciosSesionFromString(String preciosSesion) {
		Gson gson = new Gson();
		List<PreciosSesion> lista = gson.fromJson(preciosSesion, new TypeToken<List<PreciosSesion>>(){}.getType());
		this.preciosSesion = lista;
	}
	
	@JsonIgnore
	public boolean getEnPlazoVentaInternet()
    {
        Date ahora = new Date();

        return ahora.before(getFechaFinVentaOnline()) && ahora.after(fechaInicioVentaOnline);
    }

    public long getButacasVendidas()
    {
        return butacasVendidas;
    }

    public void setButacasVendidas(long butacasVendidas)
    {
        this.butacasVendidas = butacasVendidas;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getFormato()
    {
        return formato;
    }

    public void setFormato(String formato)
    {
        this.formato = formato;
    }

    public Sala getSala()
    {
        return sala;
    }

    public void setSala(Sala sala)
    {
        this.sala = sala;
    }
}
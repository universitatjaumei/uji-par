package es.uji.apps.par.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.SesionDTO;

@XmlRootElement
public class PreciosSesion {

	private long id;
	private Localizacion localizacion;
	private Sesion sesion;
	private BigDecimal descuento;
	private BigDecimal invitacion;
	private BigDecimal precio;
	
	public PreciosSesion() {
		
	}
	
	public PreciosSesion(PreciosPlantilla preciosPlantilla) {
        this.id = preciosPlantilla.getId();
        this.localizacion = preciosPlantilla.getLocalizacion();
        this.descuento = preciosPlantilla.getDescuento();
        this.invitacion = preciosPlantilla.getInvitacion();
        this.precio = preciosPlantilla.getPrecio();
    }
	
	public PreciosSesion(PreciosSesionDTO preciosSesion) {
		this.id = preciosSesion.getId();
		this.localizacion = Localizacion.localizacionDTOtoLocalizacion(preciosSesion.getParLocalizacione());
		this.sesion = Sesion.SesionDTOToSesion(preciosSesion.getParSesione());
		this.descuento = preciosSesion.getDescuento();
		this.invitacion = preciosSesion.getInvitacion();
		this.precio = preciosSesion.getPrecio();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
	
	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public BigDecimal getInvitacion() {
		return invitacion;
	}

	public void setInvitacion(BigDecimal invitacion) {
		this.invitacion = invitacion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}
	
	public static List<PreciosSesionDTO> listaPreciosSesionToListaPreciosSesionDTO(List<PreciosSesion> listaPreciosSesion) {
		List<PreciosSesionDTO> listaPreciosSesionDTO = new ArrayList<PreciosSesionDTO>();
		if (listaPreciosSesion != null) {
			for (PreciosSesion preciosSesion: listaPreciosSesion)
				listaPreciosSesionDTO.add(PreciosSesion.precioSesionToPrecioSesionDTO(preciosSesion));
		}
		
		return listaPreciosSesionDTO;
	}

	public static PreciosSesionDTO precioSesionToPrecioSesionDTO(PreciosSesion preciosSesion) {
		PreciosSesionDTO preciosSesionDTO = new PreciosSesionDTO();
		preciosSesionDTO.setDescuento(preciosSesion.getDescuento());
		preciosSesionDTO.setPrecio(preciosSesion.getPrecio());
		preciosSesionDTO.setInvitacion(preciosSesion.getInvitacion());
		
		/*LocalizacionDTO localizacionDTO = new LocalizacionDTO();
		localizacionDTO.setId(preciosSesion.getLocalizacion().getId());
		preciosSesionDTO.setParLocalizacione(localizacionDTO);*/
		
		preciosSesionDTO.setParLocalizacione(Localizacion.localizacionToLocalizacionDTO(preciosSesion.getLocalizacion()));
		
		if (preciosSesion.getSesion() != null) {
			SesionDTO sesionDTO = new SesionDTO();
			sesionDTO.setId(preciosSesion.getSesion().getId());
			preciosSesionDTO.setParSesione(sesionDTO);
		}
		
		return preciosSesionDTO;
	}
}

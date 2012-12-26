package es.uji.apps.par.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.PrecioDTO;

@XmlRootElement
public class Precio {

	private long id;
	private Localizacion localizacion;
	private PlantillaPrecios plantillaPrecios;
	private BigDecimal descuento;
	private BigDecimal invitacion;
	private BigDecimal precio;
	
	public Precio() {
		
	}
	
	public Precio(Localizacion localizacion, PlantillaPrecios plantilla) {
		this.plantillaPrecios = plantilla;
		this.localizacion = localizacion;
	}
	
	public Precio(PrecioDTO precio) {
		this.id = precio.getId();
		this.localizacion = Localizacion.localizacionDTOtoLocalizacion(precio.getParLocalizacione());
		this.plantillaPrecios = PlantillaPrecios.plantillaPreciosDTOtoPlantillaPrecios(precio.getParPlantillasPrecio());
		this.descuento = precio.getDescuento();
		this.invitacion = precio.getInvitacion();
		this.precio = precio.getPrecio();
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
	
	public PlantillaPrecios getPlantillaPrecios() {
		return plantillaPrecios;
	}

	public void setPlantillaPrecios(PlantillaPrecios plantillaPrecios) {
		this.plantillaPrecios = plantillaPrecios;
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
}

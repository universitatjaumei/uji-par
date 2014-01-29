package es.uji.apps.par.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.PreciosPlantillaDTO;

@XmlRootElement
public class PreciosPlantilla {

	private long id;
	private Localizacion localizacion;
	private Plantilla plantillaPrecios;
	private BigDecimal descuento;
	private BigDecimal invitacion;
	private BigDecimal precio;
	private BigDecimal aulaTeatro;
	private Tarifa tarifa;
	
	public PreciosPlantilla() {
		
	}
	
	public PreciosPlantilla(Localizacion localizacion, Plantilla plantilla) {
		this.plantillaPrecios = plantilla;
		this.localizacion = localizacion;
	}
	
	public PreciosPlantilla(PreciosPlantillaDTO preciosPlantilla) {
		this.id = preciosPlantilla.getId();
		this.localizacion = Localizacion.localizacionDTOtoLocalizacion(preciosPlantilla.getParLocalizacione());
		this.plantillaPrecios = Plantilla.plantillaPreciosDTOtoPlantillaPrecios(preciosPlantilla.getParPlantilla());
		this.tarifa = Tarifa.tarifaDTOToTarifa(preciosPlantilla.getParTarifa());
		this.descuento = preciosPlantilla.getDescuento();
		this.invitacion = preciosPlantilla.getInvitacion();
		this.precio = preciosPlantilla.getPrecio();
		this.aulaTeatro = preciosPlantilla.getAulaTeatro();
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
	
	public Plantilla getPlantillaPrecios() {
		return plantillaPrecios;
	}

	public void setPlantillaPrecios(Plantilla plantillaPrecios) {
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

    public BigDecimal getAulaTeatro() {
        return aulaTeatro;
    }

    public void setAulaTeatro(BigDecimal aulaTeatro) {
        this.aulaTeatro = aulaTeatro;
    }

	public Tarifa getTarifa() {
		return tarifa;
	}

	public void setTarifa(Tarifa tarifa) {
		this.tarifa = tarifa;
	}
}

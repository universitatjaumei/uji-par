package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the PAR_PRECIOS_SESION database table.
 * 
 */
@Entity
@Table(name="PAR_PRECIOS_SESION")
public class PreciosSesionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAR_PRECIOS_SESION_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_PRECIOS_SESION_ID_GENERATOR")
	private long id;

	private BigDecimal descuento;

	private BigDecimal invitacion;

	private BigDecimal precio;

	//bi-directional many-to-one association to LocalizacionDTO
	@ManyToOne
	@JoinColumn(name="LOCALIZACION_ID")
	private LocalizacionDTO parLocalizacione;

	//bi-directional many-to-one association to SesionDTO
	@ManyToOne
	@JoinColumn(name="SESION_ID")
	private SesionDTO parSesione;

	public PreciosSesionDTO() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getDescuento() {
		return this.descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public BigDecimal getInvitacion() {
		return this.invitacion;
	}

	public void setInvitacion(BigDecimal invitacion) {
		this.invitacion = invitacion;
	}

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public LocalizacionDTO getParLocalizacione() {
		return this.parLocalizacione;
	}

	public void setParLocalizacione(LocalizacionDTO parLocalizacione) {
		this.parLocalizacione = parLocalizacione;
	}

	public SesionDTO getParSesione() {
		return this.parSesione;
	}

	public void setParSesione(SesionDTO parSesione) {
		this.parSesione = parSesione;
	}

}
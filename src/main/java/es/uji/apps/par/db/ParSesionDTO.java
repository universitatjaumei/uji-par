package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the PAR_SESIONES database table.
 * 
 */
@Entity
@Table(name="PAR_SESIONES")
public class ParSesionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(name="CANAL_INTERNET")
	private BigDecimal canalInternet;

	@Column(name="CANAL_TAQUILLA")
	private BigDecimal canalTaquilla;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CELEBRACION")
	private Date fechaCelebracion;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN_VENTA_ONLINE")
	private Date fechaFinVentaOnline;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INICIO_VENTA_ONLINE")
	private Date fechaInicioVentaOnline;

	@Column(name="HORA_APERTURA")
	private String horaApertura;

	//bi-directional many-to-one association to ParEventoDTO
	@ManyToOne
	@JoinColumn(name="EVENTO_ID")
	private ParEventoDTO parEvento;

	public ParSesionDTO() {
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getCanalInternet() {
		return this.canalInternet;
	}

	public void setCanalInternet(BigDecimal canalInternet) {
		this.canalInternet = canalInternet;
	}

	public BigDecimal getCanalTaquilla() {
		return this.canalTaquilla;
	}

	public void setCanalTaquilla(BigDecimal canalTaquilla) {
		this.canalTaquilla = canalTaquilla;
	}

	public Date getFechaCelebracion() {
		return this.fechaCelebracion;
	}

	public void setFechaCelebracion(Date fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}

	public Date getFechaFinVentaOnline() {
		return this.fechaFinVentaOnline;
	}

	public void setFechaFinVentaOnline(Date fechaFinVentaOnline) {
		this.fechaFinVentaOnline = fechaFinVentaOnline;
	}

	public Date getFechaInicioVentaOnline() {
		return this.fechaInicioVentaOnline;
	}

	public void setFechaInicioVentaOnline(Date fechaInicioVentaOnline) {
		this.fechaInicioVentaOnline = fechaInicioVentaOnline;
	}

	public String getHoraApertura() {
		return this.horaApertura;
	}

	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}

	public ParEventoDTO getParEvento() {
		return this.parEvento;
	}

	public void setParEvento(ParEventoDTO parEvento) {
		this.parEvento = parEvento;
	}

}
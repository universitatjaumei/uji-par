package es.uji.apps.par.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the PAR_SESIONES database table.
 * 
 */
@Entity
@Table(name="PAR_SESIONES")
public class SesionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAR_SESIONES_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_SESIONES_ID_GENERATOR")
	private long id;

	@Column(name="CANAL_INTERNET")
	private BigDecimal canalInternet;

	@Column(name="CANAL_TAQUILLA")
	private BigDecimal canalTaquilla;

	@Column(name="FECHA_CELEBRACION")
	private Timestamp fechaCelebracion;

	@Column(name="FECHA_FIN_VENTA_ONLINE")
	private Timestamp fechaFinVentaOnline;

	@Column(name="FECHA_INICIO_VENTA_ONLINE")
	private Timestamp fechaInicioVentaOnline;

	@Column(name="HORA_APERTURA")
	private String horaApertura;

	//bi-directional many-to-one association to PreciosSesionDTO
	@OneToMany(mappedBy="parSesione", cascade=CascadeType.PERSIST)
	private List<PreciosSesionDTO> parPreciosSesions;

	//bi-directional many-to-one association to EventoDTO
	@ManyToOne
	@JoinColumn(name="EVENTO_ID")
	private EventoDTO parEvento;

	//bi-directional many-to-one association to PlantillaDTO
	@ManyToOne
	@JoinColumn(name="PLANTILLA_ID")
	private PlantillaDTO parPlantilla;
	
    //bi-directional many-to-one association to SesionDTO
    @OneToMany(mappedBy="parSesion")
    private List<ButacaDTO> parButacas;	

	public SesionDTO() {
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

	public Timestamp getFechaCelebracion() {
		return this.fechaCelebracion;
	}

	public void setFechaCelebracion(Timestamp fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}

	public Timestamp getFechaFinVentaOnline() {
		return this.fechaFinVentaOnline;
	}

	public void setFechaFinVentaOnline(Timestamp fechaFinVentaOnline) {
		this.fechaFinVentaOnline = fechaFinVentaOnline;
	}

	public Timestamp getFechaInicioVentaOnline() {
		return this.fechaInicioVentaOnline;
	}

	public void setFechaInicioVentaOnline(Timestamp fechaInicioVentaOnline) {
		this.fechaInicioVentaOnline = fechaInicioVentaOnline;
	}

	public String getHoraApertura() {
		return this.horaApertura;
	}

	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}

	public List<PreciosSesionDTO> getParPreciosSesions() {
		return this.parPreciosSesions;
	}

	public void setParPreciosSesions(List<PreciosSesionDTO> parPreciosSesions) {
		this.parPreciosSesions = parPreciosSesions;
	}
	
	public void addParPreciosSesion(PreciosSesionDTO preciosSesionDTO) {
		if (this.parPreciosSesions == null)
			this.parPreciosSesions = new ArrayList<PreciosSesionDTO>();
		
		preciosSesionDTO.setParSesione(this);
		this.parPreciosSesions.add(preciosSesionDTO);
	}
	
	public EventoDTO getParEvento() {
		return this.parEvento;
	}

	public void setParEvento(EventoDTO parEvento) {
		this.parEvento = parEvento;
	}

	public PlantillaDTO getParPlantilla() {
		return this.parPlantilla;
	}

	public void setParPlantilla(PlantillaDTO parPlantilla) {
		this.parPlantilla = parPlantilla;
	}

    public List<ButacaDTO> getParButacas() {
        return parButacas;
    }

    public void setParButacas(List<ButacaDTO> parButacas) {
        this.parButacas = parButacas;
    }
	
}
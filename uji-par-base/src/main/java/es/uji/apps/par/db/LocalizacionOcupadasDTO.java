package es.uji.apps.par.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the PAR_LOCALIZACION_OCUPADAS database table.
 * 
 */
@Entity
@Table(name="PAR_LOCALIZACION_OCUPADAS")
public class LocalizacionOcupadasDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAR_LOCALIZACION_OCUPADAS_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_LOCALIZACION_OCUPADAS_ID_GENERATOR")
	private long id;

	private int ocupadas;

	//bi-directional many-to-one association to LocalizacionDTO
	@ManyToOne
	@JoinColumn(name="LOCALIZACION_ID")
	private LocalizacionDTO parLocalizacion;

	//bi-directional many-to-one association to SesionDTO
	@ManyToOne
	@JoinColumn(name="SESION_ID")
	private SesionDTO parSesion;

	public LocalizacionOcupadasDTO() {
	}

	public LocalizacionOcupadasDTO(SesionDTO sesion, LocalizacionDTO localizacion)
    {
        super();
        this.parSesion = sesion;
        this.parLocalizacion = localizacion;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOcupadas() {
        return ocupadas;
    }

    public void setOcupadas(int ocupadas) {
        this.ocupadas = ocupadas;
    }

    public LocalizacionDTO getParLocalizacion() {
		return this.parLocalizacion;
	}

	public void setParLocalizacion(LocalizacionDTO parLocalizacion) {
		this.parLocalizacion = parLocalizacion;
	}

	public SesionDTO getParSesion() {
		return this.parSesion;
	}

	public void setParSesion(SesionDTO parSesion) {
		this.parSesion = parSesion;
	}

}
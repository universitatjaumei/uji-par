package es.uji.apps.par.db;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the PAR_TARIFAS database table.
 * 
 */
@Entity
@Table(name="PAR_TARIFAS")
public class TarifaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAR_TARIFAS_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_TARIFAS_ID_GENERATOR")
	private long id;
	private String nombre;
	private Boolean isPublica;
	private Boolean defecto;
	
	@OneToMany(mappedBy = "parTarifa")
	private List<TarifasCineDTO> parTarifasCine;

	public TarifaDTO() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<TarifasCineDTO> getParTarifasCine() {
		return parTarifasCine;
	}

	public void setParTarifasCine(List<TarifasCineDTO> parTarifasCine) {
		this.parTarifasCine = parTarifasCine;
	}

	public Boolean getIsPublica() {
		return isPublica;
	}

	public void setIsPublica(Boolean isPublica) {
		this.isPublica = isPublica;
	}

	public Boolean getDefecto() {
		return defecto;
	}

	public void setDefecto(Boolean defecto) {
		this.defecto = defecto;
	}
}
package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PAR_TIPOS_EVENTO database table.
 * 
 */
@Entity
@Table(name="PAR_TIPOS_EVENTO")
public class ParTipoEventoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="NOMBRE_VA")
	private String nombreVa;

	@Column(name="NOMBRE_EN")
	private String nombreEn;

	@Column(name="NOMBRE_ES")
	private String nombreEs;

	//bi-directional many-to-one association to ParEventoDTO
	@OneToMany(mappedBy="parTiposEvento")
	private List<ParEventoDTO> parEventos;

	public ParTipoEventoDTO() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreVa() {
		return this.nombreVa;
	}

	public void setNombreVa(String nombreVa) {
		this.nombreVa = nombreVa;
	}

	public String getNombreEn() {
		return this.nombreEn;
	}

	public void setNombreEn(String nombreEn) {
		this.nombreEn = nombreEn;
	}

	public String getNombreEs() {
		return this.nombreEs;
	}

	public void setNombreEs(String nombreEs) {
		this.nombreEs = nombreEs;
	}

	public List<ParEventoDTO> getParEventos() {
		return this.parEventos;
	}

	public void setParEventos(List<ParEventoDTO> parEventos) {
		this.parEventos = parEventos;
	}

}
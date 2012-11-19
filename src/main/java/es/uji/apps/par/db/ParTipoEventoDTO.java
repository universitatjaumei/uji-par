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

	private String nombre;

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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<ParEventoDTO> getParEventos() {
		return this.parEventos;
	}

	public void setParEventos(List<ParEventoDTO> parEventos) {
		this.parEventos = parEventos;
	}

}
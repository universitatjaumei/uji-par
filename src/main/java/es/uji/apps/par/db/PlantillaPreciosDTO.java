package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PAR_PLANTILLAS_PRECIOS database table.
 * 
 */
@Entity
@Table(name="PAR_PLANTILLAS_PRECIOS")
public class PlantillaPreciosDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAR_PLANTILLAS_PRECIOS_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_PLANTILLAS_PRECIOS_ID_GENERATOR")
	private long id;

	private String nombre;

	//bi-directional many-to-one association to PrecioDTO
	@OneToMany(mappedBy="parPlantillasPrecio")
	private List<PrecioDTO> parPrecios;

	public PlantillaPreciosDTO() {
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

	public List<PrecioDTO> getParPrecios() {
		return this.parPrecios;
	}

	public void setParPrecios(List<PrecioDTO> parPrecios) {
		this.parPrecios = parPrecios;
	}

}
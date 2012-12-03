package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the PAR_LOCALIZACIONES database table.
 * 
 */
@Entity
@Table(name="PAR_LOCALIZACIONES")
public class ParLocalizacionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAR_LOCALIZACIONES_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_LOCALIZACIONES_ID_GENERATOR")
	private long id;

	private String nombre;

	@Column(name="TOTAL_ENTRADAS")
	private BigDecimal totalEntradas;

	public ParLocalizacionDTO() {
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

	public BigDecimal getTotalEntradas() {
		return this.totalEntradas;
	}

	public void setTotalEntradas(BigDecimal totalEntradas) {
		this.totalEntradas = totalEntradas;
	}

}
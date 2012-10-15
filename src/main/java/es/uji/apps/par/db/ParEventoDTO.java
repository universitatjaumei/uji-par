package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the PAR_EVENTOS database table.
 * 
 */
@Entity
@Table(name="PAR_EVENTOS")
public class ParEventoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String caracteristicas;

	private String comentarios;

	private String companyia;

	private String descripcion;

	private String duracion;

	@Lob
	private byte[] imagen;

	private String interpretes;

	private String premios;

	private String titulo;

	//bi-directional many-to-one association to ParTipoEventoDTO
	@ManyToOne
	@JoinColumn(name="TIPO_EVENTO_ID")
	private ParTipoEventoDTO parTiposEvento;

	//bi-directional many-to-one association to ParSesionDTO
	@OneToMany(mappedBy="parEvento")
	private List<ParSesionDTO> parSesiones;

	public ParEventoDTO() {
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCaracteristicas() {
		return this.caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public String getComentarios() {
		return this.comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getCompanyia() {
		return this.companyia;
	}

	public void setCompanyia(String companyia) {
		this.companyia = companyia;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDuracion() {
		return this.duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getInterpretes() {
		return this.interpretes;
	}

	public void setInterpretes(String interpretes) {
		this.interpretes = interpretes;
	}

	public String getPremios() {
		return this.premios;
	}

	public void setPremios(String premios) {
		this.premios = premios;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public ParTipoEventoDTO getParTiposEvento() {
		return this.parTiposEvento;
	}

	public void setParTiposEvento(ParTipoEventoDTO parTiposEvento) {
		this.parTiposEvento = parTiposEvento;
	}

	public List<ParSesionDTO> getParSesiones() {
		return this.parSesiones;
	}

	public void setParSesiones(List<ParSesionDTO> parSesiones) {
		this.parSesiones = parSesiones;
	}

}
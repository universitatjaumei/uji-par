package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.uji.apps.par.db.ParEventoDTO;
import es.uji.apps.par.db.ParTipoEventoDTO;

@XmlRootElement
public class ParEvento
{
    private long id;
    private String titulo;
    private String descripcion;
    private String companyia;
    private String interpretes;
    private String duracion;
    private byte[] imagen;
    private String premios;
    private String caracteristicas;
    private String comentarios;
    private ParTipoEventoDTO tipoEvento;

    public ParEvento()
    {
    }
    
    public ParEvento(ParEventoDTO eventoDTO)
    {
        this.id = eventoDTO.getId();
        this.titulo = eventoDTO.getTitulo();
        this.descripcion = eventoDTO.getDescripcion();
        this.companyia = eventoDTO.getCompanyia();
        this.interpretes = eventoDTO.getInterpretes();
        this.duracion = eventoDTO.getDuracion();
        this.imagen = eventoDTO.getImagen();
        this.premios = eventoDTO.getPremios();
        this.caracteristicas = eventoDTO.getCaracteristicas();
        this.comentarios = eventoDTO.getComentarios();
        this.tipoEvento = eventoDTO.getParTiposEvento();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCompanyia() {
		return companyia;
	}

	public void setCompanyia(String companyia) {
		this.companyia = companyia;
	}

	public String getInterpretes() {
		return interpretes;
	}

	public void setInterpretes(String interpretes) {
		this.interpretes = interpretes;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}
    
	public String getPremios() {
		return premios;
	}

	public void setPremios(String premios) {
		this.premios = premios;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	@JsonIgnore
	public ParTipoEventoDTO getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(ParTipoEventoDTO tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
}
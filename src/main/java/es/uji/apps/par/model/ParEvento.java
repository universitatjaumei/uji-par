package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ParEventoDTO;

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
    private ParTipoEvento parTipoEvento;
    private long tipoEvento;
	private String imagenSrc;
	private String imagenContentType;

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
        this.imagenContentType = eventoDTO.getImagenContentType();
        this.imagenSrc = eventoDTO.getImagenSrc();
        this.premios = eventoDTO.getPremios();
        this.caracteristicas = eventoDTO.getCaracteristicas();
        this.comentarios = eventoDTO.getComentarios();
        
        if (eventoDTO.getParTiposEvento() != null) {
        	this.parTipoEvento = new ParTipoEvento();
        	this.parTipoEvento.setId(eventoDTO.getParTiposEvento().getId());
        	this.parTipoEvento.setNombre(eventoDTO.getParTiposEvento().getNombre());
        	this.tipoEvento = eventoDTO.getParTiposEvento().getId();
        }
    }

	public ParEvento(String titulo, ParTipoEvento tipoEvento) {
		this.parTipoEvento = new ParTipoEvento();
		this.parTipoEvento = tipoEvento;
		this.titulo = titulo;
	}

	public ParEvento(String titulo, String descripcion, String companyia, String interpretes,
			String duracion, byte[] dataBinary,	String fileName, String mediaType, String premios,
			String caracteristicas, String comentarios, Integer tipoEventoId) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.companyia = companyia;
		this.interpretes = interpretes;
		this.duracion = duracion;
		this.premios = premios;
		this.caracteristicas = caracteristicas;
		this.comentarios = comentarios;
		
		this.imagen = dataBinary;
		this.imagenSrc = fileName;
		this.imagenContentType = mediaType;
		
		if (tipoEventoId != null) {
			this.parTipoEvento = new ParTipoEvento();
			this.parTipoEvento.setId(tipoEventoId);
			this.tipoEvento = tipoEventoId;
		}
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

	public String getImagenSrc() {
		return imagenSrc;
	}

	public void setImagenSrc(String imagenSrc) {
		this.imagenSrc = imagenSrc;
	}

	public String getImagenContentType() {
		return imagenContentType;
	}

	public void setImagenContentType(String imagenContentType) {
		this.imagenContentType = imagenContentType;
	}

	public ParTipoEvento getParTipoEvento() {
		return parTipoEvento;
	}

	public void setParTipoEvento(ParTipoEvento parTipoEvento) {
		this.parTipoEvento = parTipoEvento;
	}

	public long getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(long tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
}
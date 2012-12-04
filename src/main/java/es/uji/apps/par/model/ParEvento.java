package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ParEventoDTO;

@XmlRootElement
public class ParEvento
{
    private long id;
    private String tituloEs;
    private String tituloVa;
    private String tituloEn;
    private String descripcionEs;
    private String descripcionVa;
    private String descripcionEn;
    private String companyiaEs;
    private String companyiaVa;
    private String companyiaEn;
    private String interpretesEs;
    private String interpretesEn;
    private String interpretesVa;
    private String duracionEs;
    private String duracionVa;
    private String duracionEn;
    private byte[] imagen;
    private String premiosEs;
    private String premiosVa;
    private String premiosEn;
    private String caracteristicasEs;
    private String caracteristicasVa;
    private String caracteristicasEn;
    private String comentariosEs;
    private String comentariosVa;
    private String comentariosEn;
    private ParTipoEvento parTipoEvento;
    private long tipoEvento;
	private String imagenSrc;
	private String imagenContentType;

    public ParEvento()
    {
    }
    
    public ParEvento(ParEventoDTO eventoDTO, boolean crearConImagen)
    {
        this.id = eventoDTO.getId();
        this.tituloEs = eventoDTO.getTituloEs();
        this.tituloVa = eventoDTO.getTituloVa();
        this.tituloEn = eventoDTO.getTituloEn();
        
        this.descripcionEs = eventoDTO.getDescripcionEs();
        this.descripcionVa = eventoDTO.getDescripcionVa();
        this.descripcionEn = eventoDTO.getDescripcionEn();
        
        this.companyiaEs = eventoDTO.getCompanyiaEs();
        this.companyiaEn = eventoDTO.getCompanyiaEn();
        this.companyiaVa = eventoDTO.getCompanyiaVa();
        
        this.interpretesEs = eventoDTO.getInterpretesEs();
        this.interpretesEn = eventoDTO.getInterpretesEn();
        this.interpretesVa = eventoDTO.getInterpretesVa();
        
        this.duracionEs = eventoDTO.getDuracionEs();
        this.duracionEn = eventoDTO.getDuracionEn();
        this.duracionVa = eventoDTO.getDuracionVa();
        
        if (crearConImagen) {
        	this.imagen = eventoDTO.getImagen();
        }
        
        this.imagenContentType = eventoDTO.getImagenContentType();
    	this.imagenSrc = eventoDTO.getImagenSrc();
        
        this.premiosEs = eventoDTO.getPremiosEs();
        this.premiosVa = eventoDTO.getPremiosVa();
        this.premiosEn = eventoDTO.getPremiosEn();
        
        this.caracteristicasEs = eventoDTO.getCaracteristicasEs();
        this.caracteristicasEn = eventoDTO.getCaracteristicasEn();
        this.caracteristicasVa = eventoDTO.getCaracteristicasVa();
        
        this.comentariosEs = eventoDTO.getComentariosEs();
        this.comentariosEn = eventoDTO.getComentariosEn();
        this.comentariosVa = eventoDTO.getComentariosVa();
        
        if (eventoDTO.getParTiposEvento() != null) {
        	this.parTipoEvento = new ParTipoEvento();
        	this.parTipoEvento.setId(eventoDTO.getParTiposEvento().getId());
        	this.parTipoEvento.setNombreEs(eventoDTO.getParTiposEvento().getNombreEs());
        	this.parTipoEvento.setNombreVa(eventoDTO.getParTiposEvento().getNombreVa());
        	this.parTipoEvento.setNombreEn(eventoDTO.getParTiposEvento().getNombreEn());
        	this.tipoEvento = eventoDTO.getParTiposEvento().getId();
        }
    }

	public ParEvento(String tituloEs, ParTipoEvento tipoEvento) {
		this.parTipoEvento = new ParTipoEvento();
		this.parTipoEvento = tipoEvento;
		this.tituloEs = tituloEs;
	}

	public ParEvento(
			String tituloEs, String descripcionEs, String companyiaEs, String interpretesEs, String duracionEs, String premiosEs, String caracteristicasEs, String comentariosEs, 
			String tituloVa, String descripcionVa, String companyiaVa, String interpretesVa, String duracionVa,	String premiosVa, String caracteristicasVa,	String comentariosVa, 
			String tituloEn, String descripcionEn, String companyiaEn, String interpretesEn, String duracionEn, String premiosEn, String caracteristicasEn,	String comentariosEn, 
			byte[] dataBinary, String nombreArchivo, String mediaType, Integer tipoEventoId) {
		
		this.tituloEs = tituloEs;
		this.descripcionEs = descripcionEs;
		this.companyiaEs = companyiaEs;
		this.interpretesEs = interpretesEs;
		this.duracionEs = duracionEs;
		this.premiosEs = premiosEs;
		this.caracteristicasEs = caracteristicasEs;
		this.comentariosEs = comentariosEs;
		
		this.tituloVa = tituloVa;
		this.descripcionVa = descripcionVa;
		this.companyiaVa = companyiaVa;
		this.interpretesVa = interpretesVa;
		this.duracionVa = duracionVa;
		this.premiosVa = premiosVa;
		this.caracteristicasVa = caracteristicasVa;
		this.comentariosVa = comentariosVa;
		
		this.tituloEn = tituloEn;
		this.descripcionEn = descripcionEn;
		this.companyiaEn = companyiaEn;
		this.interpretesEn = interpretesEn;
		this.duracionEn = duracionEn;
		this.premiosEn = premiosEn;
		this.caracteristicasEn = caracteristicasEn;
		this.comentariosEn = comentariosEn;
		
		this.imagen = dataBinary;
		this.imagenSrc = nombreArchivo;
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

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
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

	public String getTituloEs() {
		return tituloEs;
	}

	public void setTituloEs(String tituloEs) {
		this.tituloEs = tituloEs;
	}

	public String getTituloVa() {
		return tituloVa;
	}

	public void setTituloVa(String tituloVa) {
		this.tituloVa = tituloVa;
	}

	public String getTituloEn() {
		return tituloEn;
	}

	public void setTituloEn(String tituloEn) {
		this.tituloEn = tituloEn;
	}

	public String getDescripcionEs() {
		return descripcionEs;
	}

	public void setDescripcionEs(String descripcionEs) {
		this.descripcionEs = descripcionEs;
	}

	public String getDescripcionVa() {
		return descripcionVa;
	}

	public void setDescripcionVa(String descripcionVa) {
		this.descripcionVa = descripcionVa;
	}

	public String getDescripcionEn() {
		return descripcionEn;
	}

	public void setDescripcionEn(String descripcionEn) {
		this.descripcionEn = descripcionEn;
	}

	public String getCompanyiaEs() {
		return companyiaEs;
	}

	public void setCompanyiaEs(String companyiaEs) {
		this.companyiaEs = companyiaEs;
	}

	public String getCompanyiaVa() {
		return companyiaVa;
	}

	public void setCompanyiaVa(String companyiaVa) {
		this.companyiaVa = companyiaVa;
	}

	public String getCompanyiaEn() {
		return companyiaEn;
	}

	public void setCompanyiaEn(String companyiaEn) {
		this.companyiaEn = companyiaEn;
	}

	public String getInterpretesEs() {
		return interpretesEs;
	}

	public void setInterpretesEs(String interpretesEs) {
		this.interpretesEs = interpretesEs;
	}

	public String getInterpretesEn() {
		return interpretesEn;
	}

	public void setInterpretesEn(String interpretesEn) {
		this.interpretesEn = interpretesEn;
	}

	public String getInterpretesVa() {
		return interpretesVa;
	}

	public void setInterpretesVa(String interpretesVa) {
		this.interpretesVa = interpretesVa;
	}

	public String getDuracionEs() {
		return duracionEs;
	}

	public void setDuracionEs(String duracionEs) {
		this.duracionEs = duracionEs;
	}

	public String getDuracionVa() {
		return duracionVa;
	}

	public void setDuracionVa(String duracionVa) {
		this.duracionVa = duracionVa;
	}

	public String getDuracionEn() {
		return duracionEn;
	}

	public void setDuracionEn(String duracionEn) {
		this.duracionEn = duracionEn;
	}

	public String getPremiosEs() {
		return premiosEs;
	}

	public void setPremiosEs(String premiosEs) {
		this.premiosEs = premiosEs;
	}

	public String getPremiosVa() {
		return premiosVa;
	}

	public void setPremiosVa(String premiosVa) {
		this.premiosVa = premiosVa;
	}

	public String getPremiosEn() {
		return premiosEn;
	}

	public void setPremiosEn(String premiosEn) {
		this.premiosEn = premiosEn;
	}

	public String getCaracteristicasEs() {
		return caracteristicasEs;
	}

	public void setCaracteristicasEs(String caracteristicasEs) {
		this.caracteristicasEs = caracteristicasEs;
	}

	public String getCaracteristicasVa() {
		return caracteristicasVa;
	}

	public void setCaracteristicasVa(String caracteristicasVa) {
		this.caracteristicasVa = caracteristicasVa;
	}

	public String getCaracteristicasEn() {
		return caracteristicasEn;
	}

	public void setCaracteristicasEn(String caracteristicasEn) {
		this.caracteristicasEn = caracteristicasEn;
	}

	public String getComentariosEs() {
		return comentariosEs;
	}

	public void setComentariosEs(String comentariosEs) {
		this.comentariosEs = comentariosEs;
	}

	public String getComentariosVa() {
		return comentariosVa;
	}

	public void setComentariosVa(String comentariosVa) {
		this.comentariosVa = comentariosVa;
	}

	public String getComentariosEn() {
		return comentariosEn;
	}

	public void setComentariosEn(String comentariosEn) {
		this.comentariosEn = comentariosEn;
	}

	public String getImagenSrc() {
		return imagenSrc;
	}

}
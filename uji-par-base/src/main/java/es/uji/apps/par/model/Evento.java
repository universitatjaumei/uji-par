package es.uji.apps.par.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.EventoDTO;

@XmlRootElement
public class Evento
{
    private long id;
    private String tituloEs;
    private String tituloVa;
    private String descripcionEs;
    private String descripcionVa;
    private String companyiaEs;
    private String companyiaVa;
    private String interpretesEs;
    private String interpretesVa;
    private String duracionEs;
    private String duracionVa;
    private byte[] imagen;
    private String imagenSrc;
    private String imagenContentType;
    private String premiosEs;
    private String premiosVa;
    private String caracteristicasEs;
    private String caracteristicasVa;
    private String comentariosEs;
    private String comentariosVa;
    private TipoEvento parTiposEvento;
    private long tipoEvento;
    private BigDecimal asientosNumerados;
    private BigDecimal porcentajeIVA;
    private BigDecimal ivaSGAE;
    private BigDecimal retencionSGAE;

    public Evento()
    {
    }
    
    public static Evento eventoDTOtoEvento(EventoDTO eventoDTO) {
    	Evento evento = new Evento();
    	
    	evento.setId(eventoDTO.getId());
    	evento.setTituloEs(eventoDTO.getTituloEs());
    	evento.setTituloVa(eventoDTO.getTituloVa());

    	evento.setDescripcionEs(eventoDTO.getDescripcionEs());
    	evento.setDescripcionVa(eventoDTO.getDescripcionVa());

    	evento.setCompanyiaEs(eventoDTO.getCompanyiaEs());
    	evento.setCompanyiaVa(eventoDTO.getCompanyiaVa());

    	evento.setInterpretesEs(eventoDTO.getInterpretesEs());
    	evento.setInterpretesVa(eventoDTO.getInterpretesVa());

    	evento.setDuracionEs(eventoDTO.getDuracionEs());
    	evento.setDuracionVa(eventoDTO.getDuracionVa());

    	evento.setImagen(eventoDTO.getImagen());
    	evento.setImagenContentType(eventoDTO.getImagenContentType());
    	evento.setImagenSrc(eventoDTO.getImagenSrc());

    	evento.setPremiosEs(eventoDTO.getPremiosEs());
    	evento.setPremiosVa(eventoDTO.getPremiosVa());

    	evento.setCaracteristicasEs(eventoDTO.getCaracteristicasEs());
    	evento.setCaracteristicasVa(eventoDTO.getCaracteristicasVa());

    	evento.setComentariosEs(eventoDTO.getComentariosEs());
    	evento.setComentariosVa(eventoDTO.getComentariosVa());

        if (eventoDTO.getParTiposEvento() != null)
        	evento.setParTipoEvento(TipoEvento.tipoEventoDTOToTipoEvento(eventoDTO.getParTiposEvento()));
        
        evento.setAsientosNumerados(eventoDTO.getAsientosNumerados());
        evento.setIvaSGAE(eventoDTO.getIvaSgae());
        evento.setRetencionSGAE(eventoDTO.getRetencionSgae());
        evento.setPorcentajeIVA(eventoDTO.getPorcentajeIva());
    	
    	return evento;
    }
    
    public static EventoDTO eventoToEventoDTO(Evento evento) {
    	EventoDTO eventoDTO = new EventoDTO();
    	
    	eventoDTO.setId(evento.getId());
    	eventoDTO.setTituloEs(evento.getTituloEs());
    	eventoDTO.setTituloVa(evento.getTituloVa());

    	eventoDTO.setDescripcionEs(evento.getDescripcionEs());
    	eventoDTO.setDescripcionVa(evento.getDescripcionVa());

    	eventoDTO.setCompanyiaEs(evento.getCompanyiaEs());
    	eventoDTO.setCompanyiaVa(evento.getCompanyiaVa());

    	eventoDTO.setInterpretesEs(evento.getInterpretesEs());
    	eventoDTO.setInterpretesVa(evento.getInterpretesVa());

    	eventoDTO.setDuracionEs(evento.getDuracionEs());
    	eventoDTO.setDuracionVa(evento.getDuracionVa());

    	eventoDTO.setImagen(evento.getImagen());
    	eventoDTO.setImagenContentType(evento.getImagenContentType());
    	eventoDTO.setImagenSrc(evento.getImagenSrc());

    	eventoDTO.setPremiosEs(evento.getPremiosEs());
    	eventoDTO.setPremiosVa(evento.getPremiosVa());

    	eventoDTO.setCaracteristicasEs(evento.getCaracteristicasEs());
    	eventoDTO.setCaracteristicasVa(evento.getCaracteristicasVa());

    	eventoDTO.setComentariosEs(evento.getComentariosEs());
    	eventoDTO.setComentariosVa(evento.getComentariosVa());

        if (eventoDTO.getParTiposEvento() != null)
        	eventoDTO.setParTiposEvento(TipoEvento.tipoEventoToTipoEventoDTO(evento.getParTiposEvento()));
        
        eventoDTO.setAsientosNumerados(evento.getAsientosNumerados());
        eventoDTO.setIvaSgae(evento.getIvaSGAE());
        eventoDTO.setRetencionSgae(evento.getRetencionSGAE());
        eventoDTO.setPorcentajeIva(evento.getPorcentajeIVA());
    	
    	return eventoDTO;
	}

    public Evento(EventoDTO eventoDTO, boolean crearConImagen)
    {
        this.id = eventoDTO.getId();
        this.tituloEs = eventoDTO.getTituloEs();
        this.tituloVa = eventoDTO.getTituloVa();

        this.descripcionEs = eventoDTO.getDescripcionEs();
        this.descripcionVa = eventoDTO.getDescripcionVa();

        this.companyiaEs = eventoDTO.getCompanyiaEs();
        this.companyiaVa = eventoDTO.getCompanyiaVa();

        this.interpretesEs = eventoDTO.getInterpretesEs();
        this.interpretesVa = eventoDTO.getInterpretesVa();

        this.duracionEs = eventoDTO.getDuracionEs();
        this.duracionVa = eventoDTO.getDuracionVa();

        if (crearConImagen)
        {
            this.imagen = eventoDTO.getImagen();
        }

        this.imagenContentType = eventoDTO.getImagenContentType();
        this.imagenSrc = eventoDTO.getImagenSrc();

        this.premiosEs = eventoDTO.getPremiosEs();
        this.premiosVa = eventoDTO.getPremiosVa();

        this.caracteristicasEs = eventoDTO.getCaracteristicasEs();
        this.caracteristicasVa = eventoDTO.getCaracteristicasVa();

        this.comentariosEs = eventoDTO.getComentariosEs();
        this.comentariosVa = eventoDTO.getComentariosVa();

        if (eventoDTO.getParTiposEvento() != null)
        {
            this.parTiposEvento = new TipoEvento();
            this.parTiposEvento.setId(eventoDTO.getParTiposEvento().getId());
            this.parTiposEvento.setNombreEs(eventoDTO.getParTiposEvento().getNombreEs());
            this.parTiposEvento.setNombreVa(eventoDTO.getParTiposEvento().getNombreVa());
            this.tipoEvento = eventoDTO.getParTiposEvento().getId();
        }
        
        this.asientosNumerados = eventoDTO.getAsientosNumerados();
        this.ivaSGAE = eventoDTO.getIvaSgae();
        this.retencionSGAE = eventoDTO.getRetencionSgae();
        this.porcentajeIVA = eventoDTO.getPorcentajeIva();
    }

    public Evento(String tituloEs, TipoEvento tipoEvento)
    {
        this.parTiposEvento = new TipoEvento();
        this.parTiposEvento = tipoEvento;
        this.tituloEs = tituloEs;
    }

    public Evento(String tituloEs, String descripcionEs, String companyiaEs, String interpretesEs,
            String duracionEs, String premiosEs, String caracteristicasEs, String comentariosEs,
            String tituloVa, String descripcionVa, String companyiaVa, String interpretesVa,
            String duracionVa, String premiosVa, String caracteristicasVa, String comentariosVa,
            byte[] dataBinary, String nombreArchivo, String mediaType, Integer tipoEventoId,
            BigDecimal porcentajeIVA, BigDecimal retencionSGAE, BigDecimal ivaSGAE, BigDecimal asientosNumerados)
    {

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

        this.imagen = dataBinary;
        this.imagenSrc = nombreArchivo;
        this.imagenContentType = mediaType;

        if (tipoEventoId != null)
        {
            this.parTiposEvento = new TipoEvento();
            this.parTiposEvento.setId(tipoEventoId);
            this.tipoEvento = tipoEventoId;
        }
        
        this.porcentajeIVA = porcentajeIVA;
        this.retencionSGAE = retencionSGAE;
        this.ivaSGAE = ivaSGAE;
        this.asientosNumerados = asientosNumerados;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public byte[] getImagen()
    {
        return imagen;
    }

    public void setImagen(byte[] imagen)
    {
        this.imagen = imagen;
    }

    public void setImagenSrc(String imagenSrc)
    {
        this.imagenSrc = imagenSrc;
    }

    public String getImagenContentType()
    {
        return imagenContentType;
    }

    public void setImagenContentType(String imagenContentType)
    {
        this.imagenContentType = imagenContentType;
    }

    public TipoEvento getParTiposEvento()
    {
        return parTiposEvento;
    }

    public void setParTipoEvento(TipoEvento parTiposEvento)
    {
        this.parTiposEvento = parTiposEvento;
    }

    public long getTipoEvento()
    {
        return tipoEvento;
    }

    public void setTipoEvento(long tipoEvento)
    {
        this.tipoEvento = tipoEvento;
    }

    public String getTituloEs()
    {
        return tituloEs;
    }

    public void setTituloEs(String tituloEs)
    {
        this.tituloEs = tituloEs;
    }

    public String getTituloVa()
    {
        return tituloVa;
    }

    public void setTituloVa(String tituloVa)
    {
        this.tituloVa = tituloVa;
    }

    public String getDescripcionEs()
    {
        return descripcionEs;
    }

    public void setDescripcionEs(String descripcionEs)
    {
        this.descripcionEs = descripcionEs;
    }

    public String getDescripcionVa()
    {
        return descripcionVa;
    }

    public void setDescripcionVa(String descripcionVa)
    {
        this.descripcionVa = descripcionVa;
    }

    public String getCompanyiaEs()
    {
        return companyiaEs;
    }

    public void setCompanyiaEs(String companyiaEs)
    {
        this.companyiaEs = companyiaEs;
    }

    public String getCompanyiaVa()
    {
        return companyiaVa;
    }

    public void setCompanyiaVa(String companyiaVa)
    {
        this.companyiaVa = companyiaVa;
    }

    public String getInterpretesEs()
    {
        return interpretesEs;
    }

    public void setInterpretesEs(String interpretesEs)
    {
        this.interpretesEs = interpretesEs;
    }

    public String getInterpretesVa()
    {
        return interpretesVa;
    }

    public void setInterpretesVa(String interpretesVa)
    {
        this.interpretesVa = interpretesVa;
    }

    public String getDuracionEs()
    {
        return duracionEs;
    }

    public void setDuracionEs(String duracionEs)
    {
        this.duracionEs = duracionEs;
    }

    public String getDuracionVa()
    {
        return duracionVa;
    }

    public void setDuracionVa(String duracionVa)
    {
        this.duracionVa = duracionVa;
    }

    public String getPremiosEs()
    {
        return premiosEs;
    }

    public void setPremiosEs(String premiosEs)
    {
        this.premiosEs = premiosEs;
    }

    public String getPremiosVa()
    {
        return premiosVa;
    }

    public void setPremiosVa(String premiosVa)
    {
        this.premiosVa = premiosVa;
    }

    public String getCaracteristicasEs()
    {
        return caracteristicasEs;
    }

    public void setCaracteristicasEs(String caracteristicasEs)
    {
        this.caracteristicasEs = caracteristicasEs;
    }

    public String getCaracteristicasVa()
    {
        return caracteristicasVa;
    }

    public void setCaracteristicasVa(String caracteristicasVa)
    {
        this.caracteristicasVa = caracteristicasVa;
    }

    public String getComentariosEs()
    {
        return comentariosEs;
    }

    public void setComentariosEs(String comentariosEs)
    {
        this.comentariosEs = comentariosEs;
    }

    public String getComentariosVa()
    {
        return comentariosVa;
    }

    public void setComentariosVa(String comentariosVa)
    {
        this.comentariosVa = comentariosVa;
    }

    public String getImagenSrc()
    {
        return imagenSrc;
    }

	public BigDecimal getAsientosNumerados() {
		return asientosNumerados;
	}

	public void setAsientosNumerados(BigDecimal asientosNumerados) {
		this.asientosNumerados = asientosNumerados;
	}

	public BigDecimal getPorcentajeIVA() {
		return porcentajeIVA;
	}

	public void setPorcentajeIVA(BigDecimal porcentajeIVA) {
		this.porcentajeIVA = porcentajeIVA;
	}

	public BigDecimal getIvaSGAE() {
		return ivaSGAE;
	}

	public void setIvaSGAE(BigDecimal ivaSGAE) {
		this.ivaSGAE = ivaSGAE;
	}

	public BigDecimal getRetencionSGAE() {
		return retencionSGAE;
	}

	public void setRetencionSGAE(BigDecimal retencionSGAE) {
		this.retencionSGAE = retencionSGAE;
	}
}
package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;
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
	private long id;

	@Column(name="CARACTERISTICAS_EN")
	private String caracteristicasEn;

	@Column(name="CARACTERISTICAS_ES")
	private String caracteristicasEs;

	@Column(name="CARACTERISTICAS_VA")
	private String caracteristicasVa;

	@Column(name="COMENTARIOS_EN")
	private String comentariosEn;

	@Column(name="COMENTARIOS_ES")
	private String comentariosEs;

	@Column(name="COMENTARIOS_VA")
	private String comentariosVa;

	@Column(name="COMPANYIA_EN")
	private String companyiaEn;

	@Column(name="COMPANYIA_ES")
	private String companyiaEs;

	@Column(name="COMPANYIA_VA")
	private String companyiaVa;

	@Column(name="DESCRIPCION_EN")
	private String descripcionEn;

	@Column(name="DESCRIPCION_ES")
	private String descripcionEs;

	@Column(name="DESCRIPCION_VA")
	private String descripcionVa;

	@Column(name="DURACION_EN")
	private String duracionEn;

	@Column(name="DURACION_ES")
	private String duracionEs;

	@Column(name="DURACION_VA")
	private String duracionVa;

	@Lob
	private byte[] imagen;

	@Column(name="IMAGEN_CONTENT_TYPE")
	private String imagenContentType;

	@Column(name="IMAGEN_SRC")
	private String imagenSrc;

	@Column(name="INTERPRETES_EN")
	private String interpretesEn;

	@Column(name="INTERPRETES_ES")
	private String interpretesEs;

	@Column(name="INTERPRETES_VA")
	private String interpretesVa;

	@Column(name="PREMIOS_EN")
	private String premiosEn;

	@Column(name="PREMIOS_ES")
	private String premiosEs;

	@Column(name="PREMIOS_VA")
	private String premiosVa;

	@Column(name="TITULO_EN")
	private String tituloEn;

	@Column(name="TITULO_ES")
	private String tituloEs;

	@Column(name="TITULO_VA")
	private String tituloVa;

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

	public String getCaracteristicasEn() {
		return this.caracteristicasEn;
	}

	public void setCaracteristicasEn(String caracteristicasEn) {
		this.caracteristicasEn = caracteristicasEn;
	}

	public String getCaracteristicasEs() {
		return this.caracteristicasEs;
	}

	public void setCaracteristicasEs(String caracteristicasEs) {
		this.caracteristicasEs = caracteristicasEs;
	}

	public String getCaracteristicasVa() {
		return this.caracteristicasVa;
	}

	public void setCaracteristicasVa(String caracteristicasVa) {
		this.caracteristicasVa = caracteristicasVa;
	}

	public String getComentariosEn() {
		return this.comentariosEn;
	}

	public void setComentariosEn(String comentariosEn) {
		this.comentariosEn = comentariosEn;
	}

	public String getComentariosEs() {
		return this.comentariosEs;
	}

	public void setComentariosEs(String comentariosEs) {
		this.comentariosEs = comentariosEs;
	}

	public String getComentariosVa() {
		return this.comentariosVa;
	}

	public void setComentariosVa(String comentariosVa) {
		this.comentariosVa = comentariosVa;
	}

	public String getCompanyiaEn() {
		return this.companyiaEn;
	}

	public void setCompanyiaEn(String companyiaEn) {
		this.companyiaEn = companyiaEn;
	}

	public String getCompanyiaEs() {
		return this.companyiaEs;
	}

	public void setCompanyiaEs(String companyiaEs) {
		this.companyiaEs = companyiaEs;
	}

	public String getCompanyiaVa() {
		return this.companyiaVa;
	}

	public void setCompanyiaVa(String companyiaVa) {
		this.companyiaVa = companyiaVa;
	}

	public String getDescripcionEn() {
		return this.descripcionEn;
	}

	public void setDescripcionEn(String descripcionEn) {
		this.descripcionEn = descripcionEn;
	}

	public String getDescripcionEs() {
		return this.descripcionEs;
	}

	public void setDescripcionEs(String descripcionEs) {
		this.descripcionEs = descripcionEs;
	}

	public String getDescripcionVa() {
		return this.descripcionVa;
	}

	public void setDescripcionVa(String descripcionVa) {
		this.descripcionVa = descripcionVa;
	}

	public String getDuracionEn() {
		return this.duracionEn;
	}

	public void setDuracionEn(String duracionEn) {
		this.duracionEn = duracionEn;
	}

	public String getDuracionEs() {
		return this.duracionEs;
	}

	public void setDuracionEs(String duracionEs) {
		this.duracionEs = duracionEs;
	}

	public String getDuracionVa() {
		return this.duracionVa;
	}

	public void setDuracionVa(String duracionVa) {
		this.duracionVa = duracionVa;
	}

	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getImagenContentType() {
		return this.imagenContentType;
	}

	public void setImagenContentType(String imagenContentType) {
		this.imagenContentType = imagenContentType;
	}

	public String getImagenSrc() {
		return this.imagenSrc;
	}

	public void setImagenSrc(String imagenSrc) {
		this.imagenSrc = imagenSrc;
	}

	public String getInterpretesEn() {
		return this.interpretesEn;
	}

	public void setInterpretesEn(String interpretesEn) {
		this.interpretesEn = interpretesEn;
	}

	public String getInterpretesEs() {
		return this.interpretesEs;
	}

	public void setInterpretesEs(String interpretesEs) {
		this.interpretesEs = interpretesEs;
	}

	public String getInterpretesVa() {
		return this.interpretesVa;
	}

	public void setInterpretesVa(String interpretesVa) {
		this.interpretesVa = interpretesVa;
	}

	public String getPremiosEn() {
		return this.premiosEn;
	}

	public void setPremiosEn(String premiosEn) {
		this.premiosEn = premiosEn;
	}

	public String getPremiosEs() {
		return this.premiosEs;
	}

	public void setPremiosEs(String premiosEs) {
		this.premiosEs = premiosEs;
	}

	public String getPremiosVa() {
		return this.premiosVa;
	}

	public void setPremiosVa(String premiosVa) {
		this.premiosVa = premiosVa;
	}

	public String getTituloEn() {
		return this.tituloEn;
	}

	public void setTituloEn(String tituloEn) {
		this.tituloEn = tituloEn;
	}

	public String getTituloEs() {
		return this.tituloEs;
	}

	public void setTituloEs(String tituloEs) {
		this.tituloEs = tituloEs;
	}

	public String getTituloVa() {
		return this.tituloVa;
	}

	public void setTituloVa(String tituloVa) {
		this.tituloVa = tituloVa;
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
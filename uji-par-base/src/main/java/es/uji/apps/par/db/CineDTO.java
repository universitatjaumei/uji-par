package es.uji.apps.par.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the PAR_CINES database table.
 */
@Entity
@Table(name = "PAR_CINES")
public class CineDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAR_CINES_ID_GENERATOR", sequenceName = "HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAR_CINES_ID_GENERATOR")
	private long id;

	@Column(name = "CODIGO")
	private String codigo;

	@Column(name = "NOMBRE")
	private String nombre;
	
    @Column(name = "CIF")
    private String cif;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "COD_MUNICIPIO")
    private String codigoMunicipio;

	@Column(name = "NOM_MUNICIPIO")
	private String nombreMunicipio;

	@Column(name = "CP")
	private String cp;
	
    @Column(name = "EMPRESA")
    private String empresa;	
    
    @Column(name = "COD_REGISTRO")
    private String codigoRegistro; 
    
    @Column(name = "TFNO")
    private String telefono;
    
    @Column(name = "IVA")
    private BigDecimal iva;

	@Column(name = "URL_PUBLIC")
	private String urlPublic;

	@Column(name = "URL_PRIVACIDAD")
	private String urlPrivacidad;

	@Column(name = "URL_COMO_LLEGAR")
	private String urlComoLlegar;

	@Column(name = "MAIL_FROM")
	private String mailFrom;

	@Column(name = "LOGO_REPORT")
	private String logoReport;

	@Column(name = "URL_PIE_ENTRADA")
	private String urlPieEntrada;

	@Column(name = "API_KEY")
	private String apiKey;

	@Column(name = "BUTACASENTRADOENDISTINTOCOLOR")
	private Boolean showButacasQueHanEntradoEnDistintoColor;

	@Column(name = "LANGS")
	private String langs;

	@Column(name = "DEFAULT_LANG")
	private String defaultLang;

	@Column(name = "SHOW_IVA")
	private Boolean showIVA;

	@Column(name = "PASSBOOK_ACTIVADO")
	private Boolean passbookActivado;

	@OneToMany(mappedBy = "parCine", fetch=FetchType.LAZY)
	private List<SalaDTO> parSalas;
	
	@OneToMany(mappedBy = "parCine")
	private List<TarifasCineDTO> parTarifasCine;

	@OneToMany(mappedBy = "parCine", fetch=FetchType.LAZY)
	private List<SalaDTO> parEventos;

	@OneToMany(mappedBy = "parCine", fetch=FetchType.LAZY)
	private List<TarifaDTO> parTarifas;

	@OneToMany(mappedBy = "parCine", fetch=FetchType.LAZY)
	private List<TipoEventoDTO> parTiposEvento;

	@OneToMany(mappedBy="cine")
	private List<TpvsCinesDTO> parTpvsCines;
    
	public CineDTO()
    {
    }
	
	public CineDTO(String codigo, String nombre, String cif, String direccion,
            String codigoMunicipio, String nombreMunicipio, String cp, String empresa, String codigoRegistro,
            String telefono, BigDecimal iva, String urlPublic, String urlPrivacidad, String urlComoLlegar, String urlPieEntrada, String mailFrom, String logoReport, String apiKey)
    {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
        this.codigoMunicipio = codigoMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.cp = cp;
        this.empresa = empresa;
        this.codigoRegistro = codigoRegistro;
        this.telefono = telefono;
        this.iva = iva;
		this.urlPublic = urlPublic;
		this.urlPrivacidad = urlPrivacidad;
		this.urlComoLlegar = urlComoLlegar;
		this.urlPieEntrada = urlPieEntrada;
		this.mailFrom = mailFrom;
		this.logoReport = logoReport;
		this.apiKey = apiKey;
    }

	public CineDTO(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getCif()
    {
        return cif;
    }

    public void setCif(String cif)
    {
        this.cif = cif;
    }

    public String getCodigoMunicipio()
    {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio)
    {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNombreMunicipio()
    {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio)
    {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getCp()
    {
        return cp;
    }

    public void setCp(String cp)
    {
        this.cp = cp;
    }

    public String getEmpresa()
    {
        return empresa;
    }

    public void setEmpresa(String empresa)
    {
        this.empresa = empresa;
    }

    public String getCodigoRegistro()
    {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro)
    {
        this.codigoRegistro = codigoRegistro;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public BigDecimal getIva()
    {
        return iva;
    }

    public void setIva(BigDecimal iva)
    {
        this.iva = iva;
    }

    public List<SalaDTO> getParSalas()
    {
        return parSalas;
    }

    public void setParSalas(List<SalaDTO> parSalas)
    {
        this.parSalas = parSalas;
    }

	public List<TarifasCineDTO> getParTarifasCine() {
		return parTarifasCine;
	}

	public void setParTarifasCine(List<TarifasCineDTO> parTarifasCine) {
		this.parTarifasCine = parTarifasCine;
	}

	public List<SalaDTO> getParEventos()
	{
		return parEventos;
	}

	public void setParEventos(List<SalaDTO> parEventos)
	{
		this.parEventos = parEventos;
	}

	public List<TarifaDTO> getParTarifas()
	{
		return parTarifas;
	}

	public void setParTarifas(List<TarifaDTO> parTarifas)
	{
		this.parTarifas = parTarifas;
	}

	public List<TipoEventoDTO> getParTiposEvento()
	{
		return parTiposEvento;
	}

	public void setParTiposEvento(List<TipoEventoDTO> parTiposEvento)
	{
		this.parTiposEvento = parTiposEvento;
	}

	public String getUrlPublic()
	{
		return urlPublic;
	}

	public void setUrlPublic(String urlPublic)
	{
		this.urlPublic = urlPublic;
	}

	public String getUrlPrivacidad()
	{
		return urlPrivacidad;
	}

	public void setUrlPrivacidad(String urlPrivacidad)
	{
		this.urlPrivacidad = urlPrivacidad;
	}

	public String getUrlComoLlegar()
	{
		return urlComoLlegar;
	}

	public void setUrlComoLlegar(String urlComoLlegar)
	{
		this.urlComoLlegar = urlComoLlegar;
	}

	public String getMailFrom()
	{
		return mailFrom;
	}

	public void setMailFrom(String mailFrom)
	{
		this.mailFrom = mailFrom;
	}

	public String getLogoReport()
	{
		return logoReport;
	}

	public void setLogoReport(String logoReport)
	{
		this.logoReport = logoReport;
	}

	public String getUrlPieEntrada()
	{
		return urlPieEntrada;
	}

	public void setUrlPieEntrada(String urlPieEntrada)
	{
		this.urlPieEntrada = urlPieEntrada;
	}

	public List<TpvsCinesDTO> getParTpvsCines()
	{
		return parTpvsCines;
	}

	public void setParTpvsCines(List<TpvsCinesDTO> parTpvsCines)
	{
		this.parTpvsCines = parTpvsCines;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public Boolean getShowButacasQueHanEntradoEnDistintoColor() {
		return showButacasQueHanEntradoEnDistintoColor;
	}

	public void setShowButacasQueHanEntradoEnDistintoColor(Boolean showButacasQueHanEntradoEnDistintoColor) {
		this.showButacasQueHanEntradoEnDistintoColor = showButacasQueHanEntradoEnDistintoColor;
	}

	public String getLangs() {
		return langs;
	}

	public void setLangs(String langs) {
		this.langs = langs;
	}

	public String getDefaultLang() {
		return defaultLang;
	}

	public void setDefaultLang(String defaultLang) {
		this.defaultLang = defaultLang;
	}

	public Boolean getShowIVA() {
		return showIVA;
	}

	public void setShowIVA(Boolean showIVA) {
		this.showIVA = showIVA;
	}

	public Boolean getPassbookActivado() {
		return passbookActivado;
	}

	public void setPassbookActivado(Boolean passbookActivado) {
		this.passbookActivado = passbookActivado;
	}

	public static CineDTO resultsetToDTO(ResultSet res) throws SQLException {
		CineDTO cine = new CineDTO();
		cine.setNombre(res.getString("nombre"));
		cine.setMailFrom(res.getString("mail_from"));
		cine.setUrlPublic(res.getString("url_public"));
		cine.setUrlPieEntrada(res.getString("url_pie_entrada"));
		cine.setDefaultLang(res.getString("default_lang"));

		return cine;
	}
}
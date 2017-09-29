package es.uji.apps.par.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.model.TipoInforme;
import es.uji.apps.par.utils.DateUtils;

@Component
public class Configuration
{
    private static final String TPV_ORDER_PREFIX_CODE_CAJAMAR = "0000";
    private static final String TPV_LANG_ES_CODE = "001";
    private static final String TPV_LANG_CA_CODE = "003";

    private static final String PROPERTIES_SEPARATOR = ",";
	private static final String SECRET = "uji.par.secret";
    private static final String LOCALIZACIONES = "uji.par.localizaciones";
    private static final String MAIL_HOST = "uji.par.mail.host";
	private static final String MAIL_USERNAME = "uji.par.mail.username";
	private static final String MAIL_PASSWORD = "uji.par.mail.password";
    private static final String PAY_MODES = "uji.par.paymodes";
    private static final String GASTOS_GESTION = "uji.par.gastosGestion";
    private static final String ENVIAR_MAILS_ENTRADAS = "uji.par.enviarMailsEntradas";
    private static final String ENTORNO = "uji.par.entorno";
    private static final String INFORME_EFECTIVO_CARGO = "uji.par.informeEfectivo.cargo";
    private static final String INFORME_EFECTIVO_FIRMANTE = "uji.par.informeEfectivo.firmante";
    private static final String MARGEN_VENTA_TAQUILLA_MINUTOS = "uji.par.margenVentaTaquillaMinutos";
    private static final String AUTH_CLASS = "uji.par.authClass";
    private static final String ADMIN_LOGIN = "uji.par.auth.admin.login";
    private static final String ADMIN_PASSWORD = "uji.par.auth.admin.password";
    private static final String USER_READONLY_LOGIN = "uji.par.auth.readonly.login";
    private static final String USER_READONLY_PASSWORD = "uji.par.auth.readonly.password";    
    private static final String JDBC_URL = "uji.db.jdbcUrl";
    private static final String DB_USER = "uji.db.username";
    private static final String DB_PASS = "uji.db.password";
    private static final String SYNC_TIPO = "uji.sync.lugar";
    public static final String SYNC_URL_TIPO = "uji.sync.rss";
    public static final String SYNC_TOKEN = "uji.sync.token";
    public static final String SYNC_HEADER_TOKEN = "uji.sync.headerToken";
    private static final String SYNC_HORAS_INICIO_VENTA_ONLINE = "uji.sync.horasInicioVentaOnlineTrasCreacion";
	private static final String BARCODE_WIDTH_HEIGHT = "uji.reports.barcodeWidthHeight";
    private static final String ENTRADA_ID = "uji.reports.entradaId";
    private static final String TPV = "uji.tpv";
	private static final String TPV_CURRENCY = "uji.tpv.currency";
	private static final String TPV_CODE = "uji.tpv.code";
	private static final String TPV_TERMINAL = "uji.tpv.terminal";
	private static final String TPV_TRANSACTION = "uji.tpv.transaction";
	private static final String TPV_NOMBRE = "uji.tpv.nombre";
	private static final String MAILING_CLASS = "uji.par.mail.class";
	private static final String TMP_FOLDER = "uji.tmp.folder";
	private static final String PGP_PASSPHRASE = "uji.pgp.key";
	private static final String CODIGO_BUZON = "uji.codigo.buzon";
	private static final String ACTIVE_DIRECTORY_IP = "activedirectory.ip";
	private static final String ACTIVE_DIRECTORY_Port = "activedirectory.port";
	private static final String ACTIVE_DIRECTORY_DOMAIN = "activedirectory.domain";
	private static final String ACTIVE_DIRECTORY_DC = "activedirectory.dc";
	private static final String IMAGEN_SUSTITUTIVA = "uji.reports.imagenSustitutiva";
	private static final String IMAGEN_SUSTITUTIVA_CONTENT_TYPE = "uji.reports.imagenSustitutivaContentType";
	private static final String IMAGEN_PUBLI_SUSTITUTIVA = "uji.reports.imagenPubliSustitutiva";
	private static final String IMAGEN_PUBLI_SUSTITUTIVA_CONTENT_TYPE = "uji.reports.imagenPubliSustitutivaContentType";
	private static final String PORCENTAJE_IVA_DEFECTO = "uji.par.porcentajeIvaDefecto";
    private static final String TIPOS_INFORME = "uji.reports.tipos";
	private static final String TIPOS_INFORME_GENERALES = "uji.reports.tiposGenerales";
    public static final String HORAS_VENTA_ANTICIPADA = "uji.reports.horaVentaAnticipada";
	private static final String ALLOW_MULTISESION = "uji.par.allowMultisesion";
	private static final String GENERAR_CIFRADO = "uji.pgp.generateCifrado";

    private static final String MENU_ABONO = "uji.par.menuAbonos";
    private static final String MENU_CLIENTES = "uji.par.menuClientes";
    private static final String MENU_ICAA = "uji.par.menuICAA";

	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	private static final String SHOW_SESIONES_SIN_VENTA_INTERNET = "uji.par.showSesionesSinVentaInternet";
    public static final String JSON_LOCALIZACIONES_PATH = "/etc/uji/par/butacas/";
	private static final String IS_LOADED_FROM_RESOURCE = "uji.par.isLoadedFromResource";

    private Properties properties;

	@Autowired
    public Configuration(ConfigurationInterface configurationInterface) throws IOException
    {
        properties = new Properties();
        properties.load(configurationInterface.getPathToFile());
    }

    private String getNoObligatoryProperty(String propertyName)
    {
        try
        {
            String property = getProperty(propertyName);
            return property;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private String getProperty(String propertyName)
    {
        return Configuration.getProperty(properties, propertyName);
    }

    static public String getProperty(Properties properties, String propertyName)
    {
        String value = properties.getProperty(propertyName);
        try
        {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        catch (NullPointerException e) {
            log.info("Propiedad " + propertyName + " nula");
            throw e;
        }

        return value.trim();
    }

    public boolean isIdEntrada()
    {
        String entradaId = getNoObligatoryProperty(ENTRADA_ID);
        if (entradaId != null && entradaId.length() > 0 && !entradaId.equals("false")) {
            try
            {
                Integer.parseInt(entradaId);
                return true;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public Boolean isMultipleTpvEnabled()
    {
        String tpv = getNoObligatoryProperty(TPV);
        if (tpv != null && tpv.length() > 0 && !tpv.equals("false")) {
                return true;
        }
        else {
            return false;
        }
    }

    public Integer getIdEntrada()
    {
        String entradaId = getNoObligatoryProperty(ENTRADA_ID);
        if (entradaId != null && entradaId.length() > 0) {
            try
            {
                return Integer.parseInt(entradaId);
            }
            catch (NumberFormatException e)
            {
                return 0;
            }
        }
        else {
            return 0;
        }
    }
    
    public String getPayModes(Locale locale)
    {
    	List<String> payModesJs = new ArrayList<String>(Arrays.asList("['metalico', '" + ResourceProperties.getProperty(locale, "paymode.metalico") + "']",
    	         "['tarjeta', '" + ResourceProperties.getProperty(locale, "paymode.tarjeta") + "']",
    	         "['tarjetaOffline', '" + ResourceProperties.getProperty(locale, "paymode.tarjetaOffline") + "']",
				 "['transferencia', '" + ResourceProperties.getProperty(locale, "paymode.transferencia") + "']"));
    	
    	List<String> modes = Arrays.asList(getProperty(PAY_MODES).split(PROPERTIES_SEPARATOR));
    	List<String> payModes = new ArrayList<String>();

		for (String mode : modes) {
    		for (String payModeJs : payModesJs) {
    			if (payModeJs.contains(mode)) {
    				payModes.add(payModeJs);
    				payModesJs.remove(payModeJs);
    				break;
    			}
    		}
    	}
    	
    	return "[" + StringUtils.join(payModes, PROPERTIES_SEPARATOR) + "]";
    }

    public String getSecret()
    {
        return getProperty(SECRET);
    }

    public String getMailHost()
    {
        return getProperty(MAIL_HOST);
    }

    public String getGastosGestion()
    {
        return getProperty(GASTOS_GESTION);
    }

    public String getCargoInformeEfectivo()
    {
        return getProperty(INFORME_EFECTIVO_CARGO);
    }
    
    public String getFirmanteInformeEfectivo()
    {
        return getProperty(INFORME_EFECTIVO_FIRMANTE);
    } 
    
    public int getMargenVentaTaquillaMinutos()
    {
        try {
            return Integer.parseInt(getProperty(MARGEN_VENTA_TAQUILLA_MINUTOS));
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public String getAuthClass()
    {
        return getProperty(AUTH_CLASS);
    }
    
    public List<String> getAdminLogin()
    {
        return Arrays.asList(getProperty(ADMIN_LOGIN).split(PROPERTIES_SEPARATOR));
    }
    
    public List<String> getAdminPassword()
    {
        return Arrays.asList(getProperty(ADMIN_PASSWORD).split(PROPERTIES_SEPARATOR));
    }

    public List<String> getUserReadonlyLogin()
    {
        String noObligatoryProperty = getNoObligatoryProperty(USER_READONLY_LOGIN);
        if (noObligatoryProperty != null)
        {
            return Arrays.asList(noObligatoryProperty.split(PROPERTIES_SEPARATOR));
        }
        else
        {
            return null;
        }
    }
    
    public List<String> getUserReadonlyPassword()
    {
        String noObligatoryProperty = getNoObligatoryProperty(USER_READONLY_PASSWORD);
        if (noObligatoryProperty != null)
        {
            return Arrays.asList(noObligatoryProperty.split(PROPERTIES_SEPARATOR));
        }
        else
        {
            return null;
        }
    }
    
    public String getJdbUrl()
    {
        return getProperty(JDBC_URL);
    }

    public String getSyncTipo()
    {
        return getProperty(SYNC_TIPO);
    }
    
    public String[] getSyncUrlsRss()
    {
        return getProperty(SYNC_URL_TIPO).split(PROPERTIES_SEPARATOR);
    }

    public String getSyncUrlsHeaderToken()
    {
        return getNoObligatoryProperty(SYNC_HEADER_TOKEN);
    }

    public String getSyncUrlsToken()
    {
        return getNoObligatoryProperty(SYNC_TOKEN);
    }
    
    public int getSyncHorasInicioVentaOnline()
    {
        try
        {
            return Integer.parseInt(getProperty(SYNC_HORAS_INICIO_VENTA_ONLINE));
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }
    
    public String[] getImagenesFondo() {
    	return getProperty(LOCALIZACIONES).split(PROPERTIES_SEPARATOR);
    }

	public String[] getLocalizacionesEnImagen(String localizacion) {
		return getProperty(LOCALIZACIONES + "." + localizacion).split(PROPERTIES_SEPARATOR);
	}

	public String getBarcodeWidthHeight() {
		return getProperty(BARCODE_WIDTH_HEIGHT);
	}

    public String getTpvLangCaCode() {
        return TPV_LANG_CA_CODE;
    }

    public String getTpvLangEsCode() {
        return TPV_LANG_ES_CODE;
    }

    public String getTpvOrderPrefixCodeCajamar() {
        return TPV_ORDER_PREFIX_CODE_CAJAMAR;
    }

	public String getTpvCurrency() {
		return getNoObligatoryProperty(TPV_CURRENCY);
	}

	public String getTpvCode() {
		return getNoObligatoryProperty(TPV_CODE);
	}

	public String getTpvTerminal() {
		return getNoObligatoryProperty(TPV_TERMINAL);
	}

	public String getTpvTransaction() {
		return getNoObligatoryProperty(TPV_TRANSACTION);
	}

	public String getTpvNombre() {
		return getNoObligatoryProperty(TPV_NOMBRE);
	}

    public String getInformeReport(String tipo) {
        return getProperty("uji.reports." + tipo + ".class");
    }

    public String getTiposInforme() {
        return getProperty(TIPOS_INFORME);
    }
	
	public String getMailingClass() {
		return getProperty(MAILING_CLASS);
	}

	public String getTmpFolder() {
		return getProperty(TMP_FOLDER);
	}

	public String getPassphrase() {
		return getProperty(PGP_PASSPHRASE);
	}

	public String getCodigoBuzon() {
		return getProperty(CODIGO_BUZON);
	}

	public String getDBUser() {
		return getProperty(DB_USER);
	}

	public String getDBPassword() {
		return getProperty(DB_PASS);
	}

	public String getActiveDirectoryIP() {
		return getProperty(ACTIVE_DIRECTORY_IP);
	}
	
	public String getActiveDirectoryPort() {
		return getProperty(ACTIVE_DIRECTORY_Port);
	}
	
	public String getActiveDirectoryDomain() {
		return getProperty(ACTIVE_DIRECTORY_DOMAIN);
	}
	
	public String getActiveDirectoryDC() {
		return getProperty(ACTIVE_DIRECTORY_DC);
	}

	public String getPathImagenSustitutiva() {
		return getNoObligatoryProperty(IMAGEN_SUSTITUTIVA);
	}

	public String getPathImagenPubliSustitutiva() {
		return getNoObligatoryProperty(IMAGEN_PUBLI_SUSTITUTIVA);
	}

	public String getImagenSustitutivaContentType() {
		return getNoObligatoryProperty(IMAGEN_SUSTITUTIVA_CONTENT_TYPE);
	}

	public String getImagenPubliSustitutivaContentType() {
		return getNoObligatoryProperty(IMAGEN_PUBLI_SUSTITUTIVA_CONTENT_TYPE);
	}

	public String getPorcentajeIvaDefecto() {
		return getNoObligatoryProperty(PORCENTAJE_IVA_DEFECTO);
	}
	
	public String getEnviarMailsEntradas() {
		return getNoObligatoryProperty(ENVIAR_MAILS_ENTRADAS);
	}

    public String getHorasVentaAnticipada() {
        return getNoObligatoryProperty(HORAS_VENTA_ANTICIPADA);
    }

	public boolean isDebug() {
		String debug = getNoObligatoryProperty(ENTORNO);
		if (debug == null || !debug.toLowerCase().equals("prod"))
			return true;
		else
			return false;
	}

	public boolean getAllowMultisesion() {
		String allowMultisesion = getNoObligatoryProperty(ALLOW_MULTISESION);

		if (allowMultisesion != null && allowMultisesion.equalsIgnoreCase("true"))
			return true;
		return false;
	}

	public boolean getGenerarCifrado() {
		String generarCifrado = getNoObligatoryProperty(GENERAR_CIFRADO);
		if (generarCifrado == null || generarCifrado.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}

	public String getTiposInformeGenerales() {
		String tiposInformeGenerales = getNoObligatoryProperty(TIPOS_INFORME_GENERALES);
		if (tiposInformeGenerales == null)
			tiposInformeGenerales = TipoInforme.getDefaultGenerales();
		return tiposInformeGenerales;
	}

    public boolean showSesionesSinVentaInternet() {
        String showSesionesSinVentaInternet = getNoObligatoryProperty(SHOW_SESIONES_SIN_VENTA_INTERNET);
        if (showSesionesSinVentaInternet == null)
            return true;
        else if (showSesionesSinVentaInternet != null && showSesionesSinVentaInternet.trim().equalsIgnoreCase("true"))
            return true;

        return false;
    }

    public String getPathJson() {
        return JSON_LOCALIZACIONES_PATH;
    }

    public boolean isMenuAbono() {
        String menuAbono = getNoObligatoryProperty(MENU_ABONO);
        if (menuAbono == null || !menuAbono.equalsIgnoreCase("true"))
            return false;
        else
            return true;
    }

    public boolean isMenuClientes() {
        String menuAbono = getNoObligatoryProperty(MENU_CLIENTES);
        if (menuAbono == null || !menuAbono.equalsIgnoreCase("true"))
            return false;
        else
            return true;
    }

    public boolean isMenuICAA() {
        String menuICAA = getNoObligatoryProperty(MENU_ICAA);
        if (menuICAA == null || !menuICAA.equalsIgnoreCase("true"))
            return false;
        else
            return true;
    }

	/**
	 * Se usa para mostrar eventos y sesiones como activos pasados X minutos de la hora del espectáculo
	 */
	public Date dateConMargenTrasVenta()
	{
		Calendar limite = Calendar.getInstance();
		limite.add(Calendar.MINUTE, - getMargenVentaTaquillaMinutos());

		return limite.getTime();
	}

	public boolean isDataDegradada(Timestamp data) {
		long aux = data.getTime() + (getMargenVentaTaquillaMinutos() * 60 * 1000);
		Timestamp timeAux = new Timestamp(aux);
		return (DateUtils.getCurrentDate().after(timeAux));
	}

	public Timestamp getDataTopePerASaberSiEsDegradada(Timestamp data) {
		long aux = data.getTime() + (getMargenVentaTaquillaMinutos() * 60 * 1000);
		return new Timestamp(aux);
	}

	public boolean isLoadedFromResource() {
		String isLoadedFromResource = getNoObligatoryProperty(IS_LOADED_FROM_RESOURCE);
		if (isLoadedFromResource == null || !isLoadedFromResource.equalsIgnoreCase("true"))
			return false;
		else
			return true;
	}

	public String getMailPassword()
	{
		return getProperty(MAIL_PASSWORD);
	}

	public String getMailUsername()
	{
		return getProperty(MAIL_USERNAME);
	}
}

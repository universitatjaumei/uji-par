package es.uji.apps.par.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration
{
    private static final String PROPERTIES_SEPARATOR = ",";
	private static final String SECRET = "uji.par.secret";
    private static final String URL_PUBLIC = "uji.par.urlPublic";
    private static final String LOCALIZACIONES = "uji.par.localizaciones";
    private static final String URL_ADMIN = "uji.par.urlAdmin";
    private static final String MAIL_HOST = "uji.par.mail.host";
    private static final String MAIL_FROM = "uji.par.mail.from";
    private static final String COMO_LLEGAR = "uji.par.urlComoLlegar";
    private static final String URL_CONDICIONES_PRIVACIDAD = "uji.par.urlCondicionesPrivacidad";
    private static final String GASTOS_GESTION = "uji.par.gastosGestion";
    private static final String ENVIAR_MAILS_ERROR = "uji.par.enviarMailsError";
    private static final String URL_PIE_ENTRADA = "uji.par.urlPieEntrada";
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
    private static final String SYNC_TIPO = "uji.sync.lugar";
    private static final String SYNC_URL_TIPO = "uji.sync.rss";
    private static final String SYNC_HORAS_INICIO_VENTA_ONLINE = "uji.sync.horasInicioVentaOnlineTrasCreacion";
    private static final String ENTRADA_TAQUILLA_REPORT = "uji.reports.entradaTaquillaReport.class";
    private static final String ENTRADA_ONLINE_REPORT = "uji.reports.entradaOnlineReport.class";
	private static final String INFORME_TAQUILLA_REPORT = "uji.reports.informeTaquilla.class";
	private static final String INFORME_EFECTIVO_REPORT = "uji.reports.informeEfectivo.class";
	private static final String INFORME_TAQUILLA_SUBTOTALES_TPV_REPORT = "uji.reports.informeTaquillaTpvSubtotales.class";
	private static final String INFORME_EVENTOS_REPORT = "uji.reports.informeEventos.class";
	private static final String BARCODE_WIDTH_HEIGHT = "uji.reports.barcodeWidthHeight";
	private static final String LOGO_REPORT = "uji.reports.logo";
	private static final String API_KEY = "api.key";
    

    public static Logger log = Logger.getLogger(Configuration.class);

    private static String fileName = "/etc/uji/par/app.properties";
    private Properties properties;
    private static Configuration instance;

    static
    {
        try
        {
            instance = new Configuration();
        }
        catch (IOException e)
        {
            log.error(e.toString());
        }
    }

    private Configuration() throws IOException
    {
        String filePath = fileName;
        File f = new File(filePath);

        properties = new Properties();
        FileInputStream fis = new FileInputStream(f);
        properties.load(fis);
    }

    public static void reinitConfig() throws IOException
    {
        instance = null;
        instance = new Configuration();
    }

    private static String getProperty(String propertyName)
    {
        String value = (String) instance.properties.getProperty(propertyName);
        try
        {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        
        return value.trim();
    }

    public static String getUrlPublic()
    {
        return getProperty(URL_PUBLIC);
    }
    
    public static String getUrlAdmin()
    {
        return getProperty(URL_ADMIN);
    }

    public static String getSecret()
    {
        return getProperty(SECRET);
    }

    public static String getMailHost()
    {
        return getProperty(MAIL_HOST);
    }

    public static String getMailFrom()
    {
        return getProperty(MAIL_FROM);
    }

    public static String getUrlComoLlegar()
    {
        return getProperty(COMO_LLEGAR);
    }

    public static String getUrlCondicionesPrivacidad()
    {
        return getProperty(URL_CONDICIONES_PRIVACIDAD);
    }

    public static String getGastosGestion()
    {
        return getProperty(GASTOS_GESTION);
    }
    
    public static String getEnviarMailsError()
    {
        return getProperty(ENVIAR_MAILS_ERROR);
    }

    public static void desactivaLogGmail()
    {
        instance.properties.setProperty(ENVIAR_MAILS_ERROR, "false");
    }
    
    public static String getUrlPieEntrada()
    {
        return getProperty(URL_PIE_ENTRADA);
    }
    
    public static String getEntorno()
    {
        return getProperty(ENTORNO);
    }  
    
    public static String getCargoInformeEfectivo()
    {
        return getProperty(INFORME_EFECTIVO_CARGO);
    }
    
    public static String getFirmanteInformeEfectivo()
    {
        return getProperty(INFORME_EFECTIVO_FIRMANTE);
    } 
    
    public static int getMargenVentaTaquillaMinutos()
    {
        try
        {
            return Integer.parseInt(getProperty(MARGEN_VENTA_TAQUILLA_MINUTOS));
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }
    
    public static String getAuthClass()
    {
        return getProperty(AUTH_CLASS);
    }
    
    public static String getEntradaTaquillaReport()
    {
        return getProperty(ENTRADA_TAQUILLA_REPORT);
    }
    
    public static List<String> getAdminLogin()
    {
        return Arrays.asList(getProperty(ADMIN_LOGIN).split(PROPERTIES_SEPARATOR));
    }
    
    public static List<String> getAdminPassword()
    {
        return Arrays.asList(getProperty(ADMIN_PASSWORD).split(PROPERTIES_SEPARATOR));
    }

    public static List<String> getUserReadonlyLogin()
    {
        return Arrays.asList(getProperty(USER_READONLY_LOGIN).split(PROPERTIES_SEPARATOR));
    }
    
    public static List<String> getUserReadonlyPassword()
    {
        return Arrays.asList(getProperty(USER_READONLY_PASSWORD).split(PROPERTIES_SEPARATOR));
    }
    
    public static String getJdbUrl()
    {
        return getProperty(JDBC_URL);
    }

    public static String getSyncTipo()
    {
        return getProperty(SYNC_TIPO);
    }
    
    public static String[] getSyncUrlsRss()
    {
        return getProperty(SYNC_URL_TIPO).split(PROPERTIES_SEPARATOR);
    }
    
    public static int getSyncHorasInicioVentaOnline()
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
    
    public static String[] getImagenesFondo() {
    	return getProperty(LOCALIZACIONES).split(PROPERTIES_SEPARATOR);
    }

	public static String[] getLocalizacionesEnImagen(String localizacion) {
		return getProperty(LOCALIZACIONES + "." + localizacion).split(PROPERTIES_SEPARATOR);
	}

	public static String getEntradaOnlineReport() {
		return getProperty(ENTRADA_ONLINE_REPORT);
	}

	public static String getInformeTaquillaReport() {
		return getProperty(INFORME_TAQUILLA_REPORT);
	}

	public static String getInformeEfectivoReport() {
		return getProperty(INFORME_EFECTIVO_REPORT);
	}

	public static String getInformeTaquillaTpvSubtotalesReport() {
		return getProperty(INFORME_TAQUILLA_SUBTOTALES_TPV_REPORT);
	}

	public static String getInformeEventosReport() {
		return getProperty(INFORME_EVENTOS_REPORT);
	}

	public static String getBarcodeWidthHeight() {
		return getProperty(BARCODE_WIDTH_HEIGHT);
	}

	public static String getLogoReport() {
		return getProperty(LOGO_REPORT);
	}

	public static String getApiKey() {
		return getProperty(API_KEY);
	}
}

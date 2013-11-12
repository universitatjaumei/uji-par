package es.uji.apps.par.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration
{
    private static final String SECRET = "uji.par.secret";
    private static final String URL_PUBLIC = "uji.par.urlPublic";
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
}

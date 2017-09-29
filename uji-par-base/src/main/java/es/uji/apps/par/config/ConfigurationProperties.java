package es.uji.apps.par.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationProperties implements ConfigurationSelector {
    private static final String URL_PUBLIC = "uji.par.urlPublic";
    private static final String URL_PUBLIC_SIN_HTTPS = "uji.par.urlPublicSinHTTPS";
    private static final String URL_PUBLIC_LIMPIO = "uji.par.urlPublicLimpio";
    private static final String URL_ADMIN = "uji.par.urlAdmin";
    private static final String HTML_TITLE = "uji.par.htmltitle";
    private static final String COMO_LLEGAR = "uji.par.urlComoLlegar";
    private static final String URL_CONDICIONES_PRIVACIDAD = "uji.par.urlCondicionesPrivacidad";
    private static final String MAIL_FROM = "uji.par.mail.from";
    private static final String URL_PIE_ENTRADA = "uji.par.urlPieEntrada";
    private static final String LOGO_REPORT = "uji.reports.logo";
    private static final String LOCATION_REPORT = "uji.reports.location";
    private static final String API_KEY = "api.key";
    private static final String LANGS_ALLOWED = "uji.par.langsAllowed";
    private static final String IDIOMA_POR_DEFECTO = "uji.par.defaultLang";

    private static final Logger log = LoggerFactory.getLogger(ConfigurationProperties.class);
    private static final String SHOW_BUTACAS_QUE_HAN_ENTRADO_EN_DISTINTO_COLOR =
        "uji.par.showButacasQueHanEntradoEnDistintoColor";

    private Properties properties;

    @Autowired
    public ConfigurationProperties(ConfigurationInterface configurationInterface) throws IOException {
        properties = new Properties();
        properties.load(configurationInterface.getPathToFile());
    }

    private String getProperty(String propertyName) {
        return Configuration.getProperty(properties, propertyName);
    }

    private String getNoObligatoryProperty(String propertyName) {
        try {
            String property = getProperty(propertyName);
            return property;
        } catch (Exception e) {
            return null;
        }
    }

    public String getUrlPublic() {
        return getProperty(URL_PUBLIC);
    }

    public String getUrlPublicSinHTTPS() {
        return getProperty(URL_PUBLIC_SIN_HTTPS);
    }

    public String getUrlPublicLimpio() {
        return getNoObligatoryProperty(URL_PUBLIC_LIMPIO);
    }

    public String getUrlAdmin() {
        return getProperty(URL_ADMIN);
    }

    public String getHtmlTitle() {
        return getProperty(HTML_TITLE);
    }

    public String getMailFrom() {
        return getProperty(MAIL_FROM);
    }

    public String getUrlComoLlegar() {
        return getProperty(COMO_LLEGAR);
    }

    public String getUrlCondicionesPrivacidad() {
        return getProperty(URL_CONDICIONES_PRIVACIDAD);
    }

    public String getUrlPieEntrada() {
        return getProperty(URL_PIE_ENTRADA);
    }

    public String getLogoReport() {
        return getProperty(LOGO_REPORT);
    }

    @Override
    public String getNombreMunicipio() {
        return getNoObligatoryProperty(LOCATION_REPORT);
    }

    public String getApiKey() {
        return getProperty(API_KEY);
    }

    @Override
    public String getLangsAllowed() {
        String langsAllowed = getNoObligatoryProperty(LANGS_ALLOWED);

        if (langsAllowed != null && langsAllowed.length() > 0)
            return langsAllowed;
        return "[{'locale':'ca', 'alias': 'Valencià'}]";
    }

    @Override
    public boolean getLocalizacionEnValenciano() {
        String langsAllowed = getLangsAllowed();
        return (langsAllowed.toUpperCase().contains("VALENCI") || langsAllowed.toUpperCase().contains("CATAL"));
    }

    @Override
    public String getIdiomaPorDefecto()
    {
        String lang = getNoObligatoryProperty(IDIOMA_POR_DEFECTO);
        if (lang != null && lang.length() > 0) {
            return lang;
        }
        else {
            return "ca";
        }
    }

    @Override
    public boolean showButacasHanEntradoEnDistintoColor() {
        String showButacasQueHanEntradoEnDistintoColor =
            getNoObligatoryProperty(SHOW_BUTACAS_QUE_HAN_ENTRADO_EN_DISTINTO_COLOR);
        return (showButacasQueHanEntradoEnDistintoColor != null && showButacasQueHanEntradoEnDistintoColor.toLowerCase()
            .equals("true")) ? true : false;
    }

    @Override
    public boolean showIVA() {
        return true;
    }
}

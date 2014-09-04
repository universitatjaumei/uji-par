package es.uji.apps.par.services.rest;

import java.text.ParseException;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.commons.web.template.model.GrupoMenu;
import es.uji.commons.web.template.model.ItemMenu;
import es.uji.commons.web.template.model.Menu;
import es.uji.commons.web.template.model.Pagina;

public class BaseResource
{
    protected static final String APP = "par";
    protected static final String LANG = "language";
    //private static final String API_KEY = "kajshdka234hsdoiuqhiu918092";
    private static final String API_KEY = Configuration.getApiKey();

    @Context
    HttpServletRequest currentRequest;

    protected Locale getLocale()
    {
    	HttpSession session = currentRequest.getSession();
    	String lang = (String) session.getAttribute(LANG);
    	
    	return getLocale(lang);
    }
    
    protected Locale getLocale(String lang)
    {
        String idiomaFinal = "ca";

        if (lang != null && lang.length() > 0)
        {
        	HttpSession session = currentRequest.getSession();
        	session.setAttribute(LANG, lang);
        	idiomaFinal = lang;
        }
        else if (currentRequest != null)
        {
            if (currentRequest.getCookies() != null)
            {
                for (Cookie cookie : currentRequest.getCookies())
                {
                    if (cookie != null && "uji-lang".equals(cookie.getName()))
                    {
                        String idiomaCookie = cookie.getValue();

                        if (esIdiomaValido(idiomaCookie))
                        {
                            idiomaFinal = idiomaCookie;
                            break;
                        }
                    }
                }
            }

            String idiomaParametro = currentRequest.getParameter("idioma");

            if (idiomaParametro != null)
            {
                if (esIdiomaValido(idiomaParametro))
                {
                    idiomaFinal = idiomaParametro;
                }
            }
        }

        return new Locale(idiomaFinal);
    }

    private boolean esIdiomaValido(String idioma)
    {
        return idioma.equals("es") || idioma.equals("ca");
    }

    protected String getBaseUrlPublic()
    {
        return Configuration.getUrlPublic();
    }
    
    
    //para evitar el problema de proxy de benicassim en las redirecciones absolutas
    //deberían arreglarlo ellos pero no lo quieren hacer
    protected String getBaseUrlPublicLimpio()
    {
    	String urlPublicLimpio = Configuration.getUrlPublicLimpio(); 
        urlPublicLimpio = (urlPublicLimpio == null)?Configuration.getUrlPublic():urlPublicLimpio;
        return urlPublicLimpio;
    }

    public Response errorResponse(String messageProperty, Object... values)
    {
        String errorMessage = getProperty(messageProperty, values);
        return Response.serverError().entity(new ResponseMessage(false, errorMessage)).build();
    }

    public String getProperty(String messageProperty, Object... values)
    {
        return ResourceProperties.getProperty(getLocale(), messageProperty, values);
    }

    protected Pagina buildPublicPageInfo(String urlBase, String url, String idioma) throws ParseException
    {
        Menu menu = new Menu();

        GrupoMenu grupo = new GrupoMenu("Comunicació");
        grupo.addItem(new ItemMenu("Noticies", "http://www.uji.es/"));
        grupo.addItem(new ItemMenu("Investigació", "http://www.uji.es/"));
        menu.addGrupo(grupo);

        Pagina pagina = new Pagina(urlBase, url, idioma);
        pagina.setTitulo(Configuration.getHtmlTitle());
        pagina.setSubTitulo("");
        pagina.setMenu(menu);

        return pagina;
    }

    protected boolean correctApiKey(HttpServletRequest request)
    {
        String key = request.getParameter("key");

        return API_KEY.equals(key);
    }

    protected Response apiAccessDenied()
    {
        return Response.status(Status.UNAUTHORIZED).build();
    }
}

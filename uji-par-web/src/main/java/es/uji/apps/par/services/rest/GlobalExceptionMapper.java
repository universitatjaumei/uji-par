package es.uji.apps.par.services.rest;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.config.Configuration;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Context
    HttpServletRequest currentRequest;
    
    protected static final String APP = "par";
    protected static final String LANG = "language";
    
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

	@Override
	public Response toResponse(Exception arg0) {
		
        Locale locale = getLocale();
        String language = locale.getLanguage();
        
		Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR
				+ "error", locale, APP);

		HashMap<String, String> titulo = new HashMap<String, String>();
		titulo.put("titulo", "Error");
		template.put("pagina", titulo);
		template.put("baseUrl", Configuration.getUrlPublic());
		template.put("lang", language);

		return Response.ok(template).build();
	}
}

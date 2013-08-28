package es.uji.apps.par.services.rest;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.i18n.ResourceProperties;

public class BaseResource
{
    private static final String IDIOMA = "idioma";
    protected static final String APP = "par";

    @Context
    HttpServletRequest currentRequest;

    protected Locale getLocale()
    {
        String idiomaParametro = currentRequest.getParameter(IDIOMA);
        String idiomaSesion = (String) currentRequest.getSession().getAttribute(IDIOMA);
        String idioma = "es";

        if (idiomaParametro != null)
        {
            if (esIdiomaValido(idiomaParametro))
            {
                idioma = idiomaParametro;
                currentRequest.getSession().setAttribute(IDIOMA, idiomaParametro);
            }
        }
        else if (idiomaSesion != null)
        {
            if (esIdiomaValido(idiomaSesion))
            {
                idioma = idiomaSesion;
            }
        }

        return new Locale(idioma);
    }

    private boolean esIdiomaValido(String idioma)
    {
        return idioma.equals("es") || idioma.equals("ca");
    }

    protected String getBaseUrl()
    {
        return currentRequest.getScheme() + "://" + currentRequest.getServerName() + ":" + currentRequest.getServerPort() + currentRequest.getContextPath();
    }  
    
    public Response errorResponse(String messageProperty, Object ... values)
    {
        String errorMessage = getProperty(messageProperty, values);
        return Response.serverError().entity(new ResponseMessage(false, errorMessage)).build();
    }
    
    public String getProperty(String messageProperty, Object ... values)
    {
        return ResourceProperties.getProperty(getLocale(), messageProperty, values);
    }
}

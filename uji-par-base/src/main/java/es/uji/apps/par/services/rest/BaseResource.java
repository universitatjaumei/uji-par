package es.uji.apps.par.services.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.commons.web.template.model.GrupoMenu;
import es.uji.commons.web.template.model.ItemMenu;
import es.uji.commons.web.template.model.Menu;
import es.uji.commons.web.template.model.Pagina;

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
    
    protected String getUrlBase(HttpServletRequest clientRequest) throws MalformedURLException
    {
        String urlReference = clientRequest.getRequestURL().toString();

        URL result = new URL(urlReference);
        int port = result.getPort();

        if (port <= 0)
        {
            port = 80;
        }

        return MessageFormat.format("{0}://{1}:{2,number,#}", result.getProtocol(),
                result.getHost(), port);
    }

    protected Pagina buildPublicPageInfo(String urlBase, String url, String idioma) throws ParseException
    {
        Menu menu = new Menu();

        GrupoMenu grupo = new GrupoMenu("Comunicació");
        grupo.addItem(new ItemMenu("Noticies", "http://www.uji.es/"));
        grupo.addItem(new ItemMenu("Investigació", "http://www.uji.es/"));
        menu.addGrupo(grupo);

        Pagina pagina = new Pagina(urlBase, url, idioma);
        pagina.setTitulo("Paranimf");
        pagina.setSubTitulo("");
        pagina.setMenu(menu);

        return pagina;
    }
}

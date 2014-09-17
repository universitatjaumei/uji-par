package es.uji.apps.par.services.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.auth.Authenticator;
import es.uji.apps.par.config.Configuration;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("index")
public class IndexResource extends BaseResource
{
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Template index() throws Exception
    {
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "admin", getLocale(), APP);

        template.put("user", currentRequest.getSession().getAttribute(Authenticator.USER_ATTRIBUTE));
        template.put("urlPublic", Configuration.getUrlPublic());
        template.put("payModes", Configuration.getPayModes());
        
        return template;
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Template indexPost() throws Exception
    {
        return index();
    }
}
package es.uji.apps.par.services.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import es.uji.apps.par.exceptions.Constantes;
import es.uji.apps.par.auth.Authenticator;
import es.uji.apps.par.config.Configuration;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("index")
public class IndexResource extends BaseResource
{
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Template index(@QueryParam("lang") String lang) throws Exception
    {
        if (lang != null)
        {
            setLocale(lang);
        }

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "admin", getLocale(), APP);

        template.put("user", currentRequest.getSession().getAttribute(Authenticator.USER_ATTRIBUTE));
        template.put("urlPublic", Configuration.getUrlPublic());
		template.put("allowMultisesion", Configuration.getAllowMultisesion());
        template.put("payModes", Configuration.getPayModes(getLocale()));
        template.put("lang", getLocale().getLanguage());

		Boolean b = (Boolean) currentRequest.getSession().getAttribute(Authenticator.READONLY_ATTRIBUTE);
		boolean readOnlyUser = (b == null || !b)?false:true;
		template.put("readOnlyUser", readOnlyUser);
        template.put("langsAllowed", Configuration.getLangsAllowed());

        return template;
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Template indexPost() throws Exception
    {
        return index(null);
    }
}

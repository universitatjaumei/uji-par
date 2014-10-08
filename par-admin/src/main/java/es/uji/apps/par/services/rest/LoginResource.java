package es.uji.apps.par.services.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jasypt.util.password.BasicPasswordEncryptor;

import es.uji.apps.par.exceptions.Constantes;
import es.uji.apps.par.config.Configuration;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("login")
public class LoginResource extends BaseResource
{
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Template index(@QueryParam("error") boolean error) throws Exception
    {
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "login", getLocale(), APP);

        
        template.put("urlPublic", Configuration.getUrlPublic());
        template.put("error", error);

        return template;
    }
    
    @GET
    @Path("generatepassword")
    @Produces(MediaType.TEXT_PLAIN)
    public Response generatePassword(@QueryParam("password") String txtCleanPassword) throws Exception
    {
    	BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        return Response.ok(encryptor.encryptPassword(txtCleanPassword)).build();
    }
}

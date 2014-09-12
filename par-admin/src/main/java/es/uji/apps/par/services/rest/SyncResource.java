package es.uji.apps.par.services.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.services.EventosSyncService;

@Path("sync")
public class SyncResource extends BaseResource
{
	@InjectParam
	EventosSyncService eventosSyncService;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response syncronizaDesdeURL() throws MalformedURLException, JAXBException, IOException, ParseException
    {
    	for (String urlRss:Configuration.getSyncUrlsRss())
    		eventosSyncService.sync(new URL(urlRss).openStream());
        return Response.ok().build();
    }
}
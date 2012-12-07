package es.uji.apps.par.services.rest;

import java.net.URI;
import java.util.Collections;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.services.LocalizacionesService;

@Path("localizacion")
public class LocalizacionesResource
{
    @InjectParam
    private LocalizacionesService localizacionesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok().entity(new RestResponse(true, localizacionesService.get())).build();
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") String id)
    {
        localizacionesService.remove(Integer.parseInt(id));
        return Response.ok().entity(new RestResponse(true)).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Localizacion localizacion) throws GeneralPARException
    {
        Localizacion newLocalizacion = localizacionesService.add(localizacion);
        //TODO crear URI
        return Response.created(URI.create("")).entity(new RestResponse(true, Collections.singletonList(newLocalizacion))).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Localizacion localizacion) throws GeneralPARException
    {
        localizacionesService.update(localizacion);
        return Response.ok().entity(new RestResponse(true, Collections.singletonList(localizacion))).build();
    }    
}

package es.uji.apps.par.services.rest;

import java.net.URI;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.exceptions.GeneralPARException;
import es.uji.apps.par.auth.AuthChecker;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.services.LocalizacionesService;

@Path("localizacion")
public class LocalizacionesResource
{
    @InjectParam
    private LocalizacionesService localizacionesService;
    
    @Context
    HttpServletRequest currentRequest;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit, 
    		@QueryParam("sala") Integer salaId)
    {
        return Response.ok().entity(new RestResponse(true, localizacionesService.get(sort, start, limit, salaId), 
        		localizacionesService.getTotalLocalizaciones(salaId))).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") String id)
    {
        AuthChecker.canWrite(currentRequest);
        
        localizacionesService.remove(Integer.parseInt(id));
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Localizacion localizacion) throws GeneralPARException
    {
        AuthChecker.canWrite(currentRequest);
        
        Localizacion newLocalizacion = localizacionesService.add(localizacion);
        // TODO crear URI
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Arrays.asList(newLocalizacion), 1)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Localizacion localizacion) throws GeneralPARException
    {
        AuthChecker.canWrite(currentRequest);
        
        localizacionesService.update(localizacion);
        return Response.ok()
                .entity(new RestResponse(true, Arrays.asList(localizacion), 1)).build();
    }
}

package es.uji.apps.par.services.rest;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.model.Tarifa;
import es.uji.apps.par.services.TarifasService;

@Path("tarifa")
public class TarifasResource
{
    @InjectParam
    private TarifasService tarifasService;
    
    @Context
    HttpServletRequest currentRequest;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("sort") @DefaultValue("[{\"property\":\"nombre\",\"direction\":\"ASC\"}]") String sort, 
    		@QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
        List<Tarifa> tarifas = tarifasService.getAll(sort, start, limit);
        return Response.ok().entity(new RestResponse(true, tarifas, tarifas.size())).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTarifa(Tarifa tarifa) throws GeneralPARException
    {
        tarifa = tarifasService.add(tarifa);

        // TODO -> crear URL
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Arrays.asList(tarifa), 1)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Tarifa tarifa) throws GeneralPARException
    {
    	//TODO
        //AuthChecker.canWrite(currentRequest);

        tarifa = tarifasService.update(tarifa);

        return Response.ok()
                .entity(new RestResponse(true, Collections.singletonList(tarifa), 1)).build();
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Integer id)
    {
    	//TODO
        //AuthChecker.canWrite(currentRequest);
        
        tarifasService.removeTarifa(id);
        return Response.ok().entity(new RestResponse(true)).build();
    }
}

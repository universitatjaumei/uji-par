package es.uji.apps.par.services.rest;

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

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.model.ParTipoEvento;
import es.uji.apps.par.services.TiposEventosService;

@Path("tiposeventos")
public class TiposEventosResource
{
    @InjectParam
    private TiposEventosService tiposEventosService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse getAll()
    {
        return new RestResponse(true, tiposEventosService.getTiposEventos());
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse remove(@PathParam("id") String id)
    {
        tiposEventosService.removeTipoEvento(Integer.parseInt(id));
        return new RestResponse(true);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse add(ParTipoEvento tipoEvento)
    {
        ParTipoEvento newTipoEvento = tiposEventosService.addTipoEvento(tipoEvento);
        return new RestResponse(true, Collections.singletonList(newTipoEvento));
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse update(ParTipoEvento tipoEvento)
    {
        tiposEventosService.updateTipoEvento(tipoEvento);
        return new RestResponse(true, Collections.singletonList(tipoEvento));
    }    
}

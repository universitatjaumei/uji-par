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

import es.uji.apps.par.exceptions.ParException;
import es.uji.apps.par.model.ParEvento;
import es.uji.apps.par.model.ParSesion;
import es.uji.apps.par.services.EventosService;
import es.uji.apps.par.services.SesionesService;

@Path("evento")
public class EventosResource
{
    @InjectParam
    private EventosService eventosService;
    
    @InjectParam
    private SesionesService sesionesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok().entity(new RestResponse(true, eventosService.getEventos())).build();
    }
    
    @GET
    @Path("{id}/sesiones")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse getSesiones(@PathParam("id") Integer eventoId)
    {
        return new RestResponse(true, sesionesService.getSesiones(eventoId));
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") String id)
    {
        eventosService.removeEvento(Integer.parseInt(id));
        return Response.ok().entity(new RestResponse(true)).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(ParEvento evento) throws ParException
    {
        ParEvento newEvento = eventosService.addEvento(evento);
        
        //TODO -> crear URL
        return Response.created(URI.create("")).entity(new RestResponse(true, Collections.singletonList(newEvento))).build();
    }
    
    @POST
    @Path("{id}/sesiones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse add(@PathParam("id") String eventoId, ParSesion sesion)
    {
    	ParSesion newSesion = sesionesService.addSesion(Long.parseLong(eventoId), sesion);
        return new RestResponse(true, Collections.singletonList(newSesion));
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(ParEvento evento) throws ParException
    {
        eventosService.updateEvento(evento);
        return Response.ok().entity(new RestResponse(true, Collections.singletonList(evento))).build();
    }
    
    @PUT
    @Path("{id}/sesiones/{sesionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse update(@PathParam("id") String eventoId, @PathParam("sesionId") String sesionId, ParSesion sesion)
    {
    	sesion.setId(Long.valueOf(sesionId));
        sesionesService.updateSesion(Long.parseLong(eventoId), sesion);
        return new RestResponse(true, Collections.singletonList(sesion));
    }
    
    @DELETE
    @Path("{id}/sesiones/{sesionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse remove(@PathParam("id") String eventoId, @PathParam("sesionId") String sesionId)
    {
        sesionesService.removeSesion(Integer.parseInt(sesionId));
        return new RestResponse(true);
    }    
}

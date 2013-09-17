package es.uji.apps.par.services.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sesion;
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
    public Response getAll(@QueryParam("activos") boolean activos, @QueryParam("sort") String sort, 
    		@QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
        List<Evento> eventos;
        int total = 0;
        
        if (activos) {
            eventos = eventosService.getEventosActivos(sort, start, limit);
            total = eventosService.getTotalEventosActivos();
        }
        else {
            eventos = eventosService.getEventos(sort, start, limit);
            total = eventosService.getTotalEventos();
        }
                
        return Response.ok().entity(new RestResponse(true, eventos, total)).build();
    }

    @GET
    @Path("{id}/imagen")
    public Response getImagenEvento(@PathParam("id") Integer eventoId)
    {
        try
        {
            Evento evento = eventosService.getEvento(eventoId);

            return Response.ok(evento.getImagen()).type(evento.getImagenContentType()).build();
        }
        catch (EventoNoEncontradoException e)
        {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("{id}/sesiones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSesiones(@PathParam("id") Integer eventoId, @QueryParam("activos") boolean activos,
    		@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
        List<Sesion> sesiones;
        int total;
        
        if (activos) {
            sesiones = sesionesService.getSesionesActivasDateEnSegundos(eventoId, sort, start, limit);
            total = sesionesService.getTotalSesionesActivas(eventoId);
        }
        else {
            sesiones = sesionesService.getSesionesDateEnSegundos(eventoId, sort, start, limit);
            total = sesionesService.getTotalSesiones(eventoId);
        }
        
        
        return Response.ok().entity(new RestResponse(true, sesiones, total)).build();
    }
    
    @GET
    @Path("{eventoId}/sesiones/{sesionId}/precios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreciosSesion(@PathParam("eventoId") Integer eventoId, @PathParam("sesionId") Long sesionId, 
    		@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
        return Response.ok().entity(new RestResponse(true, 
        		sesionesService.getPreciosSesion(sesionId, sort, start, limit), 
        		sesionesService.getTotalPreciosSesion(sesionId)))
                .build();
    }

    @POST
    @Path("{id}/sesiones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSesion(@PathParam("id") Integer eventoId, Sesion sesion) throws GeneralPARException
    {
        Sesion newSesion = sesionesService.addSesion(eventoId, sesion);
        //TODO -> crear URL
        return Response.created(URI.create("")).entity(new RestResponse(true, Arrays.asList(newSesion), 1))
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, @FormParam("porcentajeIVA") BigDecimal porcentajeIVA,
            @FormParam("retencionSGAE") BigDecimal retencionSGAE,
            @FormParam("ivaSGAE") BigDecimal ivaSGAE) throws GeneralPARException
    {
        Evento evento = new Evento();
        evento.setRetencionSGAE(retencionSGAE);
        evento.setIvaSGAE(ivaSGAE);
        evento.setPorcentajeIVA(porcentajeIVA);
        evento.setId(id);
        eventosService.updateEvento(evento);

        return Response.ok().entity(new RestResponse(true, Arrays.asList(evento), 1)).build();
    }

    @PUT
    @Path("{id}/sesiones/{sesionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer eventoId,
            @PathParam("sesionId") Integer sesionId, Sesion sesion) throws GeneralPARException
    {
        sesion.setId(sesionId);
        sesionesService.updateSesion(eventoId, sesion);
        return Response.ok().entity(new RestResponse(true, Arrays.asList(sesion), 1))
                .build();
    }

    @DELETE
    @Path("{id}/sesiones/{sesionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Integer eventoId,
            @PathParam("sesionId") Integer sesionId)
    {
        sesionesService.removeSesion(sesionId);
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @DELETE
    @Path("{id}/imagen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeImagen(@PathParam("id") Integer eventoId)
    {
        eventosService.removeImagen(eventoId);
        return Response.ok().entity(new RestResponse(true)).build();
    }
}

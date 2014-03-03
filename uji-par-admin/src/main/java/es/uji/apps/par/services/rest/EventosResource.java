package es.uji.apps.par.services.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
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
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.auth.AuthChecker;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.EventosService;
import es.uji.apps.par.services.LocalizacionesService;
import es.uji.apps.par.services.SesionesService;

@Path("evento")
public class EventosResource
{
    @InjectParam
    private EventosService eventosService;

    @InjectParam
    private SesionesService sesionesService;
    
    @InjectParam
    private LocalizacionesService localizacionesService;
    
    @Context
    HttpServletRequest currentRequest;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("activos") boolean activos, 
    		@QueryParam("sort") @DefaultValue("[{\"property\":\"tituloVa\",\"direction\":\"ASC\"}]") String sort, 
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
    public Response getImagenEvento(@PathParam("id") Long eventoId)
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
    public Response getSesiones(@PathParam("id") Long eventoId, @QueryParam("activos") boolean activos,
    		@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
        List<Sesion> sesiones;
        int total;
        
        if (activos) {
            sesiones = sesionesService.getSesionesActivasConVendidasDateEnSegundos(eventoId, sort, start, limit);
            total = sesionesService.getTotalSesionesActivas(eventoId);
        }
        else {
            sesiones = sesionesService.getSesionesConVendidasDateEnSegundos(eventoId, sort, start, limit);
            total = sesionesService.getTotalSesiones(eventoId);
        }
        
        
        return Response.ok().entity(new RestResponse(true, sesiones, total)).build();
    }
    
    @GET
    @Path("sesionesficheros")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSesionesCinePorFechas(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFin") String fechaFin,
    		@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
    	List<Sesion> sesiones = sesionesService.getSesionesCinePorFechas(fechaInicio, fechaFin, sort);
        return Response.ok().entity(new RestResponse(true, sesiones, sesiones.size())).build();
    }
    
    @PUT
    @Path("sesionesficheros/{sesionId}/incidencia/{incidenciaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setIncidencia(@PathParam("sesionId") long sesionId, @PathParam("incidenciaId") int incidenciaId) {
    	sesionesService.setIncidencia(sesionId, incidenciaId);
    	
    	return Response.ok().entity(new RestResponse(true)).build();
    }
    
    @GET
    @Path("{eventoId}/sesiones/{sesionId}/precios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreciosSesion(@PathParam("eventoId") Integer eventoId, @PathParam("sesionId") Long sesionId, 
    		@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit)
    {
        return Response.ok().entity(new RestResponse(true, 
        		sesionesService.getPreciosSesion(sesionId, sort, start, limit, true), 
        		sesionesService.getTotalPreciosSesion(sesionId)))
                .build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEvento(@FormDataParam("tituloEs") String tituloEs,
            @FormDataParam("descripcionEs") String descripcionEs,
            @FormDataParam("companyiaEs") String companyiaEs,
            @FormDataParam("interpretesEs") String interpretesEs,
            @FormDataParam("duracionEs") String duracionEs,
            @FormDataParam("dataBinary") byte[] dataBinary,
            @FormDataParam("dataBinary") FormDataContentDisposition dataBinaryDetail,
            @FormDataParam("dataBinary") FormDataBodyPart imagenBodyPart,
            @FormDataParam("premiosEs") String premiosEs,
            @FormDataParam("caracteristicasEs") String caracteristicasEs,
            @FormDataParam("comentariosEs") String comentariosEs,
            @FormDataParam("tipoEvento") Integer tipoEventoId,
            @FormDataParam("tituloVa") String tituloVa,
            @FormDataParam("descripcionVa") String descripcionVa,
            @FormDataParam("companyiaVa") String companyiaVa,
            @FormDataParam("interpretesVa") String interpretesVa,
            @FormDataParam("duracionVa") String duracionVa,
            @FormDataParam("premiosVa") String premiosVa,
            @FormDataParam("caracteristicasVa") String caracteristicasVa,
            @FormDataParam("comentariosVa") String comentariosVa,
            @FormDataParam("porcentajeIVA") BigDecimal porcentajeIVA,
            @FormDataParam("retencionSGAE") BigDecimal retencionSGAE,
            @FormDataParam("ivaSGAE") BigDecimal ivaSGAE,
            @FormDataParam("asientosNumerados") Boolean asientosNumerados,
            @FormDataParam("rssId") String rssId,
            @FormDataParam("expediente") String expediente,
            @FormDataParam("codigoDistribuidora") String codigoDistribuidora,
            @FormDataParam("nombreDistribuidora") String nombreDistribuidora,
            @FormDataParam("nacionalidad") String nacionalidad,
            @FormDataParam("vo") String vo,
            @FormDataParam("metraje") String metraje,
            @FormDataParam("subtitulos") String subtitulos) throws GeneralPARException
    {
        String nombreArchivo = (dataBinaryDetail != null) ? dataBinaryDetail.getFileName() : "";
        String mediaType = (imagenBodyPart != null) ? imagenBodyPart.getMediaType().toString() : "";

        Evento evento = new Evento(rssId, tituloEs, descripcionEs, companyiaEs, interpretesEs, duracionEs,
                premiosEs, caracteristicasEs, comentariosEs, tituloVa, descripcionVa, companyiaVa,
                interpretesVa, duracionVa, premiosVa, caracteristicasVa, comentariosVa, dataBinary,
                nombreArchivo, mediaType, tipoEventoId, porcentajeIVA, retencionSGAE, ivaSGAE, asientosNumerados, 
                expediente, codigoDistribuidora, nombreDistribuidora, nacionalidad, vo, metraje, subtitulos);
        Evento newEvento = eventosService.addEvento(evento);

        // TODO -> crear URL
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Arrays.asList(newEvento), 1)).build();
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

    @POST
    @Path("{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, @FormDataParam("tituloEs") String tituloEs,
            @FormDataParam("descripcionEs") String descripcionEs,
            @FormDataParam("companyiaEs") String companyiaEs,
            @FormDataParam("interpretesEs") String interpretesEs,
            @FormDataParam("duracionEs") String duracionEs,
            @FormDataParam("dataBinary") byte[] dataBinary,
            @FormDataParam("dataBinary") FormDataContentDisposition dataBinaryDetail,
            @FormDataParam("dataBinary") FormDataBodyPart imagenBodyPart,
            @FormDataParam("premiosEs") String premiosEs,
            @FormDataParam("caracteristicasEs") String caracteristicasEs,
            @FormDataParam("comentariosEs") String comentariosEs,
            @FormDataParam("tipoEvento") Integer tipoEventoId,
            @FormDataParam("tituloVa") String tituloVa,
            @FormDataParam("descripcionVa") String descripcionVa,
            @FormDataParam("companyiaVa") String companyiaVa,
            @FormDataParam("interpretesVa") String interpretesVa,
            @FormDataParam("duracionVa") String duracionVa,
            @FormDataParam("premiosVa") String premiosVa,
            @FormDataParam("caracteristicasVa") String caracteristicasVa,
            @FormDataParam("comentariosVa") String comentariosVa,
            @FormDataParam("porcentajeIVA") BigDecimal porcentajeIVA,
            @FormDataParam("retencionSGAE") BigDecimal retencionSGAE,
            @FormDataParam("ivaSGAE") BigDecimal ivaSGAE,
            @FormDataParam("asientosNumerados") Boolean asientosNumerados,
            @FormDataParam("rssId") String rssId,
            @FormDataParam("expediente") String expediente,
            @FormDataParam("codigoDistribuidora") String codigoDistribuidora,
            @FormDataParam("nombreDistribuidora") String nombreDistribuidora,
            @FormDataParam("nacionalidad") String nacionalidad,
            @FormDataParam("vo") String vo,
            @FormDataParam("metraje") String metraje,
            @FormDataParam("subtitulos") String subtitulos) throws GeneralPARException
    {
        AuthChecker.canWrite(currentRequest);
        
        String nombreArchivo = (dataBinaryDetail != null) ? dataBinaryDetail.getFileName() : "";
        String mediaType = (imagenBodyPart != null) ? imagenBodyPart.getMediaType().toString() : "";

        Evento evento = new Evento(rssId, tituloEs, descripcionEs, companyiaEs, interpretesEs, duracionEs,
                premiosEs, caracteristicasEs, comentariosEs, tituloVa, descripcionVa, companyiaVa,
                interpretesVa, duracionVa, premiosVa, caracteristicasVa, comentariosVa, dataBinary,
                nombreArchivo, mediaType, tipoEventoId, porcentajeIVA, retencionSGAE, ivaSGAE, asientosNumerados, 
                expediente, codigoDistribuidora, nombreDistribuidora, nacionalidad, vo, metraje, subtitulos);
        
        evento.setId(id);
        eventosService.updateEvento(evento);

        // no devolvemos el evento porque al enviar la imagen colgaba el navegador durante un tiempo
        return Response.ok()
                .entity(new RestResponse(true/* , Collections.singletonList(evento) */)).build();
    }

    @PUT
    @Path("{id}/sesiones/{sesionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer eventoId,
            @PathParam("sesionId") Integer sesionId, Sesion sesion) throws GeneralPARException
    {
        AuthChecker.canWrite(currentRequest);
        
        sesion.setId(sesionId);
        sesionesService.updateSesion(eventoId, sesion);
        return Response.ok().entity(new RestResponse(true, Arrays.asList(sesion), 1))
                .build();
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Integer id)
    {
        AuthChecker.canWrite(currentRequest);
        
        eventosService.removeEvento(id);
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @DELETE
    @Path("{id}/sesiones/{sesionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Integer eventoId,
            @PathParam("sesionId") Integer sesionId)
    {
        AuthChecker.canWrite(currentRequest);
        
        sesionesService.removeSesion(sesionId);
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @DELETE
    @Path("{id}/imagen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeImagen(@PathParam("id") Integer eventoId)
    {
        AuthChecker.canWrite(currentRequest);
        
        eventosService.removeImagen(eventoId);
        return Response.ok().entity(new RestResponse(true)).build();
    }
    
    @GET
    @Path("sesion/{sesionId}/localizacion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocalizacionesSesion(@PathParam("sesionId") Long sesionId)
    {
        return Response.ok().entity(
        	localizacionesService.getLocalizacionesSesion(sesionId)
        ).build();
    }
}

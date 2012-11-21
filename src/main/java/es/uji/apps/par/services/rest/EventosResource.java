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
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import es.uji.apps.par.exceptions.ParException;
import es.uji.apps.par.exceptions.ParImagenNotFoundException;
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
    @Path("{id}/imagen")
    public Response getImagenEvento(@PathParam("id") Integer eventoId) {
    	try {
    		ParEvento evento = eventosService.getEvento(eventoId);
        	
        	return Response.ok(evento.getImagen()).type(evento.getImagenContentType()).build();
    	} catch (ParImagenNotFoundException e) {
    		return Response.noContent().build();
    	}
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
    public Response remove(@PathParam("id") Integer id)
    {
        eventosService.removeEvento(id);
        return Response.ok().entity(new RestResponse(true)).build();
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@FormDataParam("titulo") String titulo, @FormDataParam("descripcion") String descripcion, 
    		@FormDataParam("companyia") String companyia, @FormDataParam("interpretes") String interpretes, 
    		@FormDataParam("duracion") String duracion, @FormDataParam("dataBinary") byte[] dataBinary,
    		@FormDataParam("dataBinary") FormDataContentDisposition dataBinaryDetail, @FormDataParam("dataBinary") FormDataBodyPart imagenBodyPart,
    		@FormDataParam("premios") String premios, @FormDataParam("caracteristicas") String caracteristicas,
    		@FormDataParam("comentarios") String comentarios, @FormDataParam("tipoEvento") Integer tipoEventoId) throws ParException
    {
    	String nombreArchivo = (dataBinaryDetail != null)?dataBinaryDetail.getFileName():"";
    	String mediaType = (imagenBodyPart != null)?imagenBodyPart.getMediaType().toString():"";
    	
    	ParEvento evento = new ParEvento(titulo, descripcion, companyia, interpretes, duracion, dataBinary, nombreArchivo, 
    			mediaType, premios, caracteristicas, comentarios, tipoEventoId);
        ParEvento newEvento = eventosService.addEvento(evento);
        
        //TODO -> crear URL
        return Response.created(URI.create("")).entity(new RestResponse(true, Collections.singletonList(newEvento))).build();
    }
    
    @POST
    @Path("{id}/sesiones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse add(@PathParam("id") Integer eventoId, ParSesion sesion)
    {
    	ParSesion newSesion = sesionesService.addSesion(eventoId, sesion);
        return new RestResponse(true, Collections.singletonList(newSesion));
    }
    
    @POST
    @Path("{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, @FormDataParam("titulo") String titulo, @FormDataParam("descripcion") String descripcion, 
    		@FormDataParam("companyia") String companyia, @FormDataParam("interpretes") String interpretes, 
    		@FormDataParam("duracion") String duracion, @FormDataParam("dataBinary") byte[] dataBinary,
    		@FormDataParam("dataBinary") FormDataContentDisposition dataBinaryDetail, @FormDataParam("dataBinary") FormDataBodyPart imagenBodyPart,
    		@FormDataParam("premios") String premios, @FormDataParam("caracteristicas") String caracteristicas,
    		@FormDataParam("comentarios") String comentarios, @FormDataParam("tipoEvento") Integer tipoEventoId) throws ParException
    {
    	String nombreArchivo = (dataBinaryDetail != null)?dataBinaryDetail.getFileName():"";
    	String mediaType = (imagenBodyPart != null)?imagenBodyPart.getMediaType().toString():"";
    	
    	ParEvento evento = new ParEvento(titulo, descripcion, companyia, interpretes, duracion, dataBinary, nombreArchivo, 
    			mediaType, premios, caracteristicas, comentarios, tipoEventoId);
    			
    	evento.setId(id);
        eventosService.updateEvento(evento);
        return Response.ok().entity(new RestResponse(true, Collections.singletonList(evento))).build();
    }
    
    @PUT
    @Path("{id}/sesiones/{sesionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse update(@PathParam("id") Integer eventoId, @PathParam("sesionId") Integer sesionId, ParSesion sesion)
    {
    	sesion.setId(sesionId);
        sesionesService.updateSesion(eventoId, sesion);
        return new RestResponse(true, Collections.singletonList(sesion));
    }
    
    @DELETE
    @Path("{id}/sesiones/{sesionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse remove(@PathParam("id") Integer eventoId, @PathParam("sesionId") Integer sesionId)
    {
        sesionesService.removeSesion(sesionId);
        return new RestResponse(true);
    }    
}

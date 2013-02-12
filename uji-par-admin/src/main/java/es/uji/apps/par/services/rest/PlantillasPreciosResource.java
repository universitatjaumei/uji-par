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
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.PreciosPlantilla;
import es.uji.apps.par.services.PlantillasService;
import es.uji.apps.par.services.PreciosPlantillaService;

@Path("plantillaprecios")
public class PlantillasPreciosResource {

	@InjectParam
    private PlantillasService plantillaPreciosService;
	
	@InjectParam
	private PreciosPlantillaService preciosService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok().entity(new RestResponse(true, plantillaPreciosService.getAll())).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") String id)
    {
    	plantillaPreciosService.remove(Integer.parseInt(id));
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Plantilla plantillaPrecios) throws GeneralPARException
    {
        Plantilla newPlantillaPrecios = plantillaPreciosService.add(plantillaPrecios);
        // TODO crear URI
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Collections.singletonList(newPlantillaPrecios))).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Plantilla plantillaPrecios) throws GeneralPARException
    {
    	plantillaPrecios.setId(Long.valueOf(id));
    	plantillaPreciosService.update(plantillaPrecios);
        return Response.ok()
                .entity(new RestResponse(true, Collections.singletonList(plantillaPrecios))).build();
    }
    
    @GET
    @Path("{id}/precios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrecios(@PathParam("id") String id)
    {
        return Response.ok().entity(new RestResponse(true, preciosService.getPreciosOfPlantilla(Long.valueOf(id)))).build();
    }
    
    @GET
    @Path("editables")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlantillasEditables()
    {
        return Response.ok().entity(new RestResponse(true,  plantillaPreciosService.getEditables())).build();
    }
    
    @DELETE
    @Path("{plantillaId}/precios/{precioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePrecio(@PathParam("plantillaId") String plantillaId, @PathParam("precioId") String precioId)
    {
    	preciosService.remove(Integer.parseInt(plantillaId), Integer.parseInt(precioId));
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @POST
    @Path("{plantillaId}/precios")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrecio(@PathParam("plantillaId") String plantillaId, PreciosPlantilla precio) throws GeneralPARException
    {
        PreciosPlantilla newPrecio = preciosService.add(precio);
        // TODO crear URI
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Collections.singletonList(newPrecio))).build();
    }

    @PUT
    @Path("{plantillaId}/precios/{precioId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrecio(@PathParam("plantillaId") String plantillaId, @PathParam("precioId") String precioId, PreciosPlantilla precio) throws GeneralPARException
    {
    	precio.setId(Long.valueOf(precioId));
    	preciosService.update(precio);
        return Response.ok()
                .entity(new RestResponse(true, Collections.singletonList(precio))).build();
    }
}

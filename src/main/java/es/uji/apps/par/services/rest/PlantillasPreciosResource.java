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
import es.uji.apps.par.model.PlantillaPrecios;
import es.uji.apps.par.model.Precio;
import es.uji.apps.par.services.PlantillaPreciosService;
import es.uji.apps.par.services.PreciosService;

@Path("plantillaprecios")
public class PlantillasPreciosResource {

	@InjectParam
    private PlantillaPreciosService plantillaPreciosService;
	
	@InjectParam
	private PreciosService preciosService;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok().entity(new RestResponse(true, plantillaPreciosService.get())).build();
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
    public Response add(PlantillaPrecios plantillaPrecios) throws GeneralPARException
    {
        PlantillaPrecios newPlantillaPrecios = plantillaPreciosService.add(plantillaPrecios);
        // TODO crear URI
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Collections.singletonList(newPlantillaPrecios))).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, PlantillaPrecios plantillaPrecios) throws GeneralPARException
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
    public Response addPrecio(@PathParam("plantillaId") String plantillaId, Precio precio) throws GeneralPARException
    {
        Precio newPrecio = preciosService.add(precio);
        // TODO crear URI
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Collections.singletonList(newPrecio))).build();
    }

    @PUT
    @Path("{plantillaId}/precios/{precioId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrecio(@PathParam("plantillaId") String plantillaId, @PathParam("precioId") String precioId, Precio precio) throws GeneralPARException
    {
    	precio.setId(Long.valueOf(precioId));
    	preciosService.update(precio);
        return Response.ok()
                .entity(new RestResponse(true, Collections.singletonList(precio))).build();
    }
}

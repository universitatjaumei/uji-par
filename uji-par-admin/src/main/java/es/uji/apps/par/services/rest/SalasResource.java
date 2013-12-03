package es.uji.apps.par.services.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.model.Sala;
import es.uji.apps.par.services.SalasService;

@Path("sala")
public class SalasResource
{
    @InjectParam
    private SalasService salasService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        List<Sala> salas = salasService.getSalas();
                
        return Response.ok().entity(new RestResponse(true, salas, salas.size())).build();
    }
}

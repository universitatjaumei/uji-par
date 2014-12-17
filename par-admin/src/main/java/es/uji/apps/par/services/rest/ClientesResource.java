package es.uji.apps.par.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.par.services.ClientesService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("clientes")
public class ClientesResource extends BaseResource {
    @InjectParam
    private ClientesService clientesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") @DefaultValue("1000") int limit) {
        return Response.ok().entity(new RestResponse(true, clientesService.getClientes(sort, start, limit),
                clientesService.getTotalClientes())).build();
    }

    @GET
    @Path("mails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMails() {
        return Response.ok().entity(new RestResponse(true, clientesService.getMails(), 0)).build();
    }
}
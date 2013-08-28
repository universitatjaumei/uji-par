package es.uji.apps.par.services.rest;

import java.net.URI;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.model.Usuario;
import es.uji.apps.par.services.UsersService;
import es.uji.apps.par.utils.Utils;

@Path("usuario")
public class UsersResource
{
    @InjectParam
    private UsersService usersService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("sort") String sort, @QueryParam("start") int start, @QueryParam("limit") int limit)
    {
    	limit = Utils.inicializarLimitSiNecesario(limit);
        return Response.ok().entity(new RestResponse(true, usersService.getUsuarios(sort, start, limit), 
        		usersService.getTotalUsuarios())).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") String id)
    {
        usersService.removeUser(Integer.parseInt(id));
        return Response.ok().entity(new RestResponse(true)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Usuario user) throws GeneralPARException
    {
        Usuario newUser = usersService.addUser(user);
        // TODO -> generar URL
        return Response.created(URI.create(""))
                .entity(new RestResponse(true, Arrays.asList(newUser), 1)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Usuario user) throws GeneralPARException
    {
        usersService.updateUser(user);
        return Response.ok().entity(new RestResponse(true, Arrays.asList(user), 1))
                .build();
    }
}

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
import es.uji.apps.par.model.ParUsuario;
import es.uji.apps.par.services.UsersService;

@Path("usuario")
public class UsersResource
{
    @InjectParam
    private UsersService usersService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok().entity(new RestResponse(true, usersService.getUsuarios())).build();
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
    public Response add(ParUsuario user) throws ParException
    {
        ParUsuario newUser = usersService.addUser(user);
        //TODO -> generar URL
        return Response.created(URI.create("")).entity(new RestResponse(true, Collections.singletonList(newUser))).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(ParUsuario user) throws ParException
    {
        usersService.updateUser(user);
        return Response.ok().entity(new RestResponse(true, Collections.singletonList(user))).build();
    }    
}

package es.uji.apps.par.services.rest;

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

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.model.ParUsuario;
import es.uji.apps.par.services.UsersService;

@Path("usuarios")
public class UsersResource
{
    @InjectParam
    private UsersService usersService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse getAll()
    {
        return new RestResponse(true, usersService.getUsuarios());
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse remove(@PathParam("id") String id)
    {
        usersService.removeUser(Integer.parseInt(id));
        return new RestResponse(true);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse add(ParUsuario user)
    {
        ParUsuario newUser = usersService.addUser(user);
        return new RestResponse(true, Collections.singletonList(newUser));
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse update(ParUsuario user)
    {
        usersService.updateUser(user);
        return new RestResponse(true, Collections.singletonList(user));
    }    
}

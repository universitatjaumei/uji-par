package es.uji.apps.par.services.rest;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.services.EntradasService;

@Path("compra")
public class ComprasResource extends BaseResource
{
    public static Logger log = Logger.getLogger(ComprasResource.class);

    @InjectParam
    private EntradasService entradasService;

    @Context
    HttpServletResponse currentResponse;

    @GET
    @Path("{id}/pdf")
    @Produces("application/pdf")
    public Response datosEntrada(@PathParam("id") Long idCompra) throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        entradasService.generaEntrada(idCompra, bos);

        Response response = Response.ok(bos.toByteArray())
                .header("Cache-Control", "no-cache, no-store, must-revalidate").header("Pragma", "no-cache")
                .header("Expires", "0").build();

        return response;
    }
}

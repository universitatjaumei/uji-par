package es.uji.apps.par.services.rest;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.drawer.MapaDrawer;

@Path("imagenes")
public class ImagenesResource extends BaseResource
{
    @InjectParam
    private MapaDrawer mapaDrawer;

    @Context
    ServletContext context;

    @GET
    @Path("butacas/{sesion}/{seccion}")
    @Produces("image/jpeg")
    public Response datosEntrada(@PathParam("sesion") Long sesion, @PathParam("seccion") String seccion)
            throws Exception
    {
        ByteArrayOutputStream os = mapaDrawer.generaImagen(sesion, seccion);

        Response response = Response.ok(os.toByteArray())
                .header("Cache-Control", "no-cache, no-store, must-revalidate").header("Pragma", "no-cache")
                .header("Expires", "0").build();

        return response;
    }
}
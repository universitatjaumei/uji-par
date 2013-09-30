package es.uji.apps.par.services.rest;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.services.BarcodeService;
import es.uji.apps.par.utils.Utils;

@Path("barcode")
public class BarcodeResource extends BaseResource
{
    public static Logger log = Logger.getLogger(BarcodeResource.class);

    @InjectParam
    private BarcodeService barcodeService;

    @Context
    HttpServletResponse currentResponse;

    @GET
    @Path("{text}")
    @Produces("image/png")
    public Response datosEntrada(@PathParam("text") String text) throws Exception
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        barcodeService.generaBarcodeQr(text, bos);

        ResponseBuilder builder = Response.ok(bos.toByteArray());
        
        return Utils.noCache(builder).build();
    }

}

package es.uji.apps.par.services.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.auth.AuthChecker;
import es.uji.apps.par.services.ComunicacionesICAAService;

@Path("comunicacionesicaa")
public class ComunicacionesICAAResource extends BaseResource
{
    @InjectParam
    private ComunicacionesICAAService comunicacionesICAAService;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generaFicheroICAA(@FormParam("ids") List<Integer> ids, 
    		@FormParam("fechaEnvioHabitualAnterior") String fechaEnvioHabitualAnterior, @FormParam("tipoEnvio") String tipoEnvio)
    		throws GeneralPARException, RegistroSerializaException
    {
        AuthChecker.canWrite(currentRequest);
        byte[] arr = comunicacionesICAAService.generaFicheroICAA(ids, fechaEnvioHabitualAnterior, tipoEnvio);
		return Response.ok(arr, 
				MediaType.TEXT_PLAIN).
				header("content-disposition","attachment; filename = informeICAA.txt").build();
    }
}
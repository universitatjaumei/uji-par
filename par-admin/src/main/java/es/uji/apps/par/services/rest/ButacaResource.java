package es.uji.apps.par.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.par.exceptions.ButacaOcupadaException;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.services.ButacasService;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("butacas")
public class ButacaResource extends BaseResource {
    @InjectParam
    private ButacasService butacasService;

    @Context
    HttpServletResponse currentResponse;

    @GET
    @Path("{butacaId}/libres/{tarifaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getButacasLibres(@PathParam("butacaId") Long butacaId, @PathParam("tarifaId") String tarifaId,
                                     @QueryParam("sort") String sort, @QueryParam("start") int start,
                                     @QueryParam("limit") @DefaultValue("1000") int limit,
                                     @QueryParam("search") @DefaultValue("") String search) throws IOException {
        List<Butaca> butacasDisponibles = butacasService.getButacasDisponibles(butacaId, tarifaId, getLocale());

        return Response.ok().entity(new RestResponse(true, butacasDisponibles,
                butacasDisponibles.size())).build();
    }

    @POST
    @Path("{butacaId}/cambia/{fila}/{numero}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiaButaca(@PathParam("butacaId") Long butacaId, @PathParam("fila") String fila,
                                 @PathParam("numero") String numero) {

        try {
            butacasService.cambiaFilaNumero(butacaId, fila, numero);
            return Response.ok(butacaId).build();
        } catch (ButacaOcupadaException e) {
            return errorResponse("error.butacaOcupada",
                    getProperty("localizacion." + e.getLocalizacion()),
                    e.getFila(), e.getNumero());
        }
    }
}

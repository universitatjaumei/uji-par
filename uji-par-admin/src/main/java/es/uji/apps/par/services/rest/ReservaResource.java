package es.uji.apps.par.services.rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.auth.AuthChecker;
import es.uji.apps.par.model.ReservaRequest;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.services.ComprasService;

@Path("reserva")
public class ReservaResource extends BaseResource
{
    public static Logger log = Logger.getLogger(ReservaResource.class);
    
    @InjectParam
    private ComprasService comprasService;
    
    @Context
    HttpServletResponse currentResponse;

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reservaEntrada(@PathParam("id") Long sesionId, ReservaRequest datosReserva) throws NoHayButacasLibresException, ButacaOcupadaException
    {
        AuthChecker.canWrite(currentRequest);
        
        try
        {
            ResultadoCompra resultadoCompra =  comprasService.reservaButacas(sesionId, datosReserva.getDesde(), datosReserva.getHasta(), 
            		datosReserva.getButacasSeleccionadas(), datosReserva.getObservaciones(), datosReserva.getHoraInicial(), datosReserva.getHoraFinal(),
            		datosReserva.getMinutoInicial(), datosReserva.getMinutoFinal());
            return Response.ok(resultadoCompra).build();
        }
        catch (NoHayButacasLibresException e)
        {   
            return errorResponse("error.noHayButacas", getProperty("localizacion." + e.getLocalizacion()));
        }
        catch (ButacaOcupadaException e)
        {   
            return errorResponse("error.butacaOcupada", getProperty("localizacion." +e.getLocalizacion()),
            		e.getFila(), e.getNumero());
        }
        catch (CompraSinButacasException e)
        {   
            return errorResponse("error.compraSinButacas");
        }        
    }
}

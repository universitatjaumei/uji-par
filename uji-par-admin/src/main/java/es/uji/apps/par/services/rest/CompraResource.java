package es.uji.apps.par.services.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.services.ButacasService;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.SesionesService;

@Path("compra")
public class CompraResource extends BaseResource
{
    public static Logger log = Logger.getLogger(CompraResource.class);
    
    @InjectParam
    private ComprasService comprasService;
    
    @InjectParam
    private SesionesService sesionesService;
    
    @InjectParam
    private ButacasService butacasService;

    @Context
    HttpServletResponse currentResponse;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompras()
    {
        return Response.ok().entity(new RestResponse(true, comprasService.get())).build();
    }
    
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response compraEntrada(@PathParam("id") Long sesionId, List<Butaca> butacasSeleccionadas) throws NoHayButacasLibresException, ButacaOcupadaException
    {
        try
        {
            ResultadoCompra resultadoCompra =  comprasService.registraCompraTaquilla(sesionId, butacasSeleccionadas);
            return Response.ok(resultadoCompra).build();
        }
        catch (NoHayButacasLibresException e)
        {   
            return errorResponse("error.noHayButacas", getProperty("localizacion." + e.getLocalizacion()));
        }
        catch (ButacaOcupadaException e)
        {   
            return errorResponse("error.butacaOcupada", getProperty("localizacion." +e.getLocalizacion()), e.getFila(), e.getNumero());
        }
        catch (CompraSinButacasException e)
        {   
            return errorResponse("error.compraSinButacas");
        }        
    }
    
    @GET
    @Path("{id}/precios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreciosSesion(@PathParam("id") Long sesionId)
    {
        return Response.ok().entity(new RestResponse(true, sesionesService.getPreciosSesion(sesionId))).build();
    }
    
    
    // Para una sesión no numerada devuelve las butacas disponibles por localización
    @GET
    @Path("{id}/disponibles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOcupacionesNoNumerada(@PathParam("id") Long sesionId)
    {
        return Response.ok().entity(new RestResponse(true, butacasService.getDisponiblesNoNumerada(sesionId))).build();
    }
    
    @POST
    @Path("{id}/importe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImportesButacas(@PathParam("id") Long sesionId, List<Butaca> butacasSeleccionadas)
    {
        BigDecimal importe = comprasService.calculaImporteButacas(sesionId, butacasSeleccionadas, true);
        
        return Response.ok().entity(importe.setScale(2).toString()).build();
    }
    
}

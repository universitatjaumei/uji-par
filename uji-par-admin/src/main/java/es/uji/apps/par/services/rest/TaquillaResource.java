package es.uji.apps.par.services.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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

import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.SesionesService;

@Path("compra")
public class TaquillaResource extends BaseResource
{
    public static Logger log = Logger.getLogger(TaquillaResource.class);
    
    @InjectParam
    private ComprasService comprasService;
    
    @InjectParam
    private SesionesService sesionesService;

    @Context
    HttpServletResponse currentResponse;

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compraEntrada(@PathParam("id") Long sesionId, List<Butaca> butacasSeleccionadas)
    {
        ResultadoCompra resultadoCompra =  comprasService.realizaCompraTaquilla(sesionId, butacasSeleccionadas);
       
        return Response.ok(resultadoCompra).build();
    }
    
    
    @GET
    @Path("{id}/precios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreciosSesion(@PathParam("id") Long sesionId)
    {
        return Response.ok().entity(new RestResponse(true, sesionesService.getPreciosSesion(sesionId))).build();
    }
  }

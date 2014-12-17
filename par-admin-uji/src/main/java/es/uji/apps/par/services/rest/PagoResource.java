package es.uji.apps.par.services.rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.auth.AuthChecker;
import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.pinpad.ResultadoPagoPinpad;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.PagoTarjetaService;

@Path("pago")
public class PagoResource extends BaseResource
{
    @InjectParam
    PagoTarjetaService pagoTarjeta;

    @InjectParam
    ComprasService compras;

    @Context
    HttpServletResponse currentResponse;

    @POST
    @Path("{idCompra}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultadoPagoPinpad realizaPago(@PathParam("idCompra") Integer idCompra, String concepto)
    {
        AuthChecker.canWrite(currentRequest);
        
        return pagoTarjeta.realizaPago(idCompra, concepto);
    }

    @POST
    @Path("abonado/{idAbonado}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultadoPagoPinpad realizaPagoAbonado(@PathParam("idAbonado") Integer idAbonado, String concepto)
    {
        AuthChecker.canWrite(currentRequest);

        return pagoTarjeta.realizaPagoAbonado(idAbonado, concepto);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public EstadoPinpad estadoPago(@PathParam("id") Long id)
    {
        return pagoTarjeta.consultaEstadoPago(id);
    }

    @DELETE
    @Path("{idCompra}/pendiente")
    public void borrarCompraPendiente(@PathParam("idCompra") Long idCompra)
    {
        AuthChecker.canWrite(currentRequest);
        
        pagoTarjeta.borraCompraPendiente(idCompra);
    }
}

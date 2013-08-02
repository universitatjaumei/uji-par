package es.uji.apps.par.services.rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.pinpad.ResultadoPagoPinpad;
import es.uji.apps.par.services.Pinpad;

@Path("pago")
public class PagoResource extends BaseResource
{
    public static Logger log = Logger.getLogger(PagoResource.class);

    @InjectParam
    private Pinpad pinpad;
    
    @InjectParam
    private ComprasDAO compras;

    @Context
    HttpServletResponse currentResponse;

    @POST
    @Path("{idCompra}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultadoPagoPinpad realizaPago(@PathParam("idCompra") Integer idCompra, String concepto)
    {
        CompraDTO compra = compras.getCompraById(idCompra);
        ResultadoPagoPinpad resultado = pinpad.realizaPago(Integer.toString(idCompra), compra.getImporte() , concepto);
        
        if (!resultado.getError())
        {
            compras.guardarCodigoPago(compra.getId(), resultado.getCodigo());
        }

        return resultado;
    }


}

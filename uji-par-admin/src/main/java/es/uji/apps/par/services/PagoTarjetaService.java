package es.uji.apps.par.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.pinpad.ResultadoPagoPinpad;

@Service
public class PagoTarjetaService
{
    public static Logger log = Logger.getLogger(ComprasService.class);

    private static final String PAGO_OK_TARJETA_MAGNETICA = "20";
    private static final String PAGO_OK_TARJETA_CHIP = "30";

    private Map<Long, EstadoPinpad> pagosPendientes = new HashMap<Long, EstadoPinpad>();

    @InjectParam
    private Pinpad pinpad;

    @InjectParam
    private ComprasDAO compras;

    public ResultadoPagoPinpad realizaPago(Integer idCompra, String concepto)
    {
        CompraDTO compra = compras.getCompraById(idCompra);
        ResultadoPagoPinpad resultado = pinpad.realizaPago(Integer.toString(idCompra), compra.getImporte(), concepto);

        if (!resultado.getError())
        {
            log.info("guardandoCodigoPago: idCompra:" + idCompra);
            compras.guardarCodigoPago(compra.getId(), resultado.getCodigo());
            lanzaThreadConsultaEstado(compra.getId());
        }

        return resultado;
    }

    private void lanzaThreadConsultaEstado(final long idCompra)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    EstadoPinpad estado = pinpad.getEstadoPinpad(Long.toString(idCompra));
                    pagosPendientes.put(idCompra, estado);
                    
                    if (tieneCodigoAccion(estado))
                    {
                        if (pagoCorrecto(estado))
                        {
                            log.info("marcarPagada: idCompra:" + idCompra);
                            compras.marcarPagada(idCompra);
                        }
                        else
                        {
                            compras.borrarCompraNoPagada(idCompra);
                        }

                        return;
                    }
                    else
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            log.error("Sleep en bucle de consulta estado: " + e);
                        }
                    }
                }
            }

        }.start();
    }

    private boolean pagoCorrecto(EstadoPinpad estado)
    {
        return estado.getCodigoAccion().equals(PAGO_OK_TARJETA_MAGNETICA)
                || estado.getCodigoAccion().equals(PAGO_OK_TARJETA_CHIP);
    }
    
    private boolean tieneCodigoAccion(EstadoPinpad estado)
    {
        return estado != null && estado.getCodigoAccion()!=null && !estado.getCodigoAccion().equals("");
    }

    public EstadoPinpad consultaEstadoPago(Long idCompra)
    {
        EstadoPinpad estado = pagosPendientes.get(idCompra);

        if (tieneCodigoAccion(estado))
        {
            pagosPendientes.remove(idCompra);
        }

        return estado;
    }

    public void borraCompraPendiente(Long idCompra)
    {
        compras.borrarCompraNoPagada(idCompra);
    }

}

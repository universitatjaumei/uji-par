package es.uji.apps.par.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.apps.par.dao.AbonadosDAO;
import es.uji.apps.par.db.AbonadoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.pinpad.ResultadoPagoPinpad;

@Service
public class PagoTarjetaService
{
	private static final Logger log = LoggerFactory.getLogger(PagoTarjetaService.class);

    private static final String PAGO_OK_TARJETA_MAGNETICA = "20";
    private static final String PAGO_OK_TARJETA_CHIP = "30";

    private Map<Long, EstadoPinpad> pagosPendientes = new HashMap<Long, EstadoPinpad>();

    @InjectParam
    private Pinpad pinpad;

    @InjectParam
    private ComprasDAO compras;

    @InjectParam
    private AbonadosDAO abonados;

    public ResultadoPagoPinpad realizaPago(long idCompra, String concepto)
    {
        CompraDTO compra = compras.getCompraById(idCompra);
        ResultadoPagoPinpad resultado = pinpad.realizaPago(Long.toString(idCompra), compra.getImporte(), concepto);

        if (resultado.getError())
        {
            compras.borrarCompraNoPagada(idCompra);
        }
        else
        {
            log.info("guardandoCodigoPago: idCompra:" + idCompra);
            compras.guardarCodigoPagoTarjeta(compra.getId(), resultado.getCodigo());
            lanzaThreadConsultaEstado(compra.getId());
        }

        return resultado;
    }

    public ResultadoPagoPinpad realizaPagoAbonado(long idAbonado, String concepto)
    {
        AbonadoDTO abonado = abonados.getAbonado(idAbonado);
        ResultadoPagoPinpad resultado = pinpad.realizaPago(Long.toString(idAbonado), abonado.getImporte(), concepto);
        if (resultado.getError())
        {
            compras.borrarCompraAbonadoNoPagada(idAbonado);
        }
        else
        {
            log.info("guardandoCodigoPago (abonado): idAbonado:" + idAbonado);
            for (CompraDTO compra : abonado.getParCompras())
            {
                compras.guardarCodigoPagoTarjeta(compra.getId(), resultado.getCodigo());
            }
            lanzaThreadConsultaEstadoAbonado(idAbonado);
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
                            compras.marcarPagadaConRecibo(idCompra, estado.getRecibo());
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

    private void lanzaThreadConsultaEstadoAbonado(final long idAbonado)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    EstadoPinpad estado = pinpad.getEstadoPinpad(Long.toString(idAbonado));
                    pagosPendientes.put(idAbonado, estado);

                    if (tieneCodigoAccion(estado))
                    {
                        if (pagoCorrecto(estado))
                        {
                            log.info("marcarPagada (abonado): idAbonado:" + idAbonado);
                            compras.marcarAbonadoPagadaConRecibo(idAbonado, estado.getRecibo());
                        }
                        else
                        {
                            compras.borrarCompraAbonadoNoPagada(idAbonado);
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
                            log.error("Sleep en bucle de consulta estado (abonado): " + e);
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

    public EstadoPinpad consultaEstadoPago(Long id)
    {
        EstadoPinpad estado = pagosPendientes.get(id);

        if (tieneCodigoAccion(estado))
        {
            pagosPendientes.remove(id);
        }

        return estado;
    }

    public void borraCompraPendiente(Long idCompra)
    {
        compras.borrarCompraNoPagada(idCompra);
    }
}

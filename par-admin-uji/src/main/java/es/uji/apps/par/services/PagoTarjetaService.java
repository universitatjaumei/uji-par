package es.uji.apps.par.services;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.par.dao.AbonadosDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.AbonadoDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.pinpad.ResultadoPagoPinpad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class PagoTarjetaService
{
	private static final Logger log = LoggerFactory.getLogger(PagoTarjetaService.class);
    private static final String PAGO_OK = "0";
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
            compras.borrarCompraNoPagada(idCompra);
        else
            log.info("pago realizado para la compra idCompra:" + idCompra + ", empezamos la comprobacion de estado");

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
        return estado.getCodigoAccion().equals(PAGO_OK);
    }
    
    private boolean tieneCodigoAccion(EstadoPinpad estado)
    {
        return estado != null && estado.getCodigoAccion()!=null && !estado.getCodigoAccion().equals("");
    }

    public EstadoPinpad consultaEstadoPago(Long idCompra)
    {
		EstadoPinpad estado = pinpad.getEstadoPinpad(Long.toString(idCompra));
		if (tieneCodigoAccion(estado))
		{
			if (pagoCorrecto(estado))
			{
				actualizaCompraComoPagada(idCompra, estado);
			}
			else if (estado.getError())
			{
				compras.borrarCompraNoPagada(idCompra);
			}
		}
		return estado;
    }

	@Transactional
	private void actualizaCompraComoPagada(Long idCompra, EstadoPinpad estado) {
		log.info("guardarRecibo: idCompra:" + idCompra);
		compras.guardarCodigoPagoTarjeta(idCompra, estado.getRecibo());
		log.info("marcarPagada: idCompra:" + idCompra);
		compras.marcarPagadaConRecibo(idCompra, estado.getRecibo());
	}

	public void borraCompraPendiente(Long idCompra)
    {
        compras.borrarCompraNoPagada(idCompra);
    }
}

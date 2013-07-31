package es.uji.apps.par.services;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.pinpad.ResultadoPagoPinpad;

@Service
public class Pinpad
{
    private static Logger log = Logger.getLogger(Pinpad.class);

    @Autowired
    PinpadDataService dataService;

    public Pinpad()
    {
    }

    public Pinpad(PinpadDataService dataService)
    {
        this.dataService = dataService;
    }

    public void setDataService(PinpadDataService dataService)
    {
        this.dataService = dataService;
    }

    public EstadoPinpad getEstadoPinpad(String id)
    {
        try
        {
            log.info(String.format("llamando consultaEstado(\"%s\")...", id));
            String resultado = dataService.consultaEstado(id);
            log.info(String.format("llamado consultaEstado(\"%s\") -> \"%s\"", id, resultado));

            return parseEstado(resultado);
        }
        catch (Exception e)
        {
            log.error("Error obteniendo estado pinpad", e);
            return new EstadoPinpad(true);
        }
    }

    public ResultadoPagoPinpad realizaPago(String id, BigDecimal importe, String concepto)
    {
        try
        {
            log.info(String.format("llamando realizaPago(\"%s\", \"%s\", \"%s\")...", id, importe.floatValue(), concepto));
            String resultado = dataService.realizaPago(id, importe, concepto);
            log.info(String.format("llamado realizaPago(\"%s\", \"%s\", \"%s\") -> \"%s\"", id, importe, concepto, resultado));
            
            return parseResultadoPago(resultado);
        }
        catch (Exception e)
        {
            log.error("Error realizando pago pinpad", e);
            return new ResultadoPagoPinpad(true, e.getMessage());
        }
    }

    private ResultadoPagoPinpad parseResultadoPago(String textoResultado)
    {
        ResultadoPagoPinpad resultado = new ResultadoPagoPinpad();
        
        if (textoResultado.equals(""))
        {
            resultado.setError(true);
        }
        else
        {
            resultado.setError(false);
            resultado.setCodigo(textoResultado);
        }
            
        return resultado;
    }

    private EstadoPinpad parseEstado(String resultado)
    {
        EstadoPinpad estado = new EstadoPinpad(false);

        String[] vecMensaje = resultado.split("\n", 2);
        String lineaEstado = vecMensaje[0];

        if (vecMensaje.length > 1)
            estado.setRecibo(vecMensaje[1]);

        String[] vecLineaEstado = lineaEstado.split("-", 3);

        String ready = vecLineaEstado[0];
        String codigoAccion = vecLineaEstado[1];
        String mensaje = vecLineaEstado[2];

        estado.setReady(ready.equals("1"));
        estado.setCodigoAccion(codigoAccion);
        estado.setMensaje(mensaje);
        estado.setPagoCorrecto(codigoAccion.equals("20") || codigoAccion.equals("30"));

        return estado;
    }

}

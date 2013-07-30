package es.uji.apps.par.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.pinpad.EstadoPinpad;

@Service
public class Pinpad
{
    private static Logger log = Logger.getLogger(Pinpad.class);

    @Autowired
    PinpadDataService dataService;

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
            String resultado = dataService.consultaEstado(id);
            return parseRespuesta(resultado);
        }
        catch (Exception e)
        {
            log.error("Error obteniendo estado pinpad", e);
            return new EstadoPinpad(true);
        }
    }

    private EstadoPinpad parseRespuesta(String resultado)
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

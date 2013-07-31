package es.uji.apps.par.services.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.apps.par.pinpad.EstadoPinpad;
import es.uji.apps.par.services.Pinpad;
import es.uji.apps.par.services.PinpadDataService;

public class PinpadConsultaEstadoTest
{
    private Pinpad pinpad;
    private PinpadDataService pinpadService;

    @Before
    public void before()
    {
        pinpadService = mock(PinpadDataService.class);
        pinpad = new Pinpad(pinpadService);
    }

    @Test
    public void testNoError()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("0-20-Correcto");

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertFalse("No error", estado.getError());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testError()
    {
        when(pinpadService.consultaEstado(anyString())).thenThrow(Exception.class);

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertTrue("Error", estado.getError());
    }

    @Test
    public void testReady()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-20-Correcto");

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertTrue("Ready", estado.getReady());
    }

    @Test
    public void testPagoIncorrecto()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("0-2-Correcto");

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertFalse("Not ready", estado.getReady());
    }

    @Test
    public void testCodigoError()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-2-Correcto");

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertEquals("Código error", "2", estado.getCodigoAccion());
    }

    @Test
    public void testMensaje()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-2-Esto es el mensaje");

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertEquals("Mensaje", "Esto es el mensaje", estado.getMensaje());
    }

    @Test
    public void testMensajeConGuiones()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-2-Esto es el mensaje - ole");

        EstadoPinpad estado = pinpad.getEstadoPinpad("");

        assertEquals("Mensaje con guiones", "Esto es el mensaje - ole", estado.getMensaje());
    }

    @Test
    public void pagoCorrecto()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-20-Pago correcto");
        assertTrue("Pago correcto 1", pinpad.getEstadoPinpad("").getPagoCorrecto());

        when(pinpadService.consultaEstado(anyString())).thenReturn("1-30-Pago correcto");
        assertTrue("Pago correcto 2", pinpad.getEstadoPinpad("").getPagoCorrecto());
    }

    @Test
    public void pagoNoCorrecto()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-1-Lo que sea");
        assertFalse("Pago no correcto 1", pinpad.getEstadoPinpad("").getPagoCorrecto());

        when(pinpadService.consultaEstado(anyString())).thenReturn("1-32-Lo que sea");
        assertFalse("Pago no correcto 2", pinpad.getEstadoPinpad("").getPagoCorrecto());
    }

    @Test
    public void reciboPago()
    {
        when(pinpadService.consultaEstado(anyString())).thenReturn("1-1-OPERACION ACEPTADA\nTarjeta: 1\nlalala");

        assertEquals("Texto recibo", "Tarjeta: 1\nlalala", pinpad.getEstadoPinpad("").getRecibo());
    }

}

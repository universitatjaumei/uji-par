package es.uji.apps.par.report;

import es.uji.apps.par.exceptions.AnticipadaFormatException;
import es.uji.apps.par.exceptions.RegistroSerializaException;
import es.uji.apps.par.enums.TipoVenta;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class InformeModelTest
{
    @Test(expected = AnticipadaFormatException.class)
    public void testAnticipadaNulo() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-10-02 19:15:00.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, null);
    }

    @Test(expected = AnticipadaFormatException.class)
    public void testAnticipadaFormatoIncorrecto() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-10-02 18:00:01.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, "2j");
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionFechaInicioNula() throws RegistroSerializaException
    {
        String fechaCompraSesionString = "2011-10-02 18:00:01.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, null, fechaCompraSesion, "2");

        Assert.assertEquals(TipoVenta.FISICA_TAQUILLA, tipoVenta);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionFechaCompraNula() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, fechaInicioSesion, null, "2");

        Assert.assertEquals(TipoVenta.FISICA_TAQUILLA, tipoVenta);
    }

    @Test
    public void testTipoAnticipada() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-10-01 23:59:59.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, "1d");

        Assert.assertEquals(TipoVenta.FISICA_ANTICIPADA, tipoVenta);
    }

    @Test
    public void testTipoAnticipadaTresDias() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-09-29 23:59:59.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, "3d");

        Assert.assertEquals(TipoVenta.FISICA_ANTICIPADA, tipoVenta);
    }

    @Test
    public void testTipoTaquillaTresDias() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-09-30 23:59:59.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, "3d");

        Assert.assertEquals(TipoVenta.FISICA_TAQUILLA, tipoVenta);
    }

    @Test
    public void testTipoAnticipadaDosHoras() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-10-02 18:00:00.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, "2");

        Assert.assertEquals(TipoVenta.FISICA_ANTICIPADA, tipoVenta);
    }

    @Test
    public void testTipoTaquillaDosHoras() throws RegistroSerializaException
    {
        String fechaInicioSesionString = "2011-10-02 20:00:00.000000";
        Timestamp fechaInicioSesion = Timestamp.valueOf(fechaInicioSesionString);

        String fechaCompraSesionString = "2011-10-02 18:00:01.000000";
        Timestamp fechaCompraSesion = Timestamp.valueOf(fechaCompraSesionString);

        TipoVenta tipoVenta = InformeModelReport.getTipoVenta(true, fechaInicioSesion, fechaCompraSesion, "2");

        Assert.assertEquals(TipoVenta.FISICA_TAQUILLA, tipoVenta);
    }
}

package es.uji.apps.par.ficheros.registros;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.registros.RegistroBuzon;

public class RegistroBuzonTest
{
    @Test
    @SuppressWarnings("deprecation")
    public void testRegistroCine() throws RegistroSerializaException
    {
        RegistroBuzon registro = new RegistroBuzon();

        registro.setCodigo("123");
        registro.setTipo("FL");
        registro.setFechaEnvioHabitualAnterior(new Date(113, 1, 1));
        registro.setFechaEnvio(new Date(113, 1, 2));
        registro.setLineas(7);
        registro.setSesiones(8);
        registro.setEspectadores(9);
        registro.setRecaudacion(new BigDecimal(10.11f));

        Assert.assertEquals("0123FL03203300000000007000000000080000000000900000010.11", registro.serializa());
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoNull() throws RegistroSerializaException
    {
        RegistroBuzon registro = new RegistroBuzon();

        registro.setCodigo(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testTipoNull() throws RegistroSerializaException
    {
        RegistroBuzon registro = new RegistroBuzon();

        registro.setCodigo("");
        registro.setTipo(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoTamanyo() throws RegistroSerializaException
    {
        RegistroBuzon registro = new RegistroBuzon();

        registro.setCodigo("1234");
        registro.setTipo("1");
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testTipoTamanyo() throws RegistroSerializaException
    {
        RegistroBuzon registro = new RegistroBuzon();

        registro.setCodigo("123");
        registro.setTipo("123");
        registro.serializa();
    }

}

package es.uji.apps.par.ficheros.registros;

import org.junit.Assert;
import org.junit.Test;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.registros.RegistroSala;

public class RegistroSalaTest
{
    @Test
    public void testRegistroSale() throws RegistroSerializaException
    {
        RegistroSala registro = new RegistroSala();

        registro.setCodigo("123");
        registro.setNombre("Sala 1");

        Assert.assertEquals("1123         Sala 1                        ", registro.serializa());
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoNull() throws RegistroSerializaException
    {
        RegistroSala registro = new RegistroSala();

        registro.setCodigo(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testNombreNull() throws RegistroSerializaException
    {
        RegistroSala registro = new RegistroSala();

        registro.setCodigo("");
        registro.setNombre(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoMas6Caracteres() throws RegistroSerializaException
    {
        RegistroSala registro = new RegistroSala();

        registro.setCodigo("1234567");
        registro.setNombre("");
        registro.serializa();
    }
}

package es.uji.apps.par.ficheros.registros;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import es.uji.apps.par.RegistroSerializaException;

public class RegistroSesionPeliculaTest
{
    @Test
    @SuppressWarnings("deprecation")
    public void testRegistroSesion() throws RegistroSerializaException
    {
        RegistroSesionPelicula registro = new RegistroSesionPelicula();

        registro.setCodigoSala("123");
        registro.setFecha(new Date(113, 10, 5));
        registro.setHora("0120");
        registro.setCodigoPelicula(5678);

        Assert.assertEquals("3123         051113012005678", registro.serializa());
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoSalaNull() throws RegistroSerializaException
    {
        RegistroSesionPelicula registro = new RegistroSesionPelicula();

        registro.setCodigoSala(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testFechaNull() throws RegistroSerializaException
    {
        RegistroSesionPelicula registro = new RegistroSesionPelicula();

        registro.setCodigoSala("123");
        registro.setFecha(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testHoraNull() throws RegistroSerializaException
    {
        RegistroSesionPelicula registro = new RegistroSesionPelicula();

        registro.setCodigoSala("123");
        registro.setFecha(new Date());
        registro.setHora(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoMas6Caracteres() throws RegistroSerializaException
    {
        RegistroSesionPelicula registro = new RegistroSesionPelicula();

        registro.setCodigoSala("1234567");
        registro.setFecha(new Date());
        registro.setHora("1830");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testHoraNoSon4Caracteres() throws RegistroSerializaException
    {
        RegistroSesionPelicula registro = new RegistroSesionPelicula();

        registro.setCodigoSala("123456");
        registro.setFecha(new Date());
        registro.setHora("18301");

        registro.serializa();
    }
}

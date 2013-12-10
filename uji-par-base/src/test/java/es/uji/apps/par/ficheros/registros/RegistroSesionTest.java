package es.uji.apps.par.ficheros.registros;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.registros.RegistroSesion;
import es.uji.apps.par.ficheros.registros.TipoIncidencia;

public class RegistroSesionTest
{
    @Test
    @SuppressWarnings("deprecation")
    public void testRegistroSesion() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("1234");
        registro.setFecha(new Date(113, 2, 4));
        registro.setHora("2230");
        registro.setPeliculas(4);
        registro.setEspectadores(5);
        registro.setRecaudacion(new BigDecimal(6.07));
        registro.setIncidencia(TipoIncidencia.SIN_INCIDENCIAS);

        Assert.assertEquals("21234        0403132230040000500006.07001", registro.serializa());
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoSalaNull() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testFechaNull() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("123");
        registro.setFecha(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testHoraNull() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("123");
        registro.setFecha(new Date());
        registro.setHora(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testRecaudacionNull() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("123");
        registro.setFecha(new Date());
        registro.setHora("1830");
        registro.setRecaudacion(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testIncidenciaNull() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("123");
        registro.setFecha(new Date());
        registro.setHora("1830");
        registro.setRecaudacion(BigDecimal.ONE);
        registro.setIncidencia(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoMas6Caracteres() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("1234567");
        registro.setFecha(new Date());
        registro.setHora("1830");
        registro.setRecaudacion(BigDecimal.ONE);
        registro.setIncidencia(TipoIncidencia.SIN_INCIDENCIAS);
        
        registro.serializa();
    }
    
    
    @Test(expected = RegistroSerializaException.class)
    public void testHoraNoSon4Caracteres() throws RegistroSerializaException
    {
        RegistroSesion registro = new RegistroSesion();

        registro.setCodigoSala("123456");
        registro.setFecha(new Date());
        registro.setHora("18301");
        registro.setRecaudacion(BigDecimal.ONE);
        registro.setIncidencia(TipoIncidencia.SIN_INCIDENCIAS);
        
        registro.serializa();
    }
}

package es.uji.apps.par.ficheros.registros;

import org.junit.Assert;
import org.junit.Test;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.registros.RegistroPelicula;

public class RegistroPeliculaTest
{
    @Test
    public void testRegistroPelicula() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        Assert.assertEquals(
                "41234        0567891011       buscando a sugar man                              121314      prueba s.a.                                       1234",
                registro.serializa());
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoSalaNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoSala(null);
        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoExpedienteNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoExpediente(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testTituloNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setTitulo(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoDistribuidoraNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoDistribuidora(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testNombreDistribuidoraNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setNombreDistribuidora(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testVersionOriginalNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setVersionOriginal(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testVersionLinguisticaNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setVersionLinguistica(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testIdiomaSubtitulosNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setIdiomaSubtitulos(null);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testFormatProyeccionNull() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setFormatoProyeccion(null);

        registro.serializa();
    }
    
    @Test(expected = RegistroSerializaException.class)
    public void testFormatProyeccionMas1Caracter() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setFormatoProyeccion("11");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoSalaMas6Caracteres() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoSala("1234567");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoPeliculaMas5Caracteres() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoPelicula(123456);

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoExpedienteMas12Caracteres() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoExpediente("1234567890123");

        registro.serializa();
    }

    @Test
    public void testTituloPeliculaMas50Caracteres() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setTitulo("12345678901234567890123456789012345678901234567890x");

        Assert.assertEquals(
                "41234        0567891011       12345678901234567890123456789012345678901234567890121314      prueba s.a.                                       1234",
                registro.serializa());
    }

    @Test(expected = RegistroSerializaException.class)
    public void testCodigoDistribuidoraMas12Caracteres() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setCodigoDistribuidora("1234567890123");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testNombreDistribuidoraMas50Caracteres() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setNombreDistribuidora("12345678901234567890123456789012345678901234567890x");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testVersionOriginal1Digito() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setVersionOriginal("12");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testVersionLinguistica1Digito() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setVersionLinguistica("12");

        registro.serializa();
    }

    @Test(expected = RegistroSerializaException.class)
    public void testIdioma1Digito() throws RegistroSerializaException
    {
        RegistroPelicula registro = creaRegistroEjemplo();

        registro.setIdiomaSubtitulos("12");

        registro.serializa();
    }

    private RegistroPelicula creaRegistroEjemplo()
    {
        RegistroPelicula registro = new RegistroPelicula();

        registro.setCodigoSala("1234");
        registro.setCodigoPelicula(5678);
        registro.setCodigoExpediente("91011");
        registro.setTitulo("buscando a sugar man");
        registro.setCodigoDistribuidora("121314");
        registro.setNombreDistribuidora("prueba s.a.");
        registro.setVersionOriginal("1");
        registro.setVersionLinguistica("2");
        registro.setIdiomaSubtitulos("3");
        registro.setFormatoProyeccion("4");

        return registro;
    }
}

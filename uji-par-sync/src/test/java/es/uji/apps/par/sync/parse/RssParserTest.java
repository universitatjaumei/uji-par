package es.uji.apps.par.sync.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import es.uji.apps.par.sync.rss.jaxb.Item;
import es.uji.apps.par.sync.rss.jaxb.Rss;

public class RssParserTest
{
    private RssParser parser;

    @Before
    public void before() throws JAXBException
    {
        parser = new RssParser();
    }

    @Test
    public void testParse() throws JAXBException
    {
        InputStream is = loadFromClasspath("rss.xml");

        Rss rss = parser.parse(is);

        assertNotNull("Rss parseado no es nulo", rss);
        assertNotNull("Channel parseado no es nulo", rss.getChannel());
        assertNotNull("Items parseado no es nulo", rss.getChannel().getItems());
        assertEquals("3 items", 3, rss.getChannel().getItems().size());

        Item primero = rss.getChannel().getItems().get(0);
        assertEquals("Title del primero", "Madre Coraje", primero.getTitle());
        assertEquals("Url del segundo", "http://localhost:8080/cultura/paranimf/programacio/setembre/madre-coraje/coraje.jpg",
                primero.getEnclosures().get(0).getUrl());
        assertTrue(
                "Contenido del segundo",
                primero.getContenido()
                        .startsWith(
                                "<p>\r\n\t<strong>Adaptaci&oacute; i direcci&oacute;</strong>: Ricardo Iniesta"));
        assertEquals("Asientos numerados del primero", "si", primero.getSeientsNumerats());
        assertEquals("Tipo del primero", "teatre", primero.getTipo());
        assertEquals("Compañía del primero", "Atalaya", primero.getCompanyia());
        assertEquals("Duración del primero", "120", primero.getDuracio());
        assertEquals("Resumen del primero", "Basada en la Historia de la vida de la estafadora y aventurera Coraje de Grimmelshausen.", primero.getResumen());
    }

    private InputStream loadFromClasspath(String filePath)
    {
        return RssParser.class.getClassLoader().getResourceAsStream(filePath);
    }
}

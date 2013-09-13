package es.uji.apps.par.sync.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

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

        assertEquals("Title del primero", "Madre Coraje", rss.getChannel().getItems().get(0).getTitle());
    }

    private InputStream loadFromClasspath(String filePath)
    {
        return RssParser.class.getClassLoader().getResourceAsStream(filePath);
    }
}

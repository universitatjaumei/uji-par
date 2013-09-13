package es.uji.apps.par.sync.parse;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import es.uji.apps.par.sync.rss.jaxb.Rss;

public class RssParser
{
    private Unmarshaller unmarshaller;

    public RssParser() throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    public Rss parse(InputStream inputStream) throws JAXBException
    {
        return (Rss) unmarshaller.unmarshal(inputStream);
    }
}

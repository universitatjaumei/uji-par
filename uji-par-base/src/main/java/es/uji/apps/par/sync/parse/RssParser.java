package es.uji.apps.par.sync.parse;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;

import es.uji.apps.par.sync.rss.jaxb.Rss;

@Service
public class RssParser
{
    private Unmarshaller unmarshaller;

    public RssParser()
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        }
        catch (JAXBException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Rss parse(InputStream inputStream) throws JAXBException
    {
        return (Rss) unmarshaller.unmarshal(inputStream);
    }
}

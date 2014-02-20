package es.uji.apps.par.sync.uji;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.xml.bind.JAXBException;

public interface EventosSync
{
    public void sync(InputStream rssInputStream) throws JAXBException, MalformedURLException, IOException, ParseException;
}
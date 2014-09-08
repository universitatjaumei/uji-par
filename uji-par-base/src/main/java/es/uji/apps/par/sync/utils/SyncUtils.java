package es.uji.apps.par.sync.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncUtils
{
	private static final Logger log = LoggerFactory.getLogger(SyncUtils.class);
	
    public static byte[] getImageFromUrl(String url) throws MalformedURLException
    {
    	try {
    		InputStream urlInputStream = new URL(url).openStream();

    		return IOUtils.toByteArray(urlInputStream);
    	} catch (IOException e) {
    		log.info("La imagen " + url + "no existe", e);
    		return null;
    	}
    }
}

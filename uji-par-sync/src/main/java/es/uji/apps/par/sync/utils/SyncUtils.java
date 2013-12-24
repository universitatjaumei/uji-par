package es.uji.apps.par.sync.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class SyncUtils
{
    public static byte[] getImageFromUrl(String url) throws MalformedURLException, IOException
    {
        InputStream urlInputStream = new URL(url).openStream();

        return IOUtils.toByteArray(urlInputStream);
    }
}

package es.uji.apps.par.sync;

import java.io.InputStream;

import org.junit.Ignore;

import es.uji.apps.par.config.Configuration;

@Ignore
public class SyncBaseTest
{
    public SyncBaseTest()
    {
        Configuration.desactivaLogGmail();
    }
    
    protected InputStream loadFromClasspath(String filePath)
    {
        return SyncBaseTest.class.getClassLoader().getResourceAsStream(filePath);

    }
}
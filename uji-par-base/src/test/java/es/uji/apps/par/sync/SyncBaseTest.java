package es.uji.apps.par.sync;

import es.uji.apps.par.config.Configuration;
import org.junit.Ignore;

import java.io.InputStream;

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
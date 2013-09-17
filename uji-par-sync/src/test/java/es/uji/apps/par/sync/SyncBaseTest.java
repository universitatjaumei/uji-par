package es.uji.apps.par.sync;

import java.io.InputStream;

public class SyncBaseTest
{
    protected InputStream loadFromClasspath(String filePath)
    {
        return SyncBaseTest.class.getClassLoader().getResourceAsStream(filePath);
    }
}
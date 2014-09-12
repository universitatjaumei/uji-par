package es.uji.apps.par.services.rest;

import org.junit.Ignore;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import es.uji.apps.par.config.Configuration;

@Ignore
public class BaseResourceTest extends JerseyTest
{
    public BaseResourceTest(WebAppDescriptor build)
    {
        super(build);

        desactivarAppenderLogGmail();
    }

    private void desactivarAppenderLogGmail()
    {
        Configuration.desactivaLogGmail();
    }
    
    @Override
    protected int getPort(int defaultPort) {
        return 19998;
    }
}

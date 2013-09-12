package es.uji.apps.par.services.rest;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import es.uji.apps.par.config.Configuration;

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
}

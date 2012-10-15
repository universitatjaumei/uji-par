package es.uji.apps.par.services.rest;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.util.Log4jConfigListener;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

public class UsersResourceTest extends JerseyTest
{
    private WebResource resource;

    public UsersResourceTest()
    {
        super(new WebAppDescriptor.Builder("es.uji.apps.par.services.rest")
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .contextParam("log4jConfigLocation", "src/main/webapp/WEB-INF/log4j.properties")
                .contextParam("webAppRootKey", "paranimf-fw-uji.root")
                .contextListenerClass(Log4jConfigListener.class)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .servletClass(SpringServlet.class).build());

        this.resource = resource();
    }

    @Override
    protected TestContainerFactory getTestContainerFactory()
    {
        return new GrizzlyWebTestContainerFactory();
    }

    @Test
    public void users()
    {
        ClientResponse response = resource.path("usuarios").get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertTrue(serviceResponse.getData().isEmpty());
    }
}
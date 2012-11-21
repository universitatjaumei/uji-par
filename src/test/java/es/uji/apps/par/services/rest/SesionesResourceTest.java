package es.uji.apps.par.services.rest;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.util.Log4jConfigListener;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

import es.uji.apps.par.model.ParSesion;

public class SesionesResourceTest extends JerseyTest {
	private WebResource resource;
	
	public SesionesResourceTest()
    {
        super(new WebAppDescriptor.Builder(
				"es.uji.apps.par.services.rest;com.fasterxml.jackson.jaxrs.json;es.uji.apps.par.exceptions")
				.contextParam("contextConfigLocation", "classpath:applicationContext-test.xml")
				.contextParam("log4jConfigLocation", "src/main/webapp/WEB-INF/log4j.properties")
				.contextParam("webAppRootKey", "paranimf-fw-uji.root")
				.contextListenerClass(Log4jConfigListener.class)
				.contextListenerClass(ContextLoaderListener.class)
				.clientConfig(clientConfiguration())
				.requestListenerClass(RequestContextListener.class)
				.servletClass(SpringServlet.class).build());

		this.client().addFilter(new LoggingFilter());
		this.resource = resource();
    }
    
    private static ClientConfig clientConfiguration() {
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().add(JacksonJaxbJsonProvider.class);
		return config;
	}

    @Override
    protected TestContainerFactory getTestContainerFactory()
    {
        return new GrizzlyWebTestContainerFactory();
    }
    
    @Test
    public void getSesiones() {
    	ClientResponse response = resource.path("evento").path("1").path("sesiones").get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }
    
    private ParSesion preparaSesion() {
    	ParSesion parSesion = new ParSesion();
    	parSesion.setFechaCelebracion("01/01/2012");
    	parSesion.setCanalInternet("1");
    	parSesion.setFechaFinVentaOnline("");
    	parSesion.setFechaInicioVentaOnline("");
    	/*ParEventoDTO parEventoDTO = new ParEventoDTO();
    	parEventoDTO.setId(1);
    	parSesion.setEvento(parEventoDTO);*/
   	
    	return parSesion;
    }
    
    //TODO -> realizar
    /*@Test
    public void addSesion() {
    	ParSesion parSesion = preparaSesion();
    	
    	ClientResponse response = resource.path("evento").path("1").path("sesiones").post(ClientResponse.class, parSesion);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void addSesionWithoutFechaCelebracion() {
    	
    }
    
    @Test
    public void addSesionWithFechaEndVentaAnteriorFechaStartVenta() {
    	
    }
    
    @Test
    public void addSesionWithFechaEndVentaPosteriorFechaCelebracion() {
    	
    }
    
    @Test
    public void addSesionWithFechaStartVentaPosteriorFechaCelebracion() {
    	
    }
    
    @Test
    public void addSesionWithoutHoraCelebracion() {
    	
    }*/
}

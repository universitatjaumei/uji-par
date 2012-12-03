package es.uji.apps.par.services.rest;

import java.util.HashMap;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.util.Log4jConfigListener;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

import es.uji.apps.par.exceptions.ParCampoRequeridoException;
import es.uji.apps.par.model.ParLocalizacion;
import es.uji.apps.par.model.ParResponseMessage;

public class LocalizacionesResourceTest extends JerseyTest
{
    private WebResource resource;

    public LocalizacionesResourceTest()
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
		this.resource = resource().path("localizacion");
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
    
    private ParLocalizacion preparaLocalizacion() {
		return new ParLocalizacion("Prueba");
	}

    @Test
    public void getLocalizaciones()
    {
        ClientResponse response = resource.get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }
    
    @Test
	public void addLocalizacionWithoutNombre() {
		ParLocalizacion localizacion = preparaLocalizacion();
		localizacion.setNombre(null);
		
		ClientResponse response = resource.post(ClientResponse.class, localizacion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    	
    	ParResponseMessage resultatOperacio = response.getEntity(new GenericType<ParResponseMessage>(){});
		Assert.assertEquals(ParCampoRequeridoException.CAMPO_OBLIGATORIO + "Nombre", resultatOperacio.getMessage());
	}
	
	@SuppressWarnings("rawtypes")
	private String getFieldFromRestResponse(RestResponse restResponse, String field) {
		return ((HashMap) restResponse.getData().get(0)).get(field).toString();
	}
	
	@Test
	public void addLocalizacion() {
		ParLocalizacion localizacion = preparaLocalizacion();
		ClientResponse response = resource.post(ClientResponse.class, localizacion);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
    	Assert.assertTrue(restResponse.getSuccess());
    	Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
    	Assert.assertEquals(localizacion.getNombre(), getFieldFromRestResponse(restResponse, "nombre"));
	}
    
    @Test
	public void updateLocalizacion() {
		ParLocalizacion localizacion = preparaLocalizacion();
		ClientResponse response = resource.post(ClientResponse.class, localizacion);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		localizacion.setNombre("Prueba2");
		
		response = resource.path(id).put(ClientResponse.class, localizacion);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
		Assert.assertEquals(localizacion.getNombre(), getFieldFromRestResponse(restResponse, "nombre"));
	}
    
    @Test
	public void updateLocalizacionAndRemoveNombre() {
		ParLocalizacion localizacion = preparaLocalizacion();
		ClientResponse response = resource.post(ClientResponse.class, localizacion);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		localizacion.setNombre("");
		response = resource.path(id).put(ClientResponse.class, localizacion);
		Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
		ParResponseMessage parResponseMessage = response.getEntity(new GenericType<ParResponseMessage>(){});
		
		Assert.assertEquals(ParCampoRequeridoException.CAMPO_OBLIGATORIO + "Nombre", parResponseMessage.getMessage());
	}
}
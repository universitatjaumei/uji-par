package es.uji.apps.par.services.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;
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
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

import es.uji.apps.par.exceptions.ParCampoRequeridoException;
import es.uji.apps.par.model.ParResponseMessage;
import es.uji.apps.par.model.ParTipoEvento;

public class EventosResourceTest extends JerseyTest {
	private WebResource resource;

    public EventosResourceTest()
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
    public void getEventos()
    {
        ClientResponse response = resource.path("evento").get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }
    
    @Test
	public void addEventoWithoutTitulo() {
		FormDataMultiPart parEvento = preparaEvento(new ParTipoEvento());
		parEvento.getField("titulo").setValue("");
		
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    	ParResponseMessage resultatOperacio = response.getEntity(new GenericType<ParResponseMessage>(){});
		Assert.assertEquals(ParCampoRequeridoException.CAMPO_OBLIGATORIO + "Título", resultatOperacio.getMessage());
	}
    
    @Test
	public void addEventoWithoutTipoEvento() {
		FormDataMultiPart parEvento = preparaEvento(new ParTipoEvento());
		parEvento.getField("tipoEvento").setValue("");
		
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    	ParResponseMessage resultatOperacio = response.getEntity(new GenericType<ParResponseMessage>(){});
		Assert.assertEquals(ParCampoRequeridoException.CAMPO_OBLIGATORIO + "Tipo de evento", resultatOperacio.getMessage());
	}
	
	private String getFieldFromRestResponse(RestResponse restResponse, String field) {
		return ((HashMap) restResponse.getData().get(0)).get(field).toString();
	}
	
	private ParTipoEvento addTipoEvento() {
		ParTipoEvento parTipoEvento = new ParTipoEvento("prueba");
		ClientResponse response = resource.path("tipoevento").post(ClientResponse.class, parTipoEvento);
		RestResponse serviceResponse = response.getEntity(RestResponse.class);
		
		Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
		return parTipoEvento;
	}
	
	private FormDataMultiPart preparaEvento(ParTipoEvento tipoEvento) {
		FormDataMultiPart f = new FormDataMultiPart();
		f.field("titulo", "titulo");
		f.field("tipoEvento", String.valueOf(tipoEvento.getId()));
		
		return f;
	}
	
	@Test
	public void addEvento() {
		ParTipoEvento parTipoEvento = addTipoEvento();
		FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
    	Assert.assertTrue(restResponse.getSuccess());
    	Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
    	Assert.assertEquals(parEvento.getField("titulo").getValue(), getFieldFromRestResponse(restResponse, "titulo"));
	}
    
    @Test
	public void updateEvento() {
    	ParTipoEvento parTipoEvento = addTipoEvento();
		FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		parEvento.field("premios", "premios");
		response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
		Assert.assertEquals(parEvento.getField("titulo").getValue(), getFieldFromRestResponse(restResponse, "titulo"));
	}
    
    @Test
	public void updateEventoAndRemoveNombre() {
    	ParTipoEvento parTipoEvento = addTipoEvento();
		FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		parEvento.getField("titulo").setValue("");
		response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
		Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
		ParResponseMessage parResponseMessage = response.getEntity(new GenericType<ParResponseMessage>(){});
		
		Assert.assertEquals(ParCampoRequeridoException.CAMPO_OBLIGATORIO + "Título", parResponseMessage.getMessage());
	}
    
    @Test
	public void updateEventoAndRemoveTipoEvento() {
    	ParTipoEvento parTipoEvento = addTipoEvento();
		FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		parEvento.getField("tipoEvento").setValue("");
		response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
		Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
		ParResponseMessage parResponseMessage = response.getEntity(new GenericType<ParResponseMessage>(){});
		
		Assert.assertEquals(ParCampoRequeridoException.CAMPO_OBLIGATORIO + "Tipo de evento", parResponseMessage.getMessage());
	}
    
    @Test
    public void addEventoWithImagen() {
    	ParTipoEvento parTipoEvento = addTipoEvento();
		FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
		parEvento.bodyPart(new StreamDataBodyPart("dataBinary", new ByteArrayInputStream("hola".getBytes())));
		
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
    	Assert.assertTrue(restResponse.getSuccess());
    	Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
    	Assert.assertEquals(parEvento.getField("titulo").getValue(), getFieldFromRestResponse(restResponse, "titulo"));
    }
    
    @Test
    public void updateEventoAndAddImagen() {
    	ParTipoEvento parTipoEvento = addTipoEvento();
		FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
		ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		parEvento.bodyPart(new StreamDataBodyPart("dataBinary", new ByteArrayInputStream("hola".getBytes())));
		response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, parEvento);
		Assert.assertTrue(restResponse.getSuccess());
    	Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
    	Assert.assertEquals(parEvento.getField("titulo").getValue(), getFieldFromRestResponse(restResponse, "titulo"));
    }
}

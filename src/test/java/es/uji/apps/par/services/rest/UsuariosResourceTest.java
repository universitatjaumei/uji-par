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

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.model.Usuario;

//solamente necesario si vamos a usar alguna clase DAO desde aqui
/*@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })*/
public class UsuariosResourceTest extends JerseyTest
{
    private WebResource resource;

    public UsuariosResourceTest()
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
		this.resource = resource().path("usuario");
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
    
    private Usuario preparaUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNombre("Prueba");
		usuario.setUsuario("login");
		usuario.setMail("mail");
		
		return usuario;
	}

    @Test
    public void getUsers()
    {
        ClientResponse response = resource.get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }
    
    @Test
	public void addUsuarioWithoutMail() {
    	Usuario parUsuario = preparaUsuario();
    	parUsuario.setMail(null);
    	ClientResponse response = resource.post(ClientResponse.class, parUsuario);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    	ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>(){});
		Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Mail", resultatOperacio.getMessage());
    }
	
	@Test
	public void addUsuarioWithoutNombre() {
		Usuario parUsuario = preparaUsuario();
		parUsuario.setNombre(null);
		ClientResponse response = resource.post(ClientResponse.class, parUsuario);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    	ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>(){});
		Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Nombre", resultatOperacio.getMessage());
	}
	
	@Test
	public void addUsuarioWithoutLogin() {
		Usuario parUsuario = preparaUsuario();
		parUsuario.setUsuario(null);
		ClientResponse response = resource.post(ClientResponse.class, parUsuario);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    	ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>(){});
		Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Usuario", resultatOperacio.getMessage());
	}
	
	private String getFieldFromRestResponse(RestResponse restResponse, String field) {
		return ((HashMap) restResponse.getData().get(0)).get(field).toString();
	}
	
	@Test
	public void addUsuario() {
		Usuario parUsuario = preparaUsuario();
		ClientResponse response = resource.post(ClientResponse.class, parUsuario);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
    	Assert.assertTrue(restResponse.getSuccess());
    	Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
    	Assert.assertEquals(parUsuario.getNombre(), getFieldFromRestResponse(restResponse, "nombre"));
	}
    
    @Test
	public void updateUsuario() {
		Usuario parUsuario = preparaUsuario();
		ClientResponse response = resource.post(ClientResponse.class, parUsuario);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		parUsuario.setNombre("Prueba2");
		response = resource.path(id).put(ClientResponse.class, parUsuario);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
		Assert.assertEquals(parUsuario.getNombre(), getFieldFromRestResponse(restResponse, "nombre"));
	}
    
    @Test
	public void updateUsuarioAndRemoveMail() {
		Usuario parUsuario = preparaUsuario();
		ClientResponse response = resource.post(ClientResponse.class, parUsuario);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>(){});
		
    	String id = getFieldFromRestResponse(restResponse, "id");
		Assert.assertNotNull(id);
		
		parUsuario.setMail("");
		response = resource.path(id).put(ClientResponse.class, parUsuario);
		Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
		ResponseMessage parResponseMessage = response.getEntity(new GenericType<ResponseMessage>(){});
		
		Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Mail", parResponseMessage.getMessage());
	}
}
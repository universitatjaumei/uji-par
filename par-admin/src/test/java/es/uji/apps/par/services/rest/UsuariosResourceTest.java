package es.uji.apps.par.services.rest;

import java.util.HashMap;

import javax.ws.rs.core.Response.Status;

import es.uji.apps.par.model.ResultatOperacio;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;

import es.uji.apps.par.exceptions.CampoRequeridoException;
import es.uji.apps.par.exceptions.ResponseMessage;
import es.uji.apps.par.model.Usuario;

//solamente necesario si vamos a usar alguna clase DAO desde aqui
/*@RunWith(SpringJUnit4ClassRunner.class)
 @TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
 @ContextConfiguration(locations = { "/applicationContext-db-test.xml" })*/
public class UsuariosResourceTest extends BaseResourceTest
{
    private WebResource resource;

    public UsuariosResourceTest()
    {
        super(
                new WebAppDescriptor.Builder(
                        "es.uji.apps.par.services.rest;com.fasterxml.jackson.jaxrs.json;es.uji.apps.par")
                        .contextParam("contextConfigLocation",
                                "classpath:applicationContext-db-test.xml")
                        .contextParam("webAppRootKey", "paranimf-fw-uji.root")
                        .contextListenerClass(ContextLoaderListener.class)
                        .clientConfig(clientConfiguration())
                        .requestListenerClass(RequestContextListener.class)
                        .servletClass(SpringServlet.class).build());

        this.client().addFilter(new LoggingFilter());
        this.resource = resource().path("usuario");
    }

    private static ClientConfig clientConfiguration()
    {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJaxbJsonProvider.class);
        return config;
    }

    @Override
    protected TestContainerFactory getTestContainerFactory()
    {
        return new GrizzlyWebTestContainerFactory();
    }

    private Usuario preparaUsuario(String nombre)
    {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsuario(nombre);
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
    public void addUsuarioWithoutMail()
    {
        Usuario parUsuario = preparaUsuario("addUsuarioWithoutMail");
        parUsuario.setMail(null);
        ClientResponse response = resource.type("application/json").post(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Mail",
                resultatOperacio.getDescripcio());
    }

    @Test
    public void addUsuarioWithoutNombre()
    {
        Usuario parUsuario = preparaUsuario("addUsuarioWithoutNombre");
        parUsuario.setNombre(null);
        ClientResponse response = resource.type("application/json").post(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Nombre",
                resultatOperacio.getDescripcio());
    }

    @Test
    public void addUsuarioWithoutLogin()
    {
        Usuario parUsuario = preparaUsuario("addUsuarioWithoutLogin");
        parUsuario.setUsuario(null);
        ClientResponse response = resource.type("application/json").post(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Usuario",
                resultatOperacio.getDescripcio());
    }

    private String getFieldFromRestResponse(RestResponse restResponse, String field)
    {
        return ((HashMap) restResponse.getData().get(0)).get(field).toString();
    }

    @Test
    public void addUsuario()
    {
        Usuario parUsuario = preparaUsuario("addUsuario");
        ClientResponse response = resource.type("application/json").post(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(parUsuario.getNombre(),
                getFieldFromRestResponse(restResponse, "nombre"));
    }

    @Test
    @Ignore
    public void updateUsuario()
    {
        Usuario parUsuario = preparaUsuario("updateUsuario");
        ClientResponse response = resource.type("application/json").post(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parUsuario.setNombre("updateUsuario2");
        response = resource.path(id).type("application/json").put(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        Assert.assertEquals(parUsuario.getNombre(),
                getFieldFromRestResponse(restResponse, "nombre"));
    }

    @Test
    public void updateUsuarioAndRemoveMail()
    {
        Usuario parUsuario = preparaUsuario("updateUsuarioAndRemoveMail");
        ClientResponse response = resource.type("application/json").post(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parUsuario.setMail("");
        response = resource.path(id).type("application/json").put(ClientResponse.class, parUsuario);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio parResponseMessage = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Mail",
                parResponseMessage.getDescripcio());
    }
}
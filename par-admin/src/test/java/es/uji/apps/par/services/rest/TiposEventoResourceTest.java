package es.uji.apps.par.services.rest;

import java.util.HashMap;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
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

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.model.TipoEvento;

//solamente necesario si vamos a usar alguna clase DAO desde aqui
/*@RunWith(SpringJUnit4ClassRunner.class)
 @TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
 @ContextConfiguration(locations = { "/applicationContext-db-test.xml" })*/
public class TiposEventoResourceTest extends BaseResourceTest
{
    private WebResource resource;

    public TiposEventoResourceTest()
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
        this.resource = resource().path("tipoevento");
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

    private TipoEvento preparaTipoEvento()
    {
        TipoEvento tipoEvento = new TipoEvento();
        tipoEvento.setNombreEs("Prueba");

        return tipoEvento;
    }

    @Test
    public void getTiposEventos()
    {
        ClientResponse response = resource.get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }

    @Test
    public void addTipoEventoWithoutNombre()
    {
        TipoEvento parTipoEvento = preparaTipoEvento();
        parTipoEvento.setNombreEs(null);
        ClientResponse response = resource.post(ClientResponse.class, parTipoEvento);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>()
        {
        });
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Nombre",
                resultatOperacio.getMessage());
    }

    private String getFieldFromRestResponse(RestResponse restResponse, String field)
    {
        return ((HashMap) restResponse.getData().get(0)).get(field).toString();
    }

    @Test
    public void addTipoEvento()
    {
        TipoEvento parTipoEvento = preparaTipoEvento();
        ClientResponse response = resource.post(ClientResponse.class, parTipoEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(parTipoEvento.getNombreEs(),
                getFieldFromRestResponse(restResponse, "nombreEs"));
    }

    @Test
    public void updateTipoEvento()
    {
        TipoEvento parTipoEvento = preparaTipoEvento();
        ClientResponse response = resource.post(ClientResponse.class, parTipoEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parTipoEvento.setNombreEs("Prueba2");
        response = resource.path(id).put(ClientResponse.class, parTipoEvento);
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        Assert.assertEquals(parTipoEvento.getNombreEs(),
                getFieldFromRestResponse(restResponse, "nombreEs"));
    }

    @Test
    public void updateTipoEventoAndRemoveNombre()
    {
        TipoEvento parTipoEvento = preparaTipoEvento();
        ClientResponse response = resource.post(ClientResponse.class, parTipoEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parTipoEvento.setNombreEs("");
        response = resource.path(id).put(ClientResponse.class, parTipoEvento);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage parResponseMessage = response.getEntity(new GenericType<ResponseMessage>()
        {
        });

        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Nombre",
                parResponseMessage.getMessage());
    }
}
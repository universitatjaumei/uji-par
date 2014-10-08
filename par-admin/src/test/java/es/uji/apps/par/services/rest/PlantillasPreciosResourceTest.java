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

import es.uji.apps.par.exceptions.CampoRequeridoException;
import es.uji.apps.par.exceptions.ResponseMessage;
import es.uji.apps.par.model.Plantilla;

public class PlantillasPreciosResourceTest extends BaseResourceTest {
	private WebResource resource;
	
	public PlantillasPreciosResourceTest()
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
        this.resource = resource().path("plantillaprecios");
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
    
    private Plantilla preparaPlantilla()
    {
        return new Plantilla("Prueba");
    }

    @Test
    public void getPlantillaPrecios()
    {
        ClientResponse response = resource.get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }

    @Test
    public void addPlantillaWithoutNombre()
    {
        Plantilla plantilla = preparaPlantilla();
        plantilla.setNombre(null);

        ClientResponse response = resource.post(ClientResponse.class, plantilla);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>()
        {
        });
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Nombre",
                resultatOperacio.getMessage());
    }

    @SuppressWarnings("rawtypes")
    private String getFieldFromRestResponse(RestResponse restResponse, String field)
    {
        return ((HashMap) restResponse.getData().get(0)).get(field).toString();
    }

    @Test
    public void addPlantilla()
    {
        Plantilla plantillaPrecios = preparaPlantilla();
        ClientResponse response = resource.post(ClientResponse.class, plantillaPrecios);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(plantillaPrecios.getNombre(),
                getFieldFromRestResponse(restResponse, "nombre"));
    }

    @Test
    public void updatePlantilla()
    {
        Plantilla plantilla = preparaPlantilla();
        ClientResponse response = resource.post(ClientResponse.class, plantilla);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        plantilla.setNombre("Prueba2");

        response = resource.path(id).put(ClientResponse.class, plantilla);
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        Assert.assertEquals(plantilla.getNombre(),
                getFieldFromRestResponse(restResponse, "nombre"));
    }

    @Test
    public void updatePlantillaAndRemoveNombre()
    {
        Plantilla plantilla = preparaPlantilla();
        ClientResponse response = resource.post(ClientResponse.class, plantilla);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        plantilla.setNombre("");
        response = resource.path(id).put(ClientResponse.class, plantilla);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage parResponseMessage = response.getEntity(new GenericType<ResponseMessage>()
        {
        });

        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Nombre",
                parResponseMessage.getMessage());
    }
}

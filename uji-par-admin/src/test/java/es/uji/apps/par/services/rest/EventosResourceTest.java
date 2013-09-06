package es.uji.apps.par.services.rest;

import java.io.ByteArrayInputStream;
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

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.model.TipoEvento;

public class EventosResourceTest extends BaseResourceTest
{
    private WebResource resource;

    public EventosResourceTest()
    {
        super(
                new WebAppDescriptor.Builder(
                        "es.uji.apps.par.services.rest;com.fasterxml.jackson.jaxrs.json;es.uji.apps.par")
                        .contextParam("contextConfigLocation",
                                "classpath:applicationContext-db-test.xml")
                        .contextParam("log4jConfigLocation",
                                "src/main/webapp/WEB-INF/log4j.properties")
                        .contextParam("webAppRootKey", "paranimf-fw-uji.root")
                        .contextListenerClass(Log4jConfigListener.class)
                        .contextListenerClass(ContextLoaderListener.class)
                        .clientConfig(clientConfiguration())
                        .requestListenerClass(RequestContextListener.class)
                        .servletClass(SpringServlet.class).build());

        this.client().addFilter(new LoggingFilter());
        this.resource = resource();
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
    public void addEventoWithoutTitulo()
    {
        FormDataMultiPart parEvento = preparaEvento(new TipoEvento());
        parEvento.getField("tituloEs").setValue("");

        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>()
        {
        });
        Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Título",
                resultatOperacio.getMessage());
    }

    @Test
    public void addEventoWithoutTipoEvento()
    {
        FormDataMultiPart parEvento = preparaEvento(new TipoEvento());
        parEvento.getField("tipoEvento").setValue("");

        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>()
        {
        });
        Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Tipo de evento",
                resultatOperacio.getMessage());
    }

    private String getFieldFromRestResponse(RestResponse restResponse, String field)
    {
        return ((HashMap) restResponse.getData().get(0)).get(field).toString();
    }

    private TipoEvento addTipoEvento()
    {
        TipoEvento parTipoEvento = new TipoEvento("prueba");
        ClientResponse response = resource.path("tipoevento").post(ClientResponse.class,
                parTipoEvento);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
        int id = Integer.valueOf(((HashMap) serviceResponse.getData().get(0)).get("id").toString());
        parTipoEvento.setId(id);
        return parTipoEvento;
    }

    private FormDataMultiPart preparaEvento(TipoEvento tipoEvento)
    {
        FormDataMultiPart f = new FormDataMultiPart();
        f.field("tituloEs", "titulo");
        f.field("tipoEvento", String.valueOf(tipoEvento.getId()));
        f.field("asientosNumerados", "1");

        return f;
    }

    @Test
    public void addEvento()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(parEvento.getField("tituloEs").getValue(),
                getFieldFromRestResponse(restResponse, "tituloEs"));
    }

    @Test
    public void updateEvento()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parEvento.field("premios", "premios");
        response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void updateEventoAndRemoveNombre()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parEvento.getField("tituloEs").setValue("");
        response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage parResponseMessage = response.getEntity(new GenericType<ResponseMessage>()
        {
        });

        Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Título",
                parResponseMessage.getMessage());
    }

    @Test
    public void updateEventoAndRemoveTipoEvento()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parEvento.getField("tipoEvento").setValue("");
        response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ResponseMessage parResponseMessage = response.getEntity(new GenericType<ResponseMessage>()
        {
        });

        Assert.assertEquals(CampoRequeridoException.CAMPO_OBLIGATORIO + "Tipo de evento",
                parResponseMessage.getMessage());
    }

    @Test
    public void addEventoWithImagen()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        parEvento.bodyPart(new StreamDataBodyPart("dataBinary", new ByteArrayInputStream("hola"
                .getBytes())));

        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(parEvento.getField("tituloEs").getValue(),
                getFieldFromRestResponse(restResponse, "tituloEs"));
    }

    @Test
    public void updateEventoAndAddImagen()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        parEvento.bodyPart(new StreamDataBodyPart("dataBinary", new ByteArrayInputStream("hola"
                .getBytes())));
        response = resource.path("evento").path(id).type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(parEvento.getField("tituloEs").getValue(),
                getFieldFromRestResponse(restResponse, "tituloEs"));
    }

    @Test
    public void deleteImagen()
    {
        TipoEvento parTipoEvento = addTipoEvento();
        FormDataMultiPart parEvento = preparaEvento(parTipoEvento);
        parEvento.bodyPart(new StreamDataBodyPart("dataBinary", new ByteArrayInputStream("hola"
                .getBytes())));
        ClientResponse response = resource.path("evento").type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .post(ClientResponse.class, parEvento);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });

        String id = getFieldFromRestResponse(restResponse, "id");
        Assert.assertNotNull(id);

        response = resource.path("evento").path(id).path("imagen").get(ClientResponse.class);
        ByteArrayInputStream imagen = (ByteArrayInputStream) response.getEntityInputStream();
        Assert.assertNotNull(imagen);

        response = resource.path("evento").path(id).path("imagen").delete(ClientResponse.class);
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());

        response = resource.path("evento").path(id).path("imagen").get(ClientResponse.class);
        imagen = (ByteArrayInputStream) response.getEntityInputStream();
        Assert.assertTrue(imagen.available() == 0);
    }
}

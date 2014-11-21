package es.uji.apps.par.services.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;
import es.uji.apps.par.dao.CinesDAO;
import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.exceptions.CampoRequeridoException;
import es.uji.apps.par.exceptions.FechasInvalidasException;
import es.uji.apps.par.exceptions.ResponseMessage;
import es.uji.apps.par.model.*;
import es.uji.apps.par.utils.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class SesionesResourceTest extends BaseResourceTest
{
    private WebResource resource;

    @Autowired
    SalasDAO salasDAO;

    @Autowired
    CinesDAO cinesDAO;

    public SesionesResourceTest()
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

    public Cine getCine(){
        Cine cine = new Cine();
        cine.setNombre("Prueba");
        cine = cinesDAO.addCine(cine);

        return cine;
    }

    private Sala getSala() {
        Cine cine = getCine();

        Sala sala = new Sala();
        sala.setAsientos(10);
        sala.setAsientosDiscapacitados(2);
        sala.setAsientosNoReservados(3);
        sala.setCine(cine);
        sala = salasDAO.addSala(sala);

        return sala;
    }

    private Plantilla preparaPlantilla()
    {
        Plantilla plantilla = new Plantilla("Prueba");
        plantilla.setSala(getSala());

        return plantilla;
    }
    
    private Sesion preparaSesion()
    {
        return preparaSesion(null);
    }
    
    private Sesion preparaSesion(Plantilla plantilla) {
    	Sesion sesion = new Sesion();
        sesion.setFechaCelebracion("01/01/2012");
        sesion.setFechaInicioVentaOnline("01/01/2012");
        sesion.setFechaFinVentaOnline("01/01/2012");
        sesion.setHoraCelebracion("12:30");
        //sesion.setCanalInternet("1");
        sesion.setHoraInicioVentaOnline("12:00");
        sesion.setHoraFinVentaOnline("12:30");
        sesion.setPlantillaPrecios(plantilla);
        if (plantilla != null) {
            sesion.setSala(plantilla.getSala());
        }

        return sesion;
    }
    
    @SuppressWarnings("rawtypes")
	private TipoEvento addTipoEvento()
    {
        TipoEvento parTipoEvento = new TipoEvento("prueba");
        ClientResponse response = resource.path("tipoevento").type("application/json").post(ClientResponse.class, parTipoEvento);
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

    @SuppressWarnings("rawtypes")
	private String getFieldFromRestResponse(RestResponse restResponse, String field)
    {
        return ((HashMap) restResponse.getData().get(0)).get(field).toString();
    }
    
    private String addEvento(TipoEvento parTipoEvento) {
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
		return getFieldFromRestResponse(restResponse, "id");
	}
    
    private Plantilla addPlantilla()
    {
        Plantilla plantilla = preparaPlantilla();
        ClientResponse response = resource.path("plantillaprecios").type("application/json").post(ClientResponse.class, plantilla);
        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
        {
        });
        Assert.assertTrue(restResponse.getSuccess());
        Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
        Assert.assertEquals(plantilla.getNombre(),
                getFieldFromRestResponse(restResponse, "nombre"));
        
        plantilla.setId(Long.valueOf(getFieldFromRestResponse(restResponse, "id")));
        return plantilla;
    }
    
    @Test
    public void getSesiones()
    {
        ClientResponse response = resource.path("evento").path("1").path("sesiones")
                .get(ClientResponse.class);
        RestResponse serviceResponse = response.getEntity(RestResponse.class);

        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertTrue(serviceResponse.getSuccess());
        Assert.assertNotNull(serviceResponse.getData());
    }
    
    @Test 
    public void addSesionWithoutFechaCelebracion() { 
    	Sesion sesion = preparaSesion();
    	sesion.setFechaCelebracion(null);
    	
    	ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Fecha de celebración",
    	                resultatOperacio.getDescripcio());
    }

    
    @Test 
    public void addSesionWithoutHoraCelebracion() { 
    	Sesion sesion = preparaSesion();
    	sesion.setHoraCelebracion(null);
     
    	ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Hora de celebración",
    	                resultatOperacio.getDescripcio());
    }
    
    @Test
    @Ignore //Deshabilitar rollback
    public void addSesion() {
    	TipoEvento parTipoEvento = addTipoEvento();
    	String eventoId = addEvento(parTipoEvento);
    	Plantilla plantilla = addPlantilla();
    	Sesion sesion = preparaSesion(plantilla);
     
    	ClientResponse response = resource.path("evento").path(eventoId).path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus()); 
    	
    	RestResponse restResponse = response.getEntity(new GenericType<RestResponse>()
    	{
    	});
    	Assert.assertTrue(restResponse.getSuccess());
    	Assert.assertNotNull(getFieldFromRestResponse(restResponse, "id"));
    	
    	Assert.assertEquals(String.valueOf(DateUtils.addTimeToDate(sesion.getFechaCelebracion(), sesion.getHoraCelebracion()).getTime()),
            getFieldFromRestResponse(restResponse, "fechaCelebracion"));
    }

	@Test
    @Ignore //Deshabilitar rollback
    public void addSesionWithFechaEndVentaAnteriorFechaStartVenta() {
    	Sesion sesion = preparaSesion();
    	sesion.setFechaInicioVentaOnline("02/12/2012");
    	sesion.setFechaFinVentaOnline("01/12/2012");
    	
    	ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>()
        {
        });
        Assert.assertTrue(resultatOperacio.getMessage().startsWith(FechasInvalidasException.FECHA_INICIO_VENTA_POSTERIOR_FECHA_FIN_VENTA));
    }
    
    @Test 
    public void addSesionWithoutFechaInicioVentaOnline() {
        Sesion sesion = preparaSesion();
        sesion.setFechaInicioVentaOnline(null);
        sesion.setCanalInternet("1");
        
        ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Fecha de inicio de la venta online",
            resultatOperacio.getDescripcio());
    }
    
    @Test 
    public void addSesionWithoutFechaFinVentaOnline() {
    	Sesion sesion = preparaSesion();
        sesion.setFechaFinVentaOnline(null);
        sesion.setCanalInternet("1");
        
        ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Fecha de fin de la venta online",
            resultatOperacio.getDescripcio());
    }
    
    @Test 
    public void addSesionWithoutHoraInicioVentaOnline() {
    	Sesion sesion = preparaSesion();
        sesion.setHoraInicioVentaOnline(null);
        sesion.setCanalInternet("1");
        
        ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Hora de inicio de la venta online",
            resultatOperacio.getDescripcio());
    }
    
    @Test 
    public void addSesionWithoutHoraFinVentaOnline() {
    	Sesion sesion = preparaSesion();
        sesion.setHoraFinVentaOnline(null);
        sesion.setCanalInternet("1");
        
        ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());

        ResultatOperacio resultatOperacio = response.getEntity(ResultatOperacio.class);
        Assert.assertEquals(CampoRequeridoException.REQUIRED_FIELD + "Hora de fin de la venta online",
            resultatOperacio.getDescripcio());
    }
     
    @Test
    @Ignore //Deshabilitar rollback
    public void addSesionWithFechaEndVentaPosteriorFechaCelebracion() {
    	Sesion sesion = preparaSesion();
    	sesion.setFechaCelebracion("01/12/2012");
    	sesion.setFechaInicioVentaOnline("01/11/2012");
    	sesion.setFechaFinVentaOnline("03/12/2012");
    	
    	ClientResponse response = resource.path("evento").path("1").path("sesiones").type("application/json").post(ClientResponse.class, sesion);
    	ResponseMessage resultatOperacio = response.getEntity(new GenericType<ResponseMessage>()
        {
        });
        Assert.assertTrue(resultatOperacio.getMessage().startsWith(FechasInvalidasException.FECHA_FIN_VENTA_POSTERIOR_FECHA_CELEBRACION));
    }
}

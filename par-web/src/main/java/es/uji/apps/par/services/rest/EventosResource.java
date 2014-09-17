package es.uji.apps.par.services.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.apps.par.builders.PublicPageBuilderInterface;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.sanselan.ImageReadException;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.database.DatabaseHelper;
import es.uji.apps.par.database.DatabaseHelperFactory;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.EventoParaSync;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.EntradasService;
import es.uji.apps.par.services.EventosService;
import es.uji.apps.par.services.SesionesService;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.ImageUtils;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("evento")
public class EventosResource extends BaseResource
{
	
    @InjectParam
    private EventosService eventosService;

    @InjectParam
    private SesionesService sesionesService;

    @Context
    private HttpServletRequest request;

	@InjectParam
	private PublicPageBuilderInterface publicPageBuilderInterface;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventos()
    {
        if (!correctApiKey(request))
        {
            return apiAccessDenied();
        }
        
        List<Evento> eventos;
        
        eventos = eventosService.getEventosConSesiones();
        
        imagenesANull(eventos);

        return Response.ok().entity(new RestResponse(true, eventos, 0)).build();
    }
    
    @GET
    @Path("activos/online")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventosActivosParaVentaOnline()
    {
        if (!correctApiKey(request))
        {
            return apiAccessDenied();
        }
        
        List<EventoParaSync> eventos;
        
        eventos = eventosService.getEventosActivosParaVentaOnline();
        return Response.ok().entity(eventos).build();
    }

    private void imagenesANull(List<Evento> eventos)
    {
        for (Evento evento : eventos)
        {
            evento.setImagen(null);
        }
    }

    @GET
    @Path("{contenidoId}")
    @Produces(MediaType.TEXT_HTML)
    public Template getEvento(@PathParam("contenidoId") Long contenidoId, @QueryParam("lang") String lang) throws Exception
    {
        try
        {
            Evento evento = eventosService.getEventoByRssId(contenidoId);

            return getTemplateEvento(evento, lang);
        }
        catch (EventoNoEncontradoException e)
        {
            return getTemplateEventoNoEncontrado();
        }
    }

    private Template getTemplateEventoNoEncontrado() throws MalformedURLException, ParseException
    {
    	Locale locale = getLocale();
    	String language = locale.getLanguage();
        HTMLTemplate template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "eventoNoEncontrado", locale, APP);
        
        String url = request.getRequestURL().toString();

        template.put("pagina", publicPageBuilderInterface.buildPublicPageInfo(getBaseUrlPublic(), url, language.toString()));
        template.put("baseUrl", getBaseUrlPublic());
        template.put("lang", language);
        
        return template;
    }

    private Template getTemplateEvento(Evento evento, String langparam) throws MalformedURLException, ParseException
    {
        List<Sesion> sesiones = sesionesService.getSesiones(evento.getId());
        borrarEntradasSeleccionadasConAnterioridad();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "eventoDetalle", getLocale(), APP);

        String tipoEvento, titulo, companyia, duracion, caracteristicas, premios, descripcion;

        String language = getLocale(langparam).getLanguage();
		if (language.equals("ca"))
        {
            tipoEvento = evento.getParTiposEvento().getNombreVa();
            titulo = evento.getTituloVa();
            companyia = evento.getCompanyiaVa();
            duracion = evento.getDuracionVa();
            caracteristicas = evento.getCaracteristicasVa();
            premios = evento.getPremiosVa();
            descripcion = evento.getDescripcionVa();
        }
        else
        {
            tipoEvento = evento.getParTiposEvento().getNombreEs();
            titulo = evento.getTituloEs();
            companyia = evento.getCompanyiaEs();
            duracion = evento.getDuracionEs();
            caracteristicas = evento.getCaracteristicasEs();
            premios = evento.getPremiosEs();
            descripcion = evento.getDescripcionEs();
        }

        String url = request.getRequestURL().toString();

        template.put("pagina", publicPageBuilderInterface.buildPublicPageInfo(getBaseUrlPublic(), url, language.toString()));
        template.put("baseUrl", getBaseUrlPublic());
        
        template.put("tipoEvento", tipoEvento);
        template.put("titulo", titulo);
        template.put("companyia", companyia);
        template.put("duracion", duracion);
        template.put("caracteristicas", caracteristicas);
        template.put("premios", premios);
        template.put("descripcion", descripcion);

        template.put("evento", evento);
        template.put("sesiones", sesiones);
        template.put("sesionesPlantilla", getSesionesPlantilla(sesiones));
        template.put("lang", language);

        return template;
    }

	private void borrarEntradasSeleccionadasConAnterioridad() {
		currentRequest.getSession().removeAttribute(EntradasService.BUTACAS_COMPRA);
	}

    private List<Map<String, Object>> getSesionesPlantilla(List<Sesion> sesiones)
    {
    	DatabaseHelper databaseHelper = DatabaseHelperFactory.newInstance();
        List<Map<String, Object>> sesionesPlantilla = new ArrayList<Map<String, Object>>();
        Calendar cal = Calendar.getInstance();
        

        for (Sesion sesion : sesiones)
        {
        	if (sesion.getFechaCelebracion().before(cal.getTime()))
        		continue;
        	
            Map<String, Object> datos = new HashMap<String, Object>();

            datos.put("texto", getFechaSesion(sesion));
            
            datos.put("id", sesion.getId());
            datos.put("enPlazoVentaInternet", sesion.getEnPlazoVentaInternet());
            datos.put("canalInternet", databaseHelper.booleanToNumber(sesion.getCanalInternet()));
            datos.put("fechaInicioVentaOnline", sesion.getFechaInicioVentaOnline());

            datos.put(
                    "textoFechasInternet",
                    	ResourceProperties.getProperty(getLocale(), "venta.plazoInternet",
                            DateUtils.dateToSpanishStringWithHour(sesion.getFechaInicioVentaOnline()),
                            DateUtils.dateToSpanishStringWithHour(sesion.getFechaFinVentaOnline())));
            
            if (sesion.getEnPlazoVentaInternet())
                datos.put("clase", "contieneBoton");

            sesionesPlantilla.add(datos);
        }

        return sesionesPlantilla;
    }

    private String getFechaSesion(Sesion sesion)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(sesion.getFechaCelebracion());
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM HH.mm", getLocale());
        
        String diaSemana = ResourceProperties.getProperty(getLocale(), "dia.abreviado." + cal.get(Calendar.DAY_OF_WEEK));
        
        return diaSemana + " " + format.format(cal.getTime());
    }

    @GET
    @Path("{id}/imagen")
    public Response getImagenEvento(@PathParam("id") Long eventoId)
    {
        try
        {
            Evento evento = eventosService.getEvento(eventoId);

            return Response.ok(evento.getImagen()).type(evento.getImagenContentType()).build();
        }
        catch (EventoNoEncontradoException e)
        {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("{id}/imagenEntrada")
    public Response getImagenEntrada(@PathParam("id") Long eventoId) throws IOException, ImageReadException
    {
        try
        {
            Evento evento = eventosService.getEvento(eventoId);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] imagen = (evento.getImagen() != null)?evento.getImagen():eventosService.getImagenSustitutivaSiExiste();
            String contentType = (evento.getImagenContentType() != null)?evento.getImagenContentType():eventosService.getImagenSustitutivaContentType();
            
            //si no hay imagen sustitutiva, siempre devolvemos la de bbdd, aunque sea nula
            if (imagen == null)
            	imagen = evento.getImagen();
            
            if (contentType == null)
            	contentType = evento.getImagenContentType();
            
            ImageUtils.changeDpi(imagen, bos, 3);
            return Response.ok(bos.toByteArray()).type(contentType).build();
            
        }
        catch (EventoNoEncontradoException e)
        {
            return Response.noContent().build();
        }
    }

}
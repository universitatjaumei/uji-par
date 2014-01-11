package es.uji.apps.par.services.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.sanselan.ImageReadException;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.EventosService;
import es.uji.apps.par.services.SesionesService;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.ImageUtils;
import es.uji.apps.par.utils.ReportUtils;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("evento")
public class EventosResource extends BaseResource
{
    public static Logger log = Logger.getLogger(EventosResource.class);
    
    @InjectParam
    private EventosService eventosService;

    @InjectParam
    private SesionesService sesionesService;

    @Context
    private HttpServletRequest request;

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
    public Template getEvento(@PathParam("contenidoId") Long contenidoId) throws Exception
    {
        try
        {
            Evento evento = eventosService.getEventoByRssId(contenidoId);

            return getTemplateEvento(evento);
        }
        catch (EventoNoEncontradoException e)
        {
            return getTemplateEventoNoEncontrado();
        }
    }

    private Template getTemplateEventoNoEncontrado() throws MalformedURLException, ParseException
    {
        HTMLTemplate template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "eventoNoEncontrado", getLocale(), APP);
        
        String url = request.getRequestURL().toString();

        template.put("pagina", buildPublicPageInfo(getBaseUrlPublic(), url, getLocale().getLanguage().toString()));
        template.put("baseUrl", getBaseUrlPublic());
        
        return template;
    }

    private Template getTemplateEvento(Evento evento) throws MalformedURLException, ParseException
    {
        List<Sesion> sesiones = sesionesService.getSesiones(evento.getId());

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "eventoDetalle", getLocale(), APP);

        String tipoEvento, titulo, companyia, duracion, caracteristicas, premios, descripcion;

        if (getLocale().getLanguage().equals("ca"))
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

        template.put("pagina", buildPublicPageInfo(getBaseUrlPublic(), url, getLocale().getLanguage().toString()));
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

        return template;
    }

    private List<Map<String, Object>> getSesionesPlantilla(List<Sesion> sesiones)
    {
        List<Map<String, Object>> sesionesPlantilla = new ArrayList<Map<String, Object>>();

        for (Sesion sesion : sesiones)
        {
            Map<String, Object> datos = new HashMap<String, Object>();

            datos.put("texto", getFechaSesion(sesion));
            
            datos.put("id", sesion.getId());
            datos.put("enPlazoVentaInternet", sesion.getEnPlazoVentaInternet());
            datos.put("canalInternet", sesion.getCanalInternet());
            datos.put("fechaInicioVentaOnline", sesion.getFechaFinVentaOnline());

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
        
        return diaSemana + " " + format.format(cal.getTime());// + " / " + getPrecioSesion(sesion) + " euros";
    }
    
    private String getPrecioSesion(Sesion sesion)
    {
        Map<String, PreciosSesion> preciosSesion = sesionesService.getPreciosSesionPorLocalizacion(sesion.getId());
        
        if (preciosSesion != null && preciosSesion.get("platea1") != null)
        {
            return ReportUtils.formatEuros(preciosSesion.get("platea1").getPrecio());
        }
        else
        {
            return "-";
        }
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

            ImageUtils.changeDpi(evento.getImagen(), bos, 3);

            return Response.ok(bos.toByteArray()).type(evento.getImagenContentType()).build();
        }
        catch (EventoNoEncontradoException e)
        {
            return Response.noContent().build();
        }
    }

}

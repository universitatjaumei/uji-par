package es.uji.apps.par.services.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.EventosService;
import es.uji.apps.par.services.SesionesService;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("evento")
public class EventosResource
{
    @InjectParam
    private EventosService eventosService;

    @InjectParam
    private SesionesService sesionesService;

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public Template getEvento(@PathParam("id") Integer id) throws Exception
    {
        Evento evento = eventosService.getEvento(id);
        List<Sesion> sesiones = sesionesService.getSesiones(id);

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "eventoDetalle");
        template.put("evento", evento);
        template.put("sesiones", sesiones);

        return template;
    }

    @GET
    @Path("{id}/imagen")
    public Response getImagenEvento(@PathParam("id") Integer eventoId)
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
}

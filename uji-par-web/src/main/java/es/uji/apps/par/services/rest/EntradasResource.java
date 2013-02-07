package es.uji.apps.par.services.rest;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.EventosService;
import es.uji.apps.par.services.SesionesService;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("entrada")
public class EntradasResource extends BaseResource
{
    @InjectParam
    private EventosService eventosService;

    @InjectParam
    private SesionesService sesionesService;

    @Context
    HttpServletResponse currentResponse;

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response datosEntrada(@PathParam("id") Integer sesionId) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (fueraDePlazoVenta(sesion))
            return paginaProhibida();

        EventoDTO evento = sesion.getEvento();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntrada", getLocale());
        template.put("evento", evento);
        template.put("sesion", sesion);

        return Response.ok(template).build();
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response compraEntrada(@PathParam("id") Integer sesionId, @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos, @FormParam("telefono") String telefono,
            @FormParam("email") String email, @FormParam("tipo") String tipo) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (fueraDePlazoVenta(sesion))
            return paginaProhibida();

        if (tipo.equals("normal"))
            currentResponse.sendRedirect("compraValida");
        else
            currentResponse.sendRedirect("compraNoValida");

        return null;
    }

    @GET
    @Path("compraValida")
    @Produces(MediaType.TEXT_HTML)
    public Template compraValida() throws Exception
    {
        return new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraValida", getLocale());
    }

    @GET
    @Path("compraNoValida")
    @Produces(MediaType.TEXT_HTML)
    public Template compraNoValida() throws Exception
    {
        return new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraNoValida", getLocale());
    }

    private Response paginaProhibida()
    {
        return Response.status(Status.FORBIDDEN.getStatusCode()).build();
    }

    private boolean fueraDePlazoVenta(Sesion sesion) throws IOException
    {
        Date ahora = new Date();

        return ahora.before(sesion.getFechaInicioVentaOnline()) || ahora.after(sesion.getFechaFinVentaOnline());
    }

}

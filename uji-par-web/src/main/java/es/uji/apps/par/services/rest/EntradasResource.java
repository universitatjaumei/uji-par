package es.uji.apps.par.services.rest;

import java.util.List;

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
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.ButacasService;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.SesionesService;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("entrada")
public class EntradasResource extends BaseResource
{
    @InjectParam
    private SesionesService sesionesService;

    @InjectParam
    private ButacasService butacasService;

    @InjectParam
    private ComprasService comprasService;

    @Context
    HttpServletResponse currentResponse;

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response datosEntrada(@PathParam("id") Long sesionId) throws Exception
    {
        return paginaSeleccionEntradas(sesionId, null, null);
    }

    private Response paginaSeleccionEntradas(long sesionId, List<Butaca> butacasSeleccionadas,
            List<Butaca> butacasOcupadas)
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            return paginaProhibida();

        Evento evento = sesion.getEvento();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntrada", getLocale());
        template.put("evento", evento);
        template.put("sesion", sesion);
        template.put("idioma", getLocale().getLanguage());
        template.put("urlAnfiteatro", String.format("../imagenes/butacas/730/anfiteatro", sesion.getId()));

        if (butacasSeleccionadas != null)
            template.put("butacasSeleccionadas", butacasSeleccionadas);

        List<PreciosSesion> precios = sesionesService.getPreciosSesion(sesion.getId());

        for (PreciosSesion precio : precios)
        {
            template.put("precioNormal_" + precio.getLocalizacion().getCodigo(), precio.getPrecio());
            template.put("precioDescuento_" + precio.getLocalizacion().getCodigo(), precio.getDescuento());
        }

        return Response.ok(template).build();
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response compraEntrada(@PathParam("id") Long sesionId, @FormParam("nombre") String nombre,
            @FormParam("apellidos") String apellidos, @FormParam("telefono") String telefono,
            @FormParam("email") String email, @FormParam("butacasSeleccionadas") String butacasSeleccionadasJSON)
            throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            return paginaProhibida();

        List<Butaca> butacasSeleccionadas = Butaca.parseaJSON(butacasSeleccionadasJSON);
        ResultadoCompra resultadoCompra = comprasService.realizaCompra(sesionId, nombre, apellidos, telefono, email,
                butacasSeleccionadas);

        if (resultadoCompra.getCorrecta())
            currentResponse.sendRedirect("compraValida");
        else
            return paginaSeleccionEntradas(sesionId, butacasSeleccionadas, resultadoCompra.getButacasOcupadas());

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

    @POST
    @Path("{id}/ocupadas")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Butaca> estadoButaca(@PathParam("id") Integer idSesion, List<Butaca> butacas) throws Exception
    {
        return butacasService.estanOcupadas(idSesion, butacas);
    }
}

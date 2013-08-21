package es.uji.apps.par.services.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.Constantes;
import es.uji.apps.par.FueraDePlazoVentaInternetException;
import es.uji.apps.par.butacas.EstadoButacasRequest;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.services.ButacasService;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.SesionesService;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.Utils;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("entrada")
public class EntradasResource extends BaseResource
{
    public static final String BUTACAS_COMPRA = "butacasCompra";
    public static final String UUID_COMPRA = "uuidCompra";

    public static Logger log = Logger.getLogger(EntradasResource.class);

    @InjectParam
    private SesionesService sesionesService;

    @InjectParam
    private ButacasService butacasService;

    @InjectParam
    private ComprasService comprasService;

    @Context
    HttpServletResponse currentResponse;

    @Context
    HttpServletRequest currentRequest;

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response datosEntrada(@PathParam("id") Long sesionId) throws Exception
    {
        return paginaSeleccionEntradas(sesionId, null, null, null);
    }

    private Response paginaSeleccionEntradas(long sesionId, List<Butaca> butacasSeleccionadas,
            List<Butaca> butacasOcupadas, String error)
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            return paginaProhibida();

        Evento evento = sesion.getEvento();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntrada", getLocale());
        template.put("evento", evento);
        template.put("sesion", sesion);
        template.put("idioma", getLocale().getLanguage());
        template.put("baseUrl", getBaseUrl());
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());

        if (error != null && !error.equals(""))
        {
            template.put("error", error);
        }

        String butacasSesion = (String) currentRequest.getSession().getAttribute(BUTACAS_COMPRA);
        if (butacasSesion == null)
        {
            template.put("butacasSesion", "[]");
        }
        else
        {
            template.put("butacasSesion", butacasSesion);
        }

        String uuidCompra = (String) currentRequest.getSession().getAttribute(UUID_COMPRA);
        if (uuidCompra != null)
        {
            template.put("uuidCompra", uuidCompra);
        }

        if (getLocale().getLanguage().equals("ca"))
        {
            template.put("titulo", evento.getTituloVa());
        }
        else
        {
            template.put("titulo", evento.getTituloEs());
        }

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
    public Response compraEntradaHtml(@PathParam("id") Long sesionId,
            @FormParam("butacasSeleccionadas") String butacasSeleccionadasJSON,
            @FormParam("uuidCompra") String uuidCompra) throws Exception
    {
        ResultadoCompra resultadoCompra;
        List<Butaca> butacasSeleccionadas = Butaca.parseaJSON(butacasSeleccionadasJSON);

        try
        {
            resultadoCompra = comprasService.realizaCompraInternet(sesionId, butacasSeleccionadas, uuidCompra);
        }
        catch (FueraDePlazoVentaInternetException e)
        {
            log.error("Fuera de plazo", e);
            return paginaProhibida();
        }
        catch (ButacaOcupadaException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.ocupadas");
            return paginaSeleccionEntradas(sesionId, butacasSeleccionadas, null, error);
        }
        catch (CompraSinButacasException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.noSeleccionadas");
            return paginaSeleccionEntradas(sesionId, butacasSeleccionadas, null, error);
        }

        if (resultadoCompra.getCorrecta())
        {
            currentRequest.getSession().setAttribute(BUTACAS_COMPRA, butacasSeleccionadasJSON);
            currentRequest.getSession().setAttribute(UUID_COMPRA, resultadoCompra.getUuid());

            currentResponse.sendRedirect(resultadoCompra.getUuid() + "/datosComprador");
            return null;
        }
        else
        {
            return paginaSeleccionEntradas(sesionId, butacasSeleccionadas, resultadoCompra.getButacasOcupadas(), null);
        }
    }

    @GET
    @Path("{uuidCompra}/datosComprador")
    @Produces(MediaType.TEXT_HTML)
    public Response rellenaDatosComprador(@PathParam("uuidCompra") String uuidCompra, String nombre, String apellidos,
            String direccion, String poblacion, String cp, String provincia, String telefono, String email,
            String infoPeriodica, String condicionesPrivacidad, String error) throws Exception
    {
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "datosComprador", getLocale());
        template.put("idioma", getLocale().getLanguage());
        template.put("baseUrl", getBaseUrl());

        template.put("uuidCompra", uuidCompra);

        template.put("nombre", nombre);
        template.put("apellidos", apellidos);
        template.put("direccion", direccion);
        template.put("poblacion", poblacion);
        template.put("cp", cp);
        template.put("provincia", provincia);
        template.put("telefono", telefono);
        template.put("email", email);
        template.put("condicionesPrivacidad", condicionesPrivacidad);

        if (infoPeriodica==null || infoPeriodica.equals(""))
            infoPeriodica = "no";
        
        template.put("infoPeriodica", infoPeriodica);
        
        if (error != null && !error.equals(""))
        {
            template.put("error", error);
        }

        return Response.ok(template).build();
    }

    @POST
    @Path("{uuidCompra}/datosComprador")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response guardaDatosComprador(@PathParam("uuidCompra") String uuidCompra,
            @FormParam("nombre") String nombre, @FormParam("apellidos") String apellidos,
            @FormParam("direccion") String direccion, @FormParam("poblacion") String poblacion,
            @FormParam("cp") String cp, @FormParam("provincia") String provincia,
            @FormParam("telefono") String telefono, @FormParam("email") String email,
            @FormParam("infoPeriodica") String infoPeriodica,
            @FormParam("condicionesPrivacidad") String condicionesPrivacidad) throws Exception
    {
        if (nombre == null || nombre.equals(""))
        {
            return rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono,
                    email, infoPeriodica, condicionesPrivacidad,
                    ResourceProperties.getProperty(getLocale(), "error.datosComprador.nombre"));
        }

        if (email == null || email.equals(""))
        {
            return rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono,
                    email, infoPeriodica, condicionesPrivacidad,
                    ResourceProperties.getProperty(getLocale(), "error.datosComprador.email"));
        }

        if (infoPeriodica == null || infoPeriodica.equals(""))
        {
            return rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono,
                    email, infoPeriodica, condicionesPrivacidad,
                    ResourceProperties.getProperty(getLocale(), "error.datosComprador.infoPeriodica"));
        }

        if (condicionesPrivacidad == null || condicionesPrivacidad.equals(""))
        {
            return rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono,
                    email, infoPeriodica, condicionesPrivacidad,
                    ResourceProperties.getProperty(getLocale(), "error.datosComprador.condicionesPrivacidad"));
        }

        comprasService.rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia,
                telefono, email, infoPeriodica);

        CompraDTO compra = comprasService.getCompraByUuid(uuidCompra);

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "tpv", getLocale());
        template.put("idioma", getLocale().getLanguage());
        template.put("baseUrl", getBaseUrl());

        String importe = Utils.monedaToCents(compra.getImporte());
        String url = Configuration.getUrlPublic() + "/rest/tpv/resultado";

        template.put("identificador", compra.getId());
        template.put("concepto", "Entradas Paranimf");
        template.put("importe", importe);
        template.put("correo", email);
        template.put("url", url);
        template.put("hash", Utils.sha1(compra.getId() + importe + email + url + Configuration.getSecret()));

        return Response.ok(template).build();
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
    public List<Butaca> estadoButaca(@PathParam("id") Integer idSesion, EstadoButacasRequest params) throws Exception
    {
        return butacasService.estanOcupadas(idSesion, params.getButacas(), params.getUuidCompra());
    }

    @GET
    @Path("{id}/precios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreciosSesion(@PathParam("id") Long sesionId)
    {
        return Response.ok().entity(new RestResponse(true, sesionesService.getPreciosSesion(sesionId))).build();
    }

    @GET
    @Path("butacasFragment/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Template butacasFragment(@PathParam("id") long sesionId) throws Exception
    {
        HTMLTemplate template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "butacasFragment", getLocale());

        Sesion sesion = sesionesService.getSesion(sesionId);

        template.put("baseUrl", getBaseUrl());
        template.put("idioma", getLocale().getLanguage());
        template.put("sesion", sesion);
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());

        if (getLocale().getLanguage().equals("ca"))
        {
            template.put("titulo", sesion.getEvento().getTituloVa());
        }
        else
        {
            template.put("titulo", sesion.getEvento().getTituloEs());
        }

        template.put("butacasSesion", "[]");

        return template;
    }

}

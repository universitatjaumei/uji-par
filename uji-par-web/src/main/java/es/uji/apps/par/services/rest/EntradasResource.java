package es.uji.apps.par.services.rest;

import java.math.BigDecimal;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
import es.uji.apps.par.model.Compra;
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
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (sesion.getEvento().getAsientosNumerados().equals(BigDecimal.ONE))
        {
            return paginaSeleccionEntradasNumeradas(sesionId, null, null, null);
        }
        else
        {
            return paginaSeleccionEntradasNoNumeradas(sesionId, null, null, null, null, null);
        }
    }

    private Response paginaSeleccionEntradasNumeradas(long sesionId, List<Butaca> butacasSeleccionadas,
            List<Butaca> butacasOcupadas, String error)
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            return paginaProhibida();

        Evento evento = sesion.getEvento();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntrada", getLocale(), APP);
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
            template.put("tipoEvento", evento.getParTiposEvento().getNombreVa());
        }
        else
        {
            template.put("titulo", evento.getTituloEs());
            template.put("tipoEvento", evento.getParTiposEvento().getNombreEs());
        }

        if (butacasSeleccionadas != null)
            template.put("butacasSeleccionadas", butacasSeleccionadas);

        List<PreciosSesion> precios = sesionesService.getPreciosSesion(sesion.getId());

        for (PreciosSesion precio : precios)
        {
            template.put("precioNormal_" + precio.getLocalizacion().getCodigo(), precio.getPrecio());
            template.put("precioDescuento_" + precio.getLocalizacion().getCodigo(), precio.getDescuento());
        }

        template.put("gastosGestion", Float.parseFloat(Configuration.getGastosGestion()));
        
        ResponseBuilder builder = Response.ok(template);

        return Utils.noCache(builder).build();
    }

    private Response paginaSeleccionEntradasNoNumeradas(long sesionId, String platea1Normal, String platea1Descuento,
            String platea2Normal, String platea2Descuento, String error)
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            return paginaProhibida();

        Evento evento = sesion.getEvento();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntradaNoNumerada", getLocale(), APP);
        template.put("evento", evento);
        template.put("sesion", sesion);
        template.put("idioma", getLocale().getLanguage());
        template.put("baseUrl", getBaseUrl());
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());

        if (platea1Normal == null || platea1Normal.equals(""))
            template.put("platea1Normal", "0");
        else
            template.put("platea2Normal", platea1Normal);

        if (platea1Descuento == null || platea1Descuento.equals(""))
            template.put("platea1Descuento", "0");
        else
            template.put("platea1Descuento", platea1Descuento);

        if (platea2Normal == null || platea2Normal.equals(""))
            template.put("platea2Normal", "0");
        else
            template.put("platea2Normal", platea2Normal);

        if (platea2Descuento == null || platea2Descuento.equals(""))
            template.put("platea2Descuento", "0");
        else
            template.put("platea2Descuento", platea2Descuento);

        if (error != null && !error.equals(""))
        {
            template.put("error", error);
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

        List<PreciosSesion> precios = sesionesService.getPreciosSesion(sesion.getId());

        for (PreciosSesion precio : precios)
        {
            template.put("precioNormal_" + precio.getLocalizacion().getCodigo(), precio.getPrecio());
            template.put("precioDescuento_" + precio.getLocalizacion().getCodigo(), precio.getDescuento());
        }

        template.put("gastosGestion", Float.parseFloat(Configuration.getGastosGestion()));

        ResponseBuilder builder = Response.ok(template);

        return Utils.noCache(builder).build();
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response compraEntradaHtml(@PathParam("id") Long sesionId,
            @FormParam("butacasSeleccionadas") String butacasSeleccionadasJSON,
            @FormParam("platea1Normal") String platea1Normal, @FormParam("platea1Descuento") String platea1Descuento,
            @FormParam("platea2Normal") String platea2Normal, @FormParam("platea2Descuento") String platea2Descuento,
            @FormParam("uuidCompra") String uuidCompra) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (sesion.getEvento().getAsientosNumerados().equals(BigDecimal.ONE))
        {
            return compraEntradaNumeradaHtml(sesionId, butacasSeleccionadasJSON, uuidCompra);
        }
        else
        {
            return compraEntradaNoNumeradaHtml(sesionId, platea1Normal, platea1Descuento, platea2Normal, platea2Descuento, uuidCompra);
        }
    }

    public Response compraEntradaNumeradaHtml(Long sesionId, String butacasSeleccionadasJSON, String uuidCompra)
            throws Exception
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
            return paginaSeleccionEntradasNumeradas(sesionId, butacasSeleccionadas, null, error);
        }
        catch (CompraSinButacasException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.noSeleccionadas");
            return paginaSeleccionEntradasNumeradas(sesionId, butacasSeleccionadas, null, error);
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
            return paginaSeleccionEntradasNumeradas(sesionId, butacasSeleccionadas,
                    resultadoCompra.getButacasOcupadas(), null);
        }
    }

    public Response compraEntradaNoNumeradaHtml(Long sesionId, String platea1NormalSt, String platea1DescuentoSt,
            String platea2NormalSt, String platea2DescuentoSt, String uuidCompra) throws Exception
    {
        ResultadoCompra resultadoCompra;
        int platea1Normal = 0, platea1Descuento = 0, platea2Normal = 0, platea2Descuento = 0;

        try
        {
            platea1Normal = Integer.parseInt(platea1NormalSt);
            platea1Descuento = Integer.parseInt(platea1DescuentoSt);
            platea2Normal = Integer.parseInt(platea2NormalSt);
            platea2Descuento = Integer.parseInt(platea2DescuentoSt);
        }
        catch (NumberFormatException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.noNumeros");
            return paginaSeleccionEntradasNoNumeradas(sesionId, platea1NormalSt, platea1DescuentoSt, platea2NormalSt, platea2DescuentoSt, error);
        }
        
        try
        {
            resultadoCompra = comprasService.realizaCompraInternet(sesionId, platea1Normal, platea1Descuento, platea2Normal, platea2Descuento, uuidCompra);
        }
        catch (FueraDePlazoVentaInternetException e)
        {
            log.error("Fuera de plazo", e);
            return paginaProhibida();
        }
        catch (ButacaOcupadaException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.ocupadas");
            return paginaSeleccionEntradasNoNumeradas(sesionId, platea1NormalSt, platea1DescuentoSt, platea2NormalSt, platea2DescuentoSt, error);
        }
        catch (CompraSinButacasException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.noSeleccionadas");
            return paginaSeleccionEntradasNoNumeradas(sesionId, platea1NormalSt, platea1DescuentoSt, platea2NormalSt, platea2DescuentoSt, error);
        }

        if (resultadoCompra.getCorrecta())
        {
            currentRequest.getSession().setAttribute(UUID_COMPRA, resultadoCompra.getUuid());

            currentResponse.sendRedirect(resultadoCompra.getUuid() + "/datosComprador");
            return null;
        }
        else
        {
            return paginaSeleccionEntradasNoNumeradas(sesionId, platea1NormalSt, platea1DescuentoSt, platea2NormalSt, platea2DescuentoSt, "");
        }
    }

    @GET
    @Path("{uuidCompra}/datosComprador")
    @Produces(MediaType.TEXT_HTML)
    public Response rellenaDatosComprador(@PathParam("uuidCompra") String uuidCompra, String nombre, String apellidos,
            String direccion, String poblacion, String cp, String provincia, String telefono, String email,
            String infoPeriodica, String condicionesPrivacidad, String error) throws Exception
    {
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "datosComprador", getLocale(), APP);
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
        template.put("urlCondicionesPrivacidad", Configuration.getUrlCondicionesPrivacidad());
        
        CompraDTO compra = comprasService.getCompraByUuid(uuidCompra);
        
        if (getLocale().getLanguage().equals("ca"))
        	template.put("tipoEvento", compra.getParSesion().getParEvento().getParTiposEvento().getNombreVa());
        else
        	template.put("tipoEvento", compra.getParSesion().getParEvento().getParTiposEvento().getNombreEs());
        
        if (infoPeriodica == null || infoPeriodica.equals(""))
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

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "tpv", getLocale(), APP);
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
        return Response.ok().entity(new RestResponse(true, sesionesService.getPreciosSesion(sesionId), 
        		sesionesService.getTotalPreciosSesion(sesionId))).build();
    }

    @GET
    @Path("butacasFragment/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response butacasFragment(@PathParam("id") long sesionId, @QueryParam("reserva") String reserva) throws Exception
    {
        HTMLTemplate template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "butacasFragment", getLocale(), APP);

        Sesion sesion = sesionesService.getSesion(sesionId);

        template.put("baseUrl", getBaseUrl());
        template.put("idioma", getLocale().getLanguage());
        template.put("sesion", sesion);
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());
        template.put("ocultaComprar", "true");
        template.put("gastosGestion", 0.0);
        template.put("modoReserva", reserva!=null && reserva.equals("true"));

        if (getLocale().getLanguage().equals("ca"))
        {
            template.put("titulo", sesion.getEvento().getTituloVa());
        }
        else
        {
            template.put("titulo", sesion.getEvento().getTituloEs());
        }

        template.put("butacasSesion", "[]");

        return Response.ok().entity(template).header("Content-Type", "text/html; charset=utf-8").build();
    }

}

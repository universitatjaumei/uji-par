package es.uji.apps.par.services.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraButacaDescuentoNoDisponible;
import es.uji.apps.par.CompraInvitacionPorInternetException;
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
import es.uji.apps.par.model.Tarifa;
import es.uji.apps.par.services.ButacasService;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.EntradasService;
import es.uji.apps.par.services.SesionesService;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.Utils;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("entrada")
public class EntradasResource extends BaseResource
{
    private static final String TPV_ORDER_PREFIX_CODE_CAJAMAR = "0000";
	private static final String TPV_LANG_ES_CODE = "001";
	private static final String TPV_LANG_CA_CODE = "003";

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
    
    @Context
    private HttpServletRequest request;

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response datosEntrada(@PathParam("id") Long sesionId) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (Utils.isAsientosNumerados(sesion.getEvento()))
        {
            return paginaSeleccionEntradasNumeradas(sesionId, null, null, null);
        }
        else
        {
            return paginaSeleccionEntradasNoNumeradas(sesionId, null);
        }
    }

    private Response paginaSeleccionEntradasNumeradas(long sesionId, List<Butaca> butacasSeleccionadas,
    		List<Butaca> butacasOcupadas, String error) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);
        String urlBase = getBaseUrlPublic();
        String url = request.getRequestURL().toString();

        if (!sesion.getEnPlazoVentaInternet())
            return paginaFueraDePlazo();

        Evento evento = sesion.getEvento();

        Locale locale = getLocale();
        String language = locale.getLanguage();
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntrada", locale, APP);
        template.put("evento", evento);
        template.put("sesion", sesion);
        template.put("idioma", language);
        template.put("baseUrl", getBaseUrlPublic());
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());
        template.put("pagina", buildPublicPageInfo(urlBase, url, language.toString()));
        template.put("tipoEventoEs", sesion.getEvento().getParTiposEvento().getNombreEs());
        //template.put("tarifas", sesionesService)

        if (error != null && !error.equals(""))
        {
            template.put("error", error);
        }

        String butacasSesion = (String) currentRequest.getSession().getAttribute(EntradasService.BUTACAS_COMPRA);
        if (butacasSesion == null)
        {
            template.put("butacasSesion", "[]");
        }
        else
        {
            template.put("butacasSesion", butacasSesion);
        }

        String uuidCompra = (String) currentRequest.getSession().getAttribute(EntradasService.UUID_COMPRA);
        if (uuidCompra != null)
        {
            template.put("uuidCompra", uuidCompra);
        }

        if (language.equals("ca"))
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
        template.put("lang", language);
        
        ResponseBuilder builder = Response.ok(template);

        return Utils.noCache(builder).build();
    }

    private Response paginaSeleccionEntradasNoNumeradas(long sesionId, String error) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);
        String urlBase = getBaseUrlPublic();

        if (!sesion.getEnPlazoVentaInternet())
            return paginaFueraDePlazo();

        Evento evento = sesion.getEvento();

        Locale locale = getLocale();
        String language = locale.getLanguage();
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "seleccionEntradaNoNumerada", locale, APP);
        template.put("evento", evento);
        template.put("sesion", sesion);
        template.put("idioma", language);
        template.put("baseUrl", getBaseUrlPublic());
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());
        template.put("pagina", buildPublicPageInfo(urlBase, urlBase, language.toString()));
        Calendar cal = Calendar.getInstance();
        template.put("millis", cal.getTime().getTime());
        List<Tarifa> tarifas = new ArrayList<Tarifa>();
        
        if (sesion.getPlantillaPrecios() != null && sesion.getPlantillaPrecios().getId() != -1)
        	tarifas = sesionesService.getTarifasPublicasConPrecioConPlantilla(sesionId);
        else
        	tarifas = sesionesService.getTarifasPublicasConPrecioSinPlantilla(sesionId);
        //List<PreciosSesion> preciosSesion = sesionesService.getPreciosSesion(sesionId);
        	
        template.put("tarifas", tarifas);
        //template.put("preciosSesion", preciosSesion);

        if (error != null && !error.equals(""))
        {
            template.put("error", error);
        }

        String uuidCompra = (String) currentRequest.getSession().getAttribute(EntradasService.UUID_COMPRA);
        if (uuidCompra != null)
        {
            template.put("uuidCompra", uuidCompra);
        }

        if (language.equals("ca"))
        {
            template.put("titulo", evento.getTituloVa());
        }
        else
        {
            template.put("titulo", evento.getTituloEs());
        }

        List<PreciosSesion> preciosSesion = sesionesService.getPreciosSesionPublicos(sesion.getId());
        template.put("preciosSesion", preciosSesion);

        for (PreciosSesion precio : preciosSesion)
        {
            String codigoLocalizacion = precio.getLocalizacion().getCodigo();
            
            //template.put("precioNormal_" + codigoLocalizacion, precio.getPrecio());
            //template.put("precioDescuento_" + codigoLocalizacion, precio.getDescuento());
            
            // Hay algunos casos en los que no se permite descuento
            template.put("descuentoNoDisponible_" + codigoLocalizacion, comprasService.esButacaDescuentoNoDisponible("descuento", evento, precio));
        }
        
        Map<String, Map<Long, PreciosSesion>> preciosSesionLocalizacion = sesionesService.getPreciosSesionPublicosPorLocalizacion(sesion.getId());
        template.put("preciosSesionLocalizacion", preciosSesionLocalizacion);

        template.put("gastosGestion", Float.parseFloat(Configuration.getGastosGestion()));
        template.put("lang", language);
        
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
            @FormParam("uuidCompra") String uuidCompra, @FormParam("b_t") String strButacas) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (Utils.isAsientosNumerados(sesion.getEvento()))
        {
            return compraEntradaNumeradaHtml(sesionId, butacasSeleccionadasJSON, uuidCompra);
        }
        else
        {
            return compraEntradaNoNumeradaHtml(sesionId, strButacas, uuidCompra);
        }
    }

    private Response compraEntradaNumeradaHtml(Long sesionId, String butacasSeleccionadasJSON, String uuidCompra)
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
            return paginaFueraDePlazo();
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
        catch (CompraInvitacionPorInternetException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.invitacionPorInternet");
            return paginaSeleccionEntradasNumeradas(sesionId, butacasSeleccionadas, null, error);
        }
        catch (CompraButacaDescuentoNoDisponible e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.compraDescuentoNoDisponible");
            return paginaSeleccionEntradasNumeradas(sesionId, butacasSeleccionadas, null, error);
        }

        if (resultadoCompra.getCorrecta())
        {
            currentRequest.getSession().setAttribute(EntradasService.BUTACAS_COMPRA, butacasSeleccionadasJSON);
            currentRequest.getSession().setAttribute(EntradasService.UUID_COMPRA, resultadoCompra.getUuid());

            currentResponse.sendRedirect(resultadoCompra.getUuid() + "/datosComprador");
            return null;
        }
        else
        {
            return paginaSeleccionEntradasNumeradas(sesionId, butacasSeleccionadas,
                    resultadoCompra.getButacasOcupadas(), null);
        }
    }

    private Response compraEntradaNoNumeradaHtml(Long sesionId, String butacasSeleccionadasJSON, String uuidCompra) throws Exception
    {
        ResultadoCompra resultadoCompra;
        List<Butaca> butacasSeleccionadas = Butaca.parseaJSON("[" + butacasSeleccionadasJSON + "]");
        
        try
        {
            resultadoCompra = comprasService.realizaCompraInternet(sesionId, butacasSeleccionadas, uuidCompra);
        }
        catch (FueraDePlazoVentaInternetException e)
        {
            log.error("Fuera de plazo", e);
            return paginaFueraDePlazo();
        }
        catch (ButacaOcupadaException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.ocupadas");
            return paginaSeleccionEntradasNoNumeradas(sesionId, error);
        }
        catch (CompraSinButacasException e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.noSeleccionadas");
            return paginaSeleccionEntradasNoNumeradas(sesionId, error);
        }
        catch (CompraButacaDescuentoNoDisponible e)
        {
            String error = ResourceProperties.getProperty(getLocale(), "error.seleccionEntradas.compraDescuentoNoDisponible");
            return paginaSeleccionEntradasNoNumeradas(sesionId, error);
        } catch (Exception e) {
        	String error = ResourceProperties.getProperty(getLocale(), "error.errorGeneral");
        	return paginaSeleccionEntradasNoNumeradas(sesionId, error);
        }

        if (resultadoCompra.getCorrecta())
        {
            currentRequest.getSession().setAttribute(EntradasService.UUID_COMPRA, resultadoCompra.getUuid());

            currentResponse.sendRedirect(resultadoCompra.getUuid() + "/datosComprador");
            return null;
        }
        else
        {
            return paginaSeleccionEntradasNoNumeradas(sesionId, "");
        }
    }

    @GET
    @Path("{uuidCompra}/datosComprador")
    @Produces(MediaType.TEXT_HTML)
    public Response rellenaDatosComprador(@PathParam("uuidCompra") String uuidCompra, String nombre, String apellidos,
            String direccion, String poblacion, String cp, String provincia, String telefono, String email,
            String infoPeriodica, String condicionesPrivacidad, String error) throws Exception
    {
        Locale locale = getLocale();
        String language = locale.getLanguage();
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "datosComprador", locale, APP);
        String urlBase = getBaseUrlPublic();
        String url = request.getRequestURL().toString();
        template.put("pagina", buildPublicPageInfo(urlBase, url, language.toString()));
        template.put("baseUrl", getBaseUrlPublic());
        
        template.put("idioma", language);
        template.put("lang", language);

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
        
        if (compra != null) {
	        if (language.equals("ca"))
	        	template.put("tipoEvento", compra.getParSesion().getParEvento().getParTiposEvento().getNombreVa());
	        else
	        	template.put("tipoEvento", compra.getParSesion().getParEvento().getParTiposEvento().getNombreEs());
	        
	        if (infoPeriodica == null || infoPeriodica.equals(""))
	            infoPeriodica = "no";
	
	        template.put("infoPeriodica", infoPeriodica);
        } else
        	error = ResourceProperties.getProperty(locale, "error.compraCaducada");;

        if (error != null && !error.equals(""))
        {
            template.put("error", error);
        }

        return Response.ok(template).build();
    }

    //TODO -> Verificar que funciona con el tpv de la uji
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
        
        if (apellidos == null || apellidos.equals(""))
        {
            return rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono,
                    email, infoPeriodica, condicionesPrivacidad,
                    ResourceProperties.getProperty(getLocale(), "error.datosComprador.apellidos"));
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
        
        if (compra.getCaducada())
        {
            return rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono,
                    email, infoPeriodica, condicionesPrivacidad,
                    ResourceProperties.getProperty(getLocale(), "error.datosComprador.compraCaducada"));
        }

        Locale locale = getLocale();
        String language = locale.getLanguage();
        
        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "tpv", locale, APP);
        
        template.put("idioma", language);
        template.put("lang", language);
        template.put("baseUrl", getBaseUrlPublic());

        String secret = Configuration.getSecret();
        
        String importe = Utils.monedaToCents(compra.getImporte());
        String url = Configuration.getWSDLURL();
        String urlOk = getBaseUrlPublic() + "/rest/tpv/ok";
        String urlKo = getBaseUrlPublic() + "/rest/tpv/ko";

        template.put("identificador", compra.getId());
        String concepto = StringUtils.stripAccents(compra.getParSesion().getParEvento().getTituloVa().toUpperCase());
        template.put("concepto", concepto);
        template.put("importe", importe);
        template.put("correo", email);
        template.put("url", url);
        template.put("hash", Utils.sha1(compra.getId() + importe + email + url + secret));
        
        if (language.equals("ca"))
        	template.put("langCode", TPV_LANG_CA_CODE);
        else
        	template.put("langCode", TPV_LANG_ES_CODE);

        String tpvCode = Configuration.getTpvCode();
        String tpvCurrency = Configuration.getTpvCurrency();
        String tpvTransaction = Configuration.getTpvTransaction();
        String tpvTerminal = Configuration.getTpvTerminal();
        String tpvNombre = Configuration.getTpvNombre();
        
        template.put("order", TPV_ORDER_PREFIX_CODE_CAJAMAR + compra.getId());
        if (tpvCode != null && tpvCurrency != null && tpvTransaction != null && tpvTerminal != null && tpvNombre != null)
        {
	        template.put("currency", tpvCurrency);
	        template.put("code", tpvCode);
	        template.put("terminal", tpvTerminal);
	        template.put("transaction", tpvTransaction);
	        template.put("nombre", tpvNombre);
	        String shaEnvio = Utils.sha1(importe + TPV_ORDER_PREFIX_CODE_CAJAMAR + compra.getId() + tpvCode + tpvCurrency + tpvTransaction + url + secret);
	        template.put("hashcajamar", shaEnvio);
	        log.info("Sha1 para envio generado " + shaEnvio);
        }
        template.put("urlOk", urlOk);
        template.put("urlKo", urlKo);

        return Response.ok(template).build();
    }

    private Response paginaFueraDePlazo() throws Exception
    {
    	Locale locale = getLocale();
        String language = locale.getLanguage();
        
    	Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraFinalizada", locale, APP);
    	String urlBase = getBaseUrlPublic();
        String url = request.getRequestURL().toString();
        template.put("pagina", buildPublicPageInfo(urlBase, url, language.toString()));
        template.put("baseUrl", getBaseUrlPublic());
        
        template.put("idioma", language);
        template.put("lang", language);
    	
        return Response.ok(template).build();
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
    public Response butacasFragment(@PathParam("id") long sesionId, @QueryParam("reserva") String reserva, 
    		@QueryParam("if") String isAdmin) throws Exception
    {
    	Locale locale = getLocale();
        String language = locale.getLanguage();
        HTMLTemplate template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "butacasFragment", locale, APP);

        Sesion sesion = sesionesService.getSesion(sesionId);

        template.put("baseUrl", getBaseUrlPublic());
        template.put("idioma", language);
        template.put("lang", language);
        template.put("sesion", sesion);
        template.put("fecha", DateUtils.dateToSpanishString(sesion.getFechaCelebracion()));
        template.put("hora", sesion.getHoraCelebracion());
        template.put("ocultaComprar", "true");
        template.put("gastosGestion", 0.0);
        template.put("modoReserva", reserva!=null && reserva.equals("true"));
        template.put("estilopublico", "false");
        template.put("muestraReservadas", true);
        template.put("modoAdmin", true);
        template.put("tipoEventoEs", sesion.getEvento().getParTiposEvento().getNombreEs());
        Calendar cal = Calendar.getInstance();
        template.put("millis", cal.getTime().getTime());
        List<Tarifa> tarifas = new ArrayList<Tarifa>();
        
        if (sesion.getPlantillaPrecios() != null && sesion.getPlantillaPrecios().getId() != -1)
        	tarifas = sesionesService.getTarifasConPrecioConPlantilla(sesionId);
        else
        	tarifas = sesionesService.getTarifasConPrecioSinPlantilla(sesionId);
        	
        template.put("tarifas", tarifas);

        if (language.equals("ca"))
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

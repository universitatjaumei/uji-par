package es.uji.apps.par.services.rest;

import com.sun.jersey.api.core.InjectParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.apps.par.builders.PublicPageBuilderInterface;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.MailService;
import es.uji.apps.par.services.TpvComprasService;
import es.uji.commons.web.template.Template;

@Component
@Path("tpv")
public class TpvResource extends BaseResource
{
	private static final Logger log = LoggerFactory.getLogger(TpvResource.class);

    @InjectParam
    private ComprasService compras;

    @InjectParam
    private MailService mailService;

    @Context
    HttpServletResponse currentResponse;

    @Context
    HttpServletRequest currentRequest;
    
    @Context
    private HttpServletRequest request;

	@InjectParam
	private PublicPageBuilderInterface publicPageBuilderInterface;

	@InjectParam
    TpvComprasService tpvComprasService;

    @POST
    @Path("resultado")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response leeResultadoTpv(@FormParam("tpv_estado") String estado, @FormParam("tpv_recibo") String recibo,
            @FormParam("tpv_identificador") String identificador, @FormParam("tpv_importe") String importe,
            @FormParam("tpv_firma") String firma) throws Exception
    {
        String msg = String.format(
                "Resultado pago TPV: tpv_estado=%s, tpv_recibo=%s, tpv_identificador=%s, tpv_importe=%s, tpv_firma=%s",
                estado, recibo, identificador, importe, firma);
        log.info(msg);

        CompraDTO compra = compras.getCompraById(Long.parseLong(identificador));

        String url = currentRequest.getRequestURL().toString();
        Locale locale = getLocale();
        Template template;
        if (compra.getCaducada())
        {
            compras.eliminaCompraDeSesion(currentRequest);
            template = tpvComprasService.compraCaducada(compra, recibo, url, locale);
        }
        else if (estado != null && estado.equals("OK"))
        {
            compras.eliminaCompraDeSesion(currentRequest);
            template = tpvComprasService.compraOk(compra, recibo, url, locale);
        }
        else
        {
            template = tpvComprasService.paginaError(compra, url, locale);
        }

        return Response.ok(template).build();
    }
}

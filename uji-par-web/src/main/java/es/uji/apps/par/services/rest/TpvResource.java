package es.uji.apps.par.services.rest;

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

import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.Constantes;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.MailService;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Path("tpv")
public class TpvResource extends BaseResource
{
    public static Logger log = Logger.getLogger(TpvResource.class);

    @InjectParam
    private ComprasService compras;

    @InjectParam
    private MailService mailService;

    @Context
    HttpServletResponse currentResponse;

    @Context
    HttpServletRequest currentRequest;

    @POST
    @Path("resultado")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response leeResultadoTpv(@FormParam("tpv_estado") String estado, @FormParam("tpv_recibo") String recibo,
            @FormParam("tpv_identificador") String identificador, @FormParam("tpv_importe") String importe,
            @FormParam("tpv_firma") String firma)
    {
        Template template;

        String msg = String.format(
                "Resultado pago TPV: tpv_estado=%s, tpv_recibo=%s, tpv_identificador=%s, tpv_importe=%s, tpv_firma=%s",
                estado, recibo, identificador, importe, firma);
        log.info(msg);

        if (estado != null && estado.equals("OK"))
        {
            CompraDTO compra = compras.getCompraById(Long.parseLong(identificador));

            compras.marcaPagadaPasarela(compra.getId(), recibo);
            enviaMail(compra.getEmail(), compra.getUuid());

            currentRequest.getSession().removeAttribute(EntradasResource.BUTACAS_COMPRA);
            currentRequest.getSession().removeAttribute(EntradasResource.UUID_COMPRA);

            template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraValida", getLocale());
            template.put("url", Configuration.getUrlPublic() + "/rest/compra/" + compra.getUuid() + "/pdf");
        }
        else
        {
            template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraIncorrecta", getLocale());
        }

        return Response.ok(template).build();
    }

    private void enviaMail(String email, String uuid)
    {
        String urlEntradas = String.format("%s/rest/compra/%s/pdf", getBaseUrl(), uuid);
        
        String titulo = ResourceProperties.getProperty(new Locale("es"), "mail.entradas.titulo");
        String texto = ResourceProperties.getProperty(new Locale("es"), "mail.entradas.texto", urlEntradas);

        mailService.anyadeEnvio(email, titulo, texto);
    }
}

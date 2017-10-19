package es.uji.apps.par.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

import es.uji.apps.par.builders.PublicPageBuilderInterface;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.config.ConfigurationSelector;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.exceptions.Constantes;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.services.rest.BaseResource;
import es.uji.apps.par.tpv.TpvInterface;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

@Service
public class TpvComprasService implements TpvInterface {
    private static final Logger log = LoggerFactory.getLogger(TpvComprasService.class);

    @Autowired
    private ComprasService compras;

    @Autowired
    private JavaMailService mailService;

    @Autowired
    private PublicPageBuilderInterface publicPageBuilderInterface;

    @Autowired
    Configuration configuration;

    @Autowired
    ConfigurationSelector configurationSelector;

    public Template compraOk(
        CompraDTO compraDTO,
        String recibo,
        String url,
        Locale locale
    ) throws Exception {
        Template template;
        compras.marcaPagadaPasarela(compraDTO.getId(), recibo);
        enviaMail(compraDTO.getEmail(), compraDTO.getUuid(), locale);
        template = paginaExito(compraDTO, recibo, url, locale);
        return template;
    }

    public Template compraCaducada(
        CompraDTO compraDTO,
        String recibo,
        String url,
        Locale locale
    ) throws Exception {
        Template template;
        compras.rellenaCodigoPagoPasarela(compraDTO.getId(), recibo);
        template = paginaError(compraDTO, url, locale);
        template.put("descripcionError", ResourceProperties
            .getProperty(locale, "error.datosComprador.compraCaducadaTrasPagar"));
        return template;
    }

    private Template checkCompra(
        CompraDTO compraDTO,
        String recibo,
        String estado,
        String url,
        Locale locale
    ) throws Exception {
        Template template;

        if (compraDTO.getCaducada())
        {
            template = compraCaducada(compraDTO, recibo, url, locale);
        }
        else if (estado != null && estado.equals("OK"))
        {
            template = compraOk(compraDTO, recibo, url, locale);
        }
        else
        {
            template = paginaError(compraDTO, url, locale);
        }
        return template;
    }

    private Template paginaExito(CompraDTO compra, String recibo, String url, Locale locale) throws Exception
    {
        String language = locale.getLanguage();

        Template template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraValida", locale, BaseResource.APP);

        template.put("pagina", publicPageBuilderInterface.buildPublicPageInfo(configurationSelector.getUrlPublic(), url,
            language.toString(), configurationSelector.getHtmlTitle()));
        template.put("baseUrl", configurationSelector.getUrlPublic());

        template.put("referencia", recibo);
        template.put("email", compra.getEmail());
        template.put("url", configurationSelector.getUrlPublic() + "/rest/compra/" + compra.getUuid() + "/pdf");
        template.put("urlComoLlegar", configurationSelector.getUrlComoLlegar());
        template.put("lang", language);

        return template;
    }

    public HTMLTemplate paginaError(
        CompraDTO compra,
        String url,
        Locale locale
    ) throws Exception
    {
        String language = locale.getLanguage();

        HTMLTemplate template = new HTMLTemplate(Constantes.PLANTILLAS_DIR + "compraIncorrecta", locale, BaseResource.APP);

        template.put("pagina", publicPageBuilderInterface.buildPublicPageInfo(configurationSelector.getUrlPublic(), url, language.toString(), configurationSelector.getHtmlTitle()));
        template.put("baseUrl", configurationSelector.getUrlPublic());

        template.put("urlReintentar", configurationSelector.getUrlPublic() + "/rest/entrada/" + compra.getParSesion().getId());
        template.put("lang", language);

        return template;
    }

    private void enviaMail(String email, String uuid, Locale locale)
    {
        String urlEntradas = String.format("%s/rest/compra/%s/pdf", configurationSelector.getUrlPublic(), uuid);

        String titulo = ResourceProperties.getProperty(locale, "mail.entradas.titulo");
        String texto = ResourceProperties.getProperty(locale, "mail.entradas.texto", urlEntradas);

        mailService.anyadeEnvio(configurationSelector.getMailFrom(), email, titulo, texto, uuid, configurationSelector.getUrlPublic(), configurationSelector.getUrlPieEntrada());
    }

    @Override
    public Template testTPV(long identificadorCompra, String url, Locale locale) throws Exception {
        CompraDTO compra = compras.getCompraById(identificadorCompra);
        return checkCompra(compra, "RECIBO_TEST", "OK", url, locale);
    }

    @Override
    public Template compraGratuita(long identificadorCompra, String url, Locale locale) throws Exception {
        CompraDTO compra = compras.getCompraById(identificadorCompra);
        return checkCompra(compra, "COMPRA_GRATUITA_" + identificadorCompra, "OK", url, locale);
    }
}

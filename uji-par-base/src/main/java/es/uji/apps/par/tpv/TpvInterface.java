package es.uji.apps.par.tpv;

import java.util.Locale;

import es.uji.commons.web.template.Template;

public interface TpvInterface {

	Template testTPV(long identificadorCompra, String url, Locale locale) throws Exception;

	Template compraGratuita(long identificadorCompra, String url, Locale locale) throws Exception;
}


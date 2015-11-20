package es.uji.apps.par.tpv;


import com.mysema.commons.lang.Pair;

public interface HmacSha256TPVInterface {
    public Pair<String, String> getParametrosYFirma(String importe, String order, String tpvCode, String tpvCurrency, String tpvTransaction, String tpvTerminal, String url, String urlOk, String urlKo, String email, String tpvLang, String identificador, String concepto, String tpvNombre, String secret) throws Exception;
}

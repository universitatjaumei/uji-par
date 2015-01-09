package es.uji.apps.par.tpv;

public interface SHA1TPVInterface {
    public String getFirma(String importe, String orderPrefix, String id, String tpvCode, String tpvCurrency, String tpvTransaction, String url, String secret, String date);
}

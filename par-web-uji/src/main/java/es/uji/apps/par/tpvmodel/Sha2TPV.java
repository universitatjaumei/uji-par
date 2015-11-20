package es.uji.apps.par.tpvmodel;

import com.mysema.commons.lang.Pair;
import es.uji.apps.par.tpv.HmacSha256TPVInterface;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class Sha2TPV implements HmacSha256TPVInterface {

    @Override
    public Pair<String, String> getParametrosYFirma(String importe, String order, String tpvCode, String tpvCurrency, String tpvTransaction, String tpvTerminal, String url, String urlOk, String urlKo, String email, String tpvLang, String identificador, String concepto, String tpvNombre, String secret) throws Exception {
        throw new NotImplementedException("Firma sha256 no implementada");
    }
}

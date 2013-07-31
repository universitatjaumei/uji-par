package es.uji.apps.par.services;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class PinpadWebService implements PinpadDataService
{
    private static Logger log = Logger.getLogger(PinpadWebService.class);
    
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 30000;

    private static final String SECRET = "ertyudfghjcvbnm";

    @Override
    public String consultaEstado(String id)
    {
        Client client = Client.create();

        String sha1String = sha1(id + SECRET);

        String url = String.format("https://e-ujier.uji.es/pls/www/!pinpad.estado?tpv_identificador=%s&tpv_hash=%s",
                id, sha1String);

        WebResource webResource = client.resource(url);

        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Error respuesta HTTP : " + response.getStatus());
        }

        String output = response.getEntity(String.class);
        return output;
    }
    
    @Override
    public String realizaPago(String id, BigDecimal importe, String concepto)
    {
        Client client = Client.create();

        client.setConnectTimeout(CONNECT_TIMEOUT);
        client.setReadTimeout(READ_TIMEOUT);
        
        BigDecimal importeCentimos = importe.multiply(new BigDecimal(100));
        String importeEnviar = Integer.toString(importeCentimos.intValue());
        
        System.out.println(importeEnviar);

        String sha1String = sha1(id + importeEnviar + SECRET);

        String conceptoEncoded;
        try
        {
            conceptoEncoded = URLEncoder.encode(concepto, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Error en URLEncoder.encode: " + concepto);
        }
        
        String url = String.format("https://e-ujier.uji.es/pls/www/!pinpad.cobrar?tpv_identificador=%s&tpv_concepto=%s&tpv_importe=%s&tpv_hash=%s",
                id, conceptoEncoded, importeEnviar, sha1String);

        log.info("URL pagar: " + url);
        
        WebResource webResource = client.resource(url);

        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Error respuesta HTTP: " + response.getStatus());
        }

        String output = response.getEntity(String.class);
        
        return output;
    }

    private String sha1(String string)
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            return byteArrayToHexString(md.digest(string.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String byteArrayToHexString(byte[] b)
    {
        String result = "";
        for (int i = 0; i < b.length; i++)
        {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

}

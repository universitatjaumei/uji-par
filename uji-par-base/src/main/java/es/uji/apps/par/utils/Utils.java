package es.uji.apps.par.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;

import javax.ws.rs.core.Response.ResponseBuilder;

public class Utils
{
    public static String formatEuros(BigDecimal importe)
    {
        return importe.setScale(2).toString();
    }

    public static String sha1(String string)
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

    private static String byteArrayToHexString(byte[] b)
    {
        String result = "";
        for (int i = 0; i < b.length; i++)
        {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String monedaToCents(BigDecimal cantidad)
    {
        BigDecimal importeCentimos = cantidad.multiply(new BigDecimal(100));
        return Integer.toString(importeCentimos.intValue());
    }

    public static ResponseBuilder noCache(ResponseBuilder builder)
    {
        return builder
                .header("Cache-Control", "no-cache, no-store, must-revalidate").header("Pragma", "no-cache")
                .header("Expires", "0");
    }
}

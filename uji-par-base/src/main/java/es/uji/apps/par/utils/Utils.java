package es.uji.apps.par.utils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.uji.apps.par.model.OrdreGrid;

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
        return builder.header("Cache-Control", "no-cache, no-store, must-revalidate").header("Pragma", "no-cache")
                .header("Expires", "0");
    }
    
    public static OrdreGrid getSortFromParameter(String sortParameter) {
		try {
			if (sortParameter != null && !sortParameter.equals("")) {
				Gson gson = new Gson();
				OrdreGrid sort = new OrdreGrid();
				Type collectionType = new TypeToken<List<HashMap<String,String>>>(){}.getType();
				List<HashMap<String,String>> list = gson.fromJson(sortParameter, collectionType);
				sort.setPropietat(list.get(0).get("property"));
				sort.setOrdre(list.get(0).get("direction"));
				return sort;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static int inicializarLimitSiNecesario(int limit) {
		return (limit==0)?1000:limit;
	}
}

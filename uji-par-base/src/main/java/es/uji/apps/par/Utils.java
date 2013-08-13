package es.uji.apps.par;

import java.math.BigDecimal;

public class Utils
{
    public static String formatEuros(BigDecimal importe)
    {
        return importe.setScale(2).toString();
    }

}

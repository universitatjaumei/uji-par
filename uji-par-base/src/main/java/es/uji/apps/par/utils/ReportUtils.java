package es.uji.apps.par.utils;

import java.math.BigDecimal;
import java.util.Calendar;

public class ReportUtils {
	
	public static String formatEuros(BigDecimal importe)
    {
        return importe.setScale(2).toString().replace('.', ',');
    }
	
	public static String getMesValenciaConDe(Calendar fecha)
    {
        int mes = fecha.get(Calendar.MONTH);
        
        switch (mes)
        {
        case 0:
            return "de gener";
        case 1:
            return "de febrer";
        case 2:
            return "de març";
        case 3:
            return "d'abril";
        case 4:
            return "de maig";
        case 5:
            return "de juny";
        case 6:
            return "de juliol";
        case 7:
            return "d'agost";
        case 8:
            return "de setembre";
        case 9:
            return "d'octubre";
        case 10:
            return "de novembre";
        default:
            return "de desembre";
        }
    }
}

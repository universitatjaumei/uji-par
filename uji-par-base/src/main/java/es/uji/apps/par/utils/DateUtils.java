package es.uji.apps.par.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{
    private static final SimpleDateFormat FORMAT_DAY = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMAT_HOUR = new SimpleDateFormat("hh:mm");
    private static final SimpleDateFormat FORMAT_DAY_HOUR = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static Date spanishStringToDate(String spanishDate)
    {
    	Calendar cal = Calendar.getInstance();
    	
        if (spanishDate == null || spanishDate.isEmpty())
            return null;
        
        if (!isTimestamp(spanishDate)) {
	        String[] splitDate = spanishDate.split("\\/");
	        cal.set(Integer.valueOf(splitDate[2]), Integer.valueOf(splitDate[1]) - 1,
	                Integer.valueOf(splitDate[0]));
        } else
        	cal.setTimeInMillis(Long.valueOf(spanishDate));
        
        return cal.getTime();
    }

    private static boolean isTimestamp(String spanishDate) {
    	if (spanishDate.contains("/"))
    		return false;
    	else
    		return true;
	}

	public static Timestamp dateToTimestampSafe(Date fecha)
    {
        if (fecha == null)
            return null;
        else
            return new Timestamp(fecha.getTime());
    }

    public static String getHourAndMinutesWithLeadingZeros(Date date)
    {
        SimpleDateFormat date_format = new SimpleDateFormat("HH:mm");
        return date_format.format(date);
    }
    
    public static Date addTimeToDate(Date startDate, String hour)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        String[] arrHoraMinutos = hour.split(":");

        int hora = Integer.parseInt(arrHoraMinutos[0]);
        int minutos = Integer.parseInt(arrHoraMinutos[1]);

        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minutos);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }
    
    public static String dateToSpanishString(Date fecha) {
    	if (fecha == null)
    		throw new NullPointerException();
    	
    	return FORMAT_DAY.format(fecha);
    }
    
    public static String dateToHourString(Date fecha) {
        if (fecha == null)
            throw new NullPointerException();

        return FORMAT_HOUR.format(fecha);
    }
    
    public static long millisecondsToSeconds(long time) {
    	return time/1000;
    }

	public static Object dateToSpanishStringWithHour(Date fecha) {
		if (fecha == null)
    		throw new NullPointerException();
    	
    	return FORMAT_DAY_HOUR.format(fecha);
	}
}

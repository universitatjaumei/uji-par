package es.uji.apps.par.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Date spanishStringToDate(String spanishDate) {
		String[] splitDate = spanishDate.split("\\/");
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Integer.valueOf(splitDate[2]), Integer.valueOf(splitDate[1])-1, Integer.valueOf(splitDate[0]));
		return cal.getTime();
	}
	
	public static Timestamp dateToTimestampSafe(Date fecha) {
		if (fecha == null)
			return null;
		else
			return new Timestamp(fecha.getTime());
	}
	
	public static String getDayWithLeadingZeros(Date date) {
		SimpleDateFormat date_format = new SimpleDateFormat("HH:mm");
		return date_format.format(date);
	}
}

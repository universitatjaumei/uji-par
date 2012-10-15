package es.uji.apps.par.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Date spanishStringToDate(String spanishDate) {
		String[] splitDate = spanishDate.split("\\/");
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Integer.valueOf(splitDate[2]), Integer.valueOf(splitDate[1])-1, Integer.valueOf(splitDate[0]));
		return cal.getTime();
	}
}

package monitoring.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Date getUTC(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.ZONE_OFFSET) * (-1));
		return calendar.getTime();
	}
}

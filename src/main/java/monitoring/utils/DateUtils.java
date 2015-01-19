package monitoring.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	public static final String TIMEZONEID_MOSCOW = "Europe/Moscow";
	public static final String TIMEZONEID_UTC = "UTC";

	public static Date localTimeToOtherTimeZone(Date date, String timeZoneId) {
		return new Date(date.getTime()
				- TimeZone.getDefault().getOffset(date.getTime())
				+ TimeZone.getTimeZone(timeZoneId).getOffset(date.getTime()));
	}

	public static Calendar dateToCalendar(Date date, String timeZoneId) {
		// 1) date in utc
		Calendar source = Calendar.getInstance();
		source.setTime(date);

		// 2) apply UTC time zone
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
		calendar.set(source.get(Calendar.YEAR), source.get(Calendar.MONTH), source.get(Calendar.DAY_OF_MONTH),
				source.get(Calendar.HOUR_OF_DAY), source.get(Calendar.MINUTE), source.get(Calendar.SECOND));

		return calendar;
	}
	
	public static Date changeTimezone(Date date, String timeZone1, String timeZone2) {
		Calendar calendar1 = dateToCalendar(date, timeZone1);
		
		return localTimeToOtherTimeZone(calendar1.getTime(), timeZone2);
	}
}

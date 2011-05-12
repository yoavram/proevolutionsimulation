package il.ac.tau.yoavram.pes.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

public class TimeUtils {
	private static final Logger logger = Logger.getLogger(TimeUtils.class);
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MMM-dd_HH-mm-ss-SSS_zzz", Locale.ENGLISH);

	private TimeUtils() {
	}

	public static DateFormat getDateFormat() {
		return dateFormat;
	}

	public static String formatDate(Date date) {
		return getDateFormat().format(date);
	}

	public static String formatDate(long time) {
		return formatDate(new Date(time));
	}

	public static String formatDuration(long durationMillis) {
		return org.apache.commons.lang.time.DurationFormatUtils
				.formatDurationWords(durationMillis, true, true);
	}

	public static Date parse(String dateString) {
		Date d = null;
		try {
			d = getDateFormat().parse(dateString);
		} catch (ParseException e) {
			logger.error("Failed parsing a date from the string " + dateString
					+ ", Parse Exception: " + e);
		}
		return d;
	}

	public static java.sql.Date parseToSqlDate(String dateString) {
		return dateToSqlDate(parse(dateString));
	}

	public static java.sql.Date dateToSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}
}

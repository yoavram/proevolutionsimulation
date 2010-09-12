package il.ac.tau.yoavram.pes.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
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

}

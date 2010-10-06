package il.ac.tau.yoavram.pes.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {
	private static final NumberFormat numberFormat = NumberFormat
			.getInstance(Locale.ENGLISH);

	private NumberUtils() {
	}

	public static NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public static String formatNumber(Number num) {
		return getNumberFormat().format(num);

	}
}

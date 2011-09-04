package il.ac.tau.yoavram.pes.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
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

	public static Comparator<Integer> createNaturalIntegerComparator() {
		return new NaturalIntegerComparator();
	}

	public static class NaturalIntegerComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o1);
		}
	}

	public static Comparator<BigDecimal> createNaturalBigDecimalComparator() {
		return new NaturalBigDecimalComparator();
	}

	public static class NaturalBigDecimalComparator implements
			Comparator<BigDecimal> {

		@Override
		public int compare(BigDecimal o1, BigDecimal o2) {
			return o1.compareTo(o2);
		}
	}

	public static Comparator<Number> createNaturalNumberComparator() {
		return new NaturalNumberComparator();
	}

	public static class NaturalNumberComparator implements Comparator<Number> {

		@Override
		public int compare(Number o1, Number o2) {
			return Double.compare(o1.doubleValue(), o2.doubleValue());
		}
	}
}

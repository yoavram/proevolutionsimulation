package il.ac.tau.yoavram.pes.tests;

import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.text.NumberFormat;
import java.util.Locale;

public class TestNumberFormat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double d = 100234.8832;

		System.out.println(NumberFormat.getCurrencyInstance(Locale.ENGLISH).format(d));
		System.out.println(NumberFormat.getInstance(Locale.ENGLISH).format(d));
		System.out.println(NumberFormat.getIntegerInstance().format(d));
		System.out.println(NumberFormat.getNumberInstance().format(d));
		System.out.println(NumberFormat.getPercentInstance().format(d));

		long l = 100000;
		System.out.println(NumberFormat.getNumberInstance().format(l));
		System.out.println(NumberUtils.formatNumber(l));

	}

}

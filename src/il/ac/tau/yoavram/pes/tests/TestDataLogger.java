package il.ac.tau.yoavram.pes.tests;

import il.ac.tau.yoavram.pes.statistics.listeners.DataLogger;

public class TestDataLogger {
	DataLogger dl;

	@org.junit.Before
	public void setUp() {
		dl = new DataLogger();
	}

	@org.junit.Test
	public void stringsTest() {
		Number[] data = new Number[] { 1, 0.5, Double.NaN, Float.MAX_EXPONENT,
				Double.POSITIVE_INFINITY, 0 };
		dl.listen(data);
	}
}

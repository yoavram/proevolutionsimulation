package il.ac.tau.yoavram.pes.tests;

import il.ac.tau.yoavram.pes.statistics.listeners.ChartDrawer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestChartDrawer {
	ChartDrawer charter;

	@Before
	public void setUp() throws Exception {
		charter = new ChartDrawer();
		charter.setDataFieldNames(Lists.asList("x field", new String[] {
				"first field", "second", "another one" }));
		charter.init();
	}

	@Test
	public void testListen() {
		for (int i = 1; i < 1000; i++) {
			charter.listen(new Number[] { i,i,i,i});
		}
		Assert.assertNotNull(charter);
	}
}

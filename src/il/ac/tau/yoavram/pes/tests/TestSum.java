package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.aggregators.Sum;

import org.junit.Before;
import org.junit.Test;

public class TestSum {
	Sum<Object> sum;
	Object o = new Object();

	@Before
	public void setUp() throws Exception {
		sum = new Sum<Object>();
	}

	@Test
	public void testStart() {
		assertTrue(sum.result() == 0);
	}

	@Test
	public void testAggregate() {
		Aggregator<Object> agg = sum.aggregate(o);
		assertTrue(agg.equals(sum));
		assertTrue(sum.result() == 1);

		sum.aggregate(o).aggregate(o).aggregate(o).aggregate(o);
		assertTrue(sum.result() == 5);

		sum.clear();
		assertTrue(sum.result() == 0);

		sum.aggregate(o).aggregate(o).aggregate(o).aggregate(o);
		assertTrue(sum.result() == 4);
	}
}

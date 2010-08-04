package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertTrue;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.AbstractDataGatherer;
import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.statistics.aggregators.Sum;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;
import il.ac.tau.yoavram.pes.statistics.listeners.DataLogger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestAbstractDataGatherer {

	DataGatherer<Integer> gatherer;

	@Before
	public void setUp() throws Exception {
		Filter<Integer> flipFilter = new Filter<Integer>() {
			private boolean flip = true;

			@Override
			public boolean filter(Integer filtrate) {
				return flip = !flip;
			}
		};

		Collection<Integer> population = new HashSet<Integer>();
		for (int i = 0; i < 100; i++) {
			population.add(i);
		}

		DataListener testListener = new DataListener() {

			@Override
			public void listen(Number[] data) {
				assertTrue(data[0].equals(50));
			}

			@Override
			public void close() {
			}

			@Override
			public void setDataFieldNames(List<String> aggList) {
			}
		};

		gatherer = new IntegerDataGatherer();
		gatherer.addAggregator(new Sum<Integer>())
				.addListener(new DataLogger()).addListener(testListener)
				.addFilter(flipFilter).addPopulation(population);

	}

	@Test
	public void testHappen() {
		gatherer.happen();

	}

	private class IntegerDataGatherer extends AbstractDataGatherer<Integer> {

	}

}

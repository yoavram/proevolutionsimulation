package il.ac.tau.yoavram.pes.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.statistics.SimpleDataGatherer;
import il.ac.tau.yoavram.pes.statistics.aggregators.Sum;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;
import il.ac.tau.yoavram.pes.statistics.listeners.DataLogger;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * 
 * @author Yoav
 * 
 */
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

		@SuppressWarnings("serial")
		Model<Integer> mockModel = mock(Model.class);
		List<Integer> population = Lists.newArrayList();
		for (int i = 0; i < 100; i++) {
			population.add(i);
		}
		List<List<Integer>> popList = Lists.newArrayList();
		popList.add(population);
		when(mockModel.getPopulations()).thenReturn(popList);

		DataListener testListener = mock(DataListener.class);
		gatherer = new IntegerDataGatherer();
		gatherer.addAggregator(new Sum<Integer>())
				.addListener(new DataLogger()).addListener(testListener)
				.addFilter(flipFilter).setModel(mockModel);

	}

	@Test
	public void testGather() {
		gatherer.gather();
	}

	private class IntegerDataGatherer extends SimpleDataGatherer<Integer> {

	}

}

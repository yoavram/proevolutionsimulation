package il.ac.tau.yoavram.pes.statistics.aggregators;

import il.ac.tau.yoavram.pes.Simulation;

public class Ticker extends AbstractAggregator<Object> {

	@Override
	public Aggregator<Object> aggregate(Object input) {
		return this;
	}

	@Override
	public Number result() {
		return Simulation.getInstance().getTick();
	}

	@Override
	public void clear() {
	}

}

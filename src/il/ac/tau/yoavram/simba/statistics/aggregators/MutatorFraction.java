package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.AbstractAggregator;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.simba.SimBacteria;

public class MutatorFraction extends AbstractAggregator<SimBacteria> {

	private double total = 0;
	private double mutator = 0;

	@Override
	public Aggregator<SimBacteria> aggregate(SimBacteria input) {
		total++;
		if (input.getFitness() < input.getFitnessThreshold()) {
			mutator++;
		}
		return this;
	}

	@Override
	public Number result() {
		return mutator / total;
	}

	@Override
	public void clear() {
		mutator = total = 0;
	}

}

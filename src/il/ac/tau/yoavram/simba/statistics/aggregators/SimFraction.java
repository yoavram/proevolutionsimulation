package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.AbstractAggregator;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.SimBacteria;

public class SimFraction extends AbstractAggregator<Bacteria> {

	private double total = 0;
	private double sim = 0;

	@Override
	public Aggregator<Bacteria> aggregate(Bacteria input) {
		total++;
		if (input.getClass().equals(SimBacteria.class)) {
			sim++;
		}
		return this;
	}

	@Override
	public Double result() {
		return sim / total;
	}

	@Override
	public void clear() {
		sim = total = 0;
	}

}

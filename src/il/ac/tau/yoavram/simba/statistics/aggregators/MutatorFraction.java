package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.FilterFraction;
import il.ac.tau.yoavram.simba.SimpleBacteria;
import il.ac.tau.yoavram.simba.statistics.filters.MutatorBacteriaFilter;

public class MutatorFraction extends FilterFraction<SimpleBacteria> {

	public void init() {
		setFilter(new MutatorBacteriaFilter());
	}
}

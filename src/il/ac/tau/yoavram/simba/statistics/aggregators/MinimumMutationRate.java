package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.Minimum;
import il.ac.tau.yoavram.simba.Bacteria;

public class MinimumMutationRate extends Minimum<Bacteria> {

	@Override
	protected double extractData(Bacteria input) {
		return input.getMutationRate();
	}

}

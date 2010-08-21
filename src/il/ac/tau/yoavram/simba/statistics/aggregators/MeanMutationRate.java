package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.Mean;
import il.ac.tau.yoavram.simba.Bacteria;

public class MeanMutationRate extends Mean<Bacteria> {

	@Override
	protected Number extractData(Bacteria input) {
		return input.getMutationRate();
	}

}

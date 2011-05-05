package il.ac.tau.yoavram.simarba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.Maximum;
import il.ac.tau.yoavram.simba.Bacteria;

public class MaximumMutationThreshold extends Maximum<Bacteria> {

	@Override
	protected double extractData(Bacteria input) {
		return input.getMutationThreshold();
	}

}

package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.simba.Bacteria;

public class MeanFitness extends
		il.ac.tau.yoavram.pes.statistics.aggregators.Mean<Bacteria> {

	@Override
	protected Number extractData(Bacteria input) {
		return input.getFitness();
	}

}

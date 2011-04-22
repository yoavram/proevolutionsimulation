package il.ac.tau.yoavram.simarba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.StandardDeviation;
import il.ac.tau.yoavram.simba.Bacteria;

public class StandardDeviationTransformationRate extends StandardDeviation<Bacteria> {

	@Override
	protected double extractData(Bacteria input) {
		return input.getTransformationRate();
	}

}

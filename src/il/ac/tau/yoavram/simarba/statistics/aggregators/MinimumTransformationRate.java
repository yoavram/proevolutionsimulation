package il.ac.tau.yoavram.simarba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.Minimum;
import il.ac.tau.yoavram.simba.Bacteria;

public class MinimumTransformationRate extends Minimum<Bacteria> {

	@Override
	protected double extractData(Bacteria input) {
		return input.getTransformationRate();
	}

}
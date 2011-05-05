package il.ac.tau.yoavram.simarba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.Mean;
import il.ac.tau.yoavram.simba.Bacteria;

public class MeanTransformationThreshold extends Mean<Bacteria> {

	@Override
	protected double extractData(Bacteria input) {
		return input.getTransformationThreshold();
	}

}

package il.ac.tau.yoavram.simarba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.StandardDeviation;
import il.ac.tau.yoavram.simarba.TransformableBacteria;

public class StandardDeviationMutationThreshold extends
		StandardDeviation<TransformableBacteria> {

	@Override
	protected double extractData(TransformableBacteria input) {
		return input.getMutationThreshold();
	}

}

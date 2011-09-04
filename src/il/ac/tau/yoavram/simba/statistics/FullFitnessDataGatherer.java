package il.ac.tau.yoavram.simba.statistics;

import il.ac.tau.yoavram.pes.statistics.FullDataGatherer;
import il.ac.tau.yoavram.simba.Bacteria;

public class FullFitnessDataGatherer extends FullDataGatherer<Bacteria> {

	@Override
	protected Number extractData(Bacteria t) {
		return t.getFitness();
	}

}

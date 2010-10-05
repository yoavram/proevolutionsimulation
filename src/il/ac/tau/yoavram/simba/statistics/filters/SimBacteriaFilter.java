package il.ac.tau.yoavram.simba.statistics.filters;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.simba.Bacteria;

public class SimBacteriaFilter implements Filter<Bacteria> {

	@Override
	public boolean filter(Bacteria filtrate) {
		return filtrate.isSim();
	}

}

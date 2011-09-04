package il.ac.tau.yoavram.simba.statistics.filters;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.simba.SimpleBacteria;

public class CmBacteriaFilter implements Filter<SimpleBacteria> {

	@Override
	public boolean filter(SimpleBacteria filtrate) {
		return filtrate.isCm();
	}

}

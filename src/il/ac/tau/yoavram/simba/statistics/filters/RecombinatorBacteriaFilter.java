package il.ac.tau.yoavram.simba.statistics.filters;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.SexyBacteria;

public class RecombinatorBacteriaFilter implements Filter<Bacteria> {

	@Override
	public boolean filter(Bacteria filtrate) {
		return filtrate instanceof SexyBacteria && ((SexyBacteria)filtrate).isRecombinator();
	}
}

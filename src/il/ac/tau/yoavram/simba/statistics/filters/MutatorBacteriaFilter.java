package il.ac.tau.yoavram.simba.statistics.filters;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.SexyBacteria;
import il.ac.tau.yoavram.simba.SimpleBacteria;

public class MutatorBacteriaFilter implements Filter<Bacteria> {

	@Override
	public boolean filter(Bacteria filtrate) {
		return (filtrate instanceof SexyBacteria && ((SexyBacteria) filtrate)
				.isMutator())
				|| (filtrate instanceof SimpleBacteria && ((SimpleBacteria) filtrate)
						.isMutator());
	}
}

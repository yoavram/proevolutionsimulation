package il.ac.tau.yoavram.simba.statistics.filters;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.ModifierBacteria;
import il.ac.tau.yoavram.simba.SexyBacteria;
import il.ac.tau.yoavram.simba.SimpleBacteria;

public class CmBacteriaFilter implements Filter<Bacteria> {

	@Override
	public boolean filter(Bacteria filtrate) {
		return (filtrate instanceof SexyBacteria && ((SexyBacteria) filtrate)
				.isCm())
				|| (filtrate instanceof ModifierBacteria && ((ModifierBacteria) filtrate)
						.isCm())
				|| (filtrate instanceof SimpleBacteria && ((SimpleBacteria) filtrate)
						.isCm());
	}

}

package il.ac.tau.yoavram.simba.statistics.filters;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.ModifierBacteria;
import il.ac.tau.yoavram.simba.SexyBacteria;

public class CrBacteriaFilter implements Filter<Bacteria> {

	@Override
	public boolean filter(Bacteria filtrate) {
		return (filtrate instanceof SexyBacteria && ((SexyBacteria) filtrate)
				.isCr())
				|| (filtrate instanceof ModifierBacteria && ((ModifierBacteria) filtrate)
						.isCr()) ;
	}

}

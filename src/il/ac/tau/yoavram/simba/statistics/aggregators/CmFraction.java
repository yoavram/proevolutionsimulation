package il.ac.tau.yoavram.simba.statistics.aggregators;

import il.ac.tau.yoavram.pes.statistics.aggregators.FilterFraction;
import il.ac.tau.yoavram.simba.Bacteria;
import il.ac.tau.yoavram.simba.statistics.filters.CmBacteriaFilter;

public class CmFraction extends FilterFraction<Bacteria> {

	public void init() {
		setFilter(new CmBacteriaFilter());
	}
}

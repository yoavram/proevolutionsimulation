package il.ac.tau.yoavram.pes.statistics.aggregators;

import il.ac.tau.yoavram.pes.filters.Filter;

public class FilterFraction<T> extends AbstractAggregator<T> implements
		Aggregator<T> {

	private double total = 0;
	private double filtered = 0;
	private Filter<T> filter;

	@Override
	public Aggregator<T> aggregate(T input) {
		total++;
		if (getFilter().filter(input))
			filtered++;
		return this;
	}

	@Override
	public Number result() {
		return filtered / total;
	}

	@Override
	public void clear() {
		total = 0;
		filtered = 0;
	}

	public Filter<T> getFilter() {
		return filter;
	}

	public void setFilter(Filter<T> filter) {
		this.filter = filter;
	}
}

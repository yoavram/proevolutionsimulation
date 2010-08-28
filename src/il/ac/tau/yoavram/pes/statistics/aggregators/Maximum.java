package il.ac.tau.yoavram.pes.statistics.aggregators;

public abstract class Maximum<T> extends AbstractAggregator<T> implements
		Aggregator<T> {

	protected double max = Double.MIN_VALUE;

	public Maximum() {
		clear();
	}

	public Aggregator<T> aggregate(T input) {
		double data = extractData(input);
		max = Math.max(max, data);
		return this;
	}

	protected abstract double extractData(T input);

	@Override
	public void clear() {
		max = Double.NEGATIVE_INFINITY;
	}

	@Override
	public Number result() {
		return max;
	}
}

package il.ac.tau.yoavram.pes.statistics.aggregators;

public abstract class Minimum<T> extends AbstractAggregator<T> implements
		Aggregator<T> {

	protected double min = Double.MAX_VALUE;

	public Minimum() {
		clear();
	}

	public Aggregator<T> aggregate(T input) {
		double data = extractData(input);
		min = Math.min(min, data);
		return this;
	}

	protected abstract double extractData(T input);

	@Override
	public void clear() {
		min = Double.POSITIVE_INFINITY;
	}

	@Override
	public Number result() {
		return min;
	}
}

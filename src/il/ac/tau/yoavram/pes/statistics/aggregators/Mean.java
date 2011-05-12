package il.ac.tau.yoavram.pes.statistics.aggregators;

public abstract class Mean<T> extends AbstractAggregator<T> implements
		Aggregator<T> {

	protected double count;
	protected double mean;

	public Mean() {
		clear();
	}

	public Aggregator<T> aggregate(T input) {
		double data = extractData(input);
		mean = ((mean * count) + data) / (count + 1);
		count++;
		return this;
	}

	protected abstract double extractData(T input);

	@Override
	public void clear() {
		count = 0;
		mean = 0;
	}

	@Override
	public Number result() {
		return mean;
	}
}

package il.ac.tau.yoavram.pes.statistics.aggregators;

public abstract class StandardDeviation<T> extends AbstractAggregator<T>
		implements Aggregator<T> {

	private Mean<T> meanAggregator;

	protected int count;
	protected double mean;

	public StandardDeviation() {
		clear();
	}

	@Override
	public Aggregator<T> aggregate(T input) {
		double data = extractData(input);
		data = Math.pow(data, 2);
		mean = ((mean * count) + data) / (count + 1);
		count++;
		return this;
	}

	@Override
	public void clear() {
		count = 0;
		mean = 0;
	}

	@Override
	public Number result() {
		return Math.sqrt(mean
				- Math.pow(meanAggregator.result().doubleValue(), 2));
	}

	protected abstract double extractData(T input);

	public void setMeanAggregator(Mean<T> meanAggregator) {
		this.meanAggregator = meanAggregator;
	}

	public Mean<T> getMeanAggregator() {
		return meanAggregator;
	}

}

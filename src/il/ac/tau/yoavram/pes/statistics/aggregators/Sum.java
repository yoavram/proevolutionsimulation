package il.ac.tau.yoavram.pes.statistics.aggregators;

public class Sum<T> extends AbstractAggregator<T> implements Aggregator<T>  {
	private int count = 0;

	@Override
	public Aggregator<T> aggregate(T input) {
		count++;
		return this;
	}

	@Override
	public Integer result() {
		return count;
	}

	@Override
	public void clear() {
		count = 0;
	}

	
}

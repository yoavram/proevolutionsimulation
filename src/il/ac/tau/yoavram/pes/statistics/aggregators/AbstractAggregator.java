package il.ac.tau.yoavram.pes.statistics.aggregators;

public abstract class AbstractAggregator<T> implements Aggregator<T> {
	private String name;

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}

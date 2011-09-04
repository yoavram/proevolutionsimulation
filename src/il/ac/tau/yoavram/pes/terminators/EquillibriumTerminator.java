package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;

public class EquillibriumTerminator extends AbstractTerminator implements
		Terminator {

	private Aggregator<?> aggregator;
	private double error = Double.NaN;
	private Number cache = Double.POSITIVE_INFINITY;
	private int count = 0;
	private int numberOfTicks = 0;

	@Override
	public boolean terminate() {
		Number preValue = cache;
		cache = getAggregator().result();
		if (Math.abs(preValue.doubleValue() - cache.doubleValue()) < getError())
			count++;
		else
			count = 0;
		return count > getNumberOfTicks();
	}

	public void setAggregator(Aggregator<?> aggregator) {
		this.aggregator = aggregator;
	}

	public Aggregator<?> getAggregator() {
		return aggregator;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getError() {
		return error;
	}

	public void setNumberOfTicks(int numberOfTicks) {
		this.numberOfTicks = numberOfTicks;
	}

	public int getNumberOfTicks() {
		return numberOfTicks;
	}
}

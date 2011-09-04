package il.ac.tau.yoavram.pes.statistics.aggregators;

public interface Aggregator<I> {
	Aggregator<I> aggregate(I input);

	Number result();

	void clear();

	String getName();

	void setName(String name);
}

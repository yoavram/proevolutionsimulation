package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.util.Collection;
import java.util.List;

public interface DataGatherer<T> {
	DataGatherer<T> addFilter(Filter<T> filter);

	List<Filter<T>> getFilters();

	DataGatherer<T> setFilters(List<Filter<T>> filters);

	DataGatherer<T> addAggregator(Aggregator<T> aggregator);

	List<Aggregator<T>> getAggregators();

	DataGatherer<T> setAggregators(List<Aggregator<T>> aggregators);

	DataGatherer<T> addPopulation(Collection<T> population);

	Collection<Collection<T>> getPopulations();

	DataGatherer<T> setPopulations(Collection<Collection<T>> populations);

	DataGatherer<T> addListener(DataListener listener);

	Collection<DataListener> getListeners();

	DataGatherer<T> setListeners(Collection<DataListener> listeners);

	void gather();

}

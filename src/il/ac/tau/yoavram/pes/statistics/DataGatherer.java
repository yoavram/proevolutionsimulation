package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;

public interface DataGatherer<T> extends Closeable {
	DataGatherer<T> addFilter(Filter<T> filter);

	List<Filter<T>> getFilters();

	DataGatherer<T> setFilters(List<Filter<T>> filters);

	DataGatherer<T> addAggregator(Aggregator<T> aggregator);

	List<Aggregator<T>> getAggregators();

	DataGatherer<T> setAggregators(List<Aggregator<T>> aggregators);

	DataGatherer<T> addListener(DataListener listener);

	Collection<DataListener> getListeners();

	DataGatherer<T> setListeners(Collection<DataListener> listeners);

	DataGatherer<T> setModel(Model<T> model);

	Model<T> getModel();

	DataGatherer<T> setInterval(int interval);

	int getInterval();

	void gather();

}

package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractDataGatherer<T> implements DataGatherer<T> {
	private List<Aggregator<T>> aggregators;
	private List<Filter<T>> filters;
	private Collection<DataListener> listeners;
	private Collection<Collection<T>> populations;

	public AbstractDataGatherer() {
		aggregators = new ArrayList<Aggregator<T>>();
		filters = new ArrayList<Filter<T>>();
		listeners = new HashSet<DataListener>();
		populations = new HashSet<Collection<T>>();
	}

	@Override
	public DataGatherer<T> addAggregator(Aggregator<T> aggregator) {
		aggregators.add(aggregator);
		return this;
	}

	@Override
	public DataGatherer<T> addFilter(Filter<T> filter) {
		filters.add(filter);
		return this;
	}

	@Override
	public DataGatherer<T> addListener(DataListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public DataGatherer<T> addPopulation(Collection<T> population) {
		populations.add(population);
		return this;
	}

	@Override
	public List<Aggregator<T>> getAggregators() {
		return aggregators;
	}

	@Override
	public List<Filter<T>> getFilters() {
		return filters;
	}

	@Override
	public Collection<DataListener> getListeners() {
		return listeners;
	}

	@Override
	public Collection<Collection<T>> getPopulations() {
		return populations;
	}

	@Override
	public DataGatherer<T> setAggregators(List<Aggregator<T>> aggregators) {
		this.aggregators = aggregators;
		return this;
	}

	@Override
	public DataGatherer<T> setFilters(List<Filter<T>> filters) {
		this.filters = filters;
		return this;
	}

	@Override
	public DataGatherer<T> setListeners(Collection<DataListener> listeners) {
		this.listeners = listeners;
		return this;
	}

	@Override
	public DataGatherer<T> setPopulations(Collection<Collection<T>> populations) {
		this.populations = populations;
		return this;
	}

	public void init() {
		List<String> aggList = new ArrayList<String>(aggregators.size());
		for (Aggregator<T> agg : aggregators) {
			aggList.add(agg.getName());
		}
		for (DataListener dl : listeners) {
			dl.setDataFieldNames(aggList);
		}
	}

	@Override
	public void happen() {
		for (Collection<T> pop : populations) {
			for (T t : pop) {
				boolean isFilter = true;
				for (Filter<T> filter : filters) {
					if (!filter.filter(t)) {
						isFilter = false;
						break;
					}
				}
				if (isFilter) {
					for (Aggregator<T> agg : aggregators) {
						agg.aggregate(t);
					}
				}
			}
		}

		List<Number> dataList = new ArrayList<Number>(aggregators.size());
		for (Aggregator<T> agg : aggregators) {
			dataList.add(agg.result());
		}
		for (Aggregator<T> agg : aggregators) {
			agg.clear();
		}
		Number[] data = dataList.toArray(new Number[0]);
		for (DataListener listener : listeners) {
			listener.listen(data);
		}
	}
}

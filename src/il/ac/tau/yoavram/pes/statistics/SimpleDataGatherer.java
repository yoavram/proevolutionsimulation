package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

public class SimpleDataGatherer<T> implements DataGatherer<T> {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SimpleDataGatherer.class);

	private List<Aggregator<T>> aggregators;
	private List<Filter<T>> filters;
	private Collection<DataListener> listeners;
	private Model<T> model;
	private int interval = 1;

	public SimpleDataGatherer() {
		aggregators = new ArrayList<Aggregator<T>>();
		filters = new ArrayList<Filter<T>>();
		listeners = new HashSet<DataListener>();
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

	public DataGatherer<T> setModel(Model<T> model) {
		this.model = model;
		return this;
	}

	public Model<T> getModel() {
		return model;
	}

	@Override
	public SimpleDataGatherer<T> setInterval(int interval) {
		this.interval = interval;
		return this;
	}

	@Override
	public int getInterval() {
		return interval;
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
	public void gather() {
		for (Aggregator<T> agg : getAggregators()) {
			agg.clear();
		}
		for (List<T> pop : getModel().getPopulations()) {
			for (T t : pop) {
				boolean isFilter = true;
				for (Filter<T> filter : getFilters()) {
					if (!filter.filter(t)) {
						isFilter = false;
						break;
					}
				}
				if (isFilter) {
					for (Aggregator<T> agg : getAggregators()) {
						agg.aggregate(t);
					}
				}
			}
		}

		List<Number> dataList = new ArrayList<Number>(getAggregators().size());
		for (Aggregator<T> agg : getAggregators()) {
			dataList.add(agg.result());
		}
		Number[] data = dataList.toArray(new Number[0]);
		for (DataListener listener : getListeners()) {
			listener.listen(data);
		}
	}
}

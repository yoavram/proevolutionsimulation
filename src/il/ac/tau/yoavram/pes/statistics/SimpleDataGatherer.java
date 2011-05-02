package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

public class SimpleDataGatherer<T> implements DataGatherer<T> {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SimpleDataGatherer.class);
	protected static final Number[] EMPTY_NUMBER_ARRAY = new Number[0];

	private List<Aggregator<T>> aggregators;
	private List<Filter<T>> filters;
	private Collection<DataListener> listeners;
	private Collection<DataListener> finalListeners;

	private Model<T> model;
	private int interval = 1;
	//protected List<Number> dataList;
	protected Number[] data;

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
	public DataGatherer<T> addFinalListener(DataListener listener) {
		finalListeners.add(listener);
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
	public Collection<DataListener> getFinalListeners() {
		return finalListeners;
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
	public DataGatherer<T> setFinalListeners(
			Collection<DataListener> finalListeners) {
		this.finalListeners = finalListeners;
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
	//	dataList = new ArrayList<Number>(getAggregators().size());
		data = new Number[getAggregators().size()];
	}

	@Override
	public void close() throws IOException {
		// Number[] data = dataList.toArray(EMPTY_NUMBER_ARRAY);
		for (DataListener listener : getFinalListeners()) {
			listener.listen(data);
			listener.close();
		}
		for (DataListener listener : getListeners()) {
			listener.close();
		}
	}

	@Override
	public void gather() {
		// dataList.clear();
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

		/*for (Aggregator<T> agg : getAggregators()) {
			dataList.add(agg.result());
		}*/
		for (int i = 0; i < data.length; i++) {
			data[i] = getAggregators().get(i).result();
		}
		//Number[] data = dataList.toArray(EMPTY_NUMBER_ARRAY);
		for (DataListener listener : getListeners()) {
			listener.listen(data);
		}
	}
}

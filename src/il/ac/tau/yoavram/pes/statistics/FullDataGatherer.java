package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class FullDataGatherer<T> extends SimpleDataGatherer<T> {
	private Aggregator<T> ticker;
	private List<Number> dataList;

	@Override
	public void init() {
		dataList = Lists.newArrayList();
	}

	@Override
	public void gather() {
		dataList.clear();
		dataList.add(getTicker().result());

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
					dataList.add(extractData(t));
				}
			}
		}

		Number[] data = dataList.toArray(EMPTY_NUMBER_ARRAY);
		for (DataListener listener : getListeners()) {
			listener.listen(data);
		}
	}

	protected abstract Number extractData(T t);

	public void setTicker(Aggregator<T> ticker) {
		this.ticker = ticker;
	}

	public Aggregator<T> getTicker() {
		return ticker;
	}
}
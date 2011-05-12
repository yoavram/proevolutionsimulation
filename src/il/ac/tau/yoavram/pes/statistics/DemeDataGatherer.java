package il.ac.tau.yoavram.pes.statistics;

import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

public class DemeDataGatherer<T> extends SimpleDataGatherer<T> implements
		DataGatherer<T> {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DemeDataGatherer.class);

	private int numberOfDemes;
	private int chosenAggregator;

	public DemeDataGatherer() {
		super();
	}

	public void init() {
		List<String> demes = Lists
				.newArrayListWithExpectedSize(getNumberOfDemes()+1);
		demes.add(getAggregators().get(0).getName());
		for (int i = 0; i < getNumberOfDemes(); i++) {
			demes.add("Deme " + i);
		}
		for (DataListener dl : getListeners()) {
			dl.setDataFieldNames(demes);
		}
		data = new Number[getNumberOfDemes() + 1];
	}

	@Override
	public void gather() {
		data[0] = getAggregators().get(0).result();
		for (int i = 0; i < getModel().getPopulations().size(); i++) {
			List<T> pop = getModel().getPopulations().get(i);
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
			data[i + 1] = getAggregators().get(getChosenAggregator()).result();
			for (Aggregator<T> agg : getAggregators()) {
				agg.clear();
			}
		}

		for (DataListener listener : getListeners()) {
			listener.listen(data);
		}
	}

	public void setNumberOfDemes(int numberOfDemes) {
		this.numberOfDemes = numberOfDemes;
	}

	public int getNumberOfDemes() {
		return numberOfDemes;
	}

	public void setChosenAggregator(int chosenAggregator) {
		this.chosenAggregator = chosenAggregator;
	}

	public int getChosenAggregator() {
		return chosenAggregator;
	}
}

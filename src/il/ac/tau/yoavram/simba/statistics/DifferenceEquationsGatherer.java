package il.ac.tau.yoavram.simba.statistics;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.DataGatherer;
import il.ac.tau.yoavram.pes.statistics.aggregators.Aggregator;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;
import il.ac.tau.yoavram.simba.DifferenceEquationsModel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.math.linear.RealVector;

import com.google.common.collect.Lists;

public class DifferenceEquationsGatherer implements DataGatherer<RealVector> {
	private static final Number[] EMPTY_NUMBER_ARRAY = new Number[0];
	private DifferenceEquationsModel model;
	private int interval = 1;
	private List<DataListener> listeners;
	private List<Number> data;

	@Override
	public void gather() {

		double[] freqs = model.getFrequencies();
		if (data == null) {
			data = Lists.newArrayList();
			List<String> header = Lists.newArrayList();
			header.add("step");
			for (int i = 0; i < freqs.length; i++) {
				header.add(Integer.toString(i));
			}
			// header.add("mean fitness");
			// header.add("distance");
			for (DataListener l : listeners) {
				l.setDataFieldNames(header);
			}
		}
		data.clear();
		data.add(Simulation.getInstance().getTick());
		for (int i = 0; i < freqs.length; i++) {
			data.add(freqs[i]);
		}
		// data[freqs.length + 1] = model.getMeanFitness();
		// data[freqs.length + 2] = model.getDistance();
		for (DataListener l : listeners) {
			l.listen(data.toArray(EMPTY_NUMBER_ARRAY));
		}
	}

	@Override
	public void close() throws IOException {
		for (DataListener l : listeners) {
			l.close();
		}
	}

	@Override
	public DataGatherer<RealVector> addFilter(Filter<RealVector> filter) {
		throw new NotImplementedException();
	}

	@Override
	public List<Filter<RealVector>> getFilters() {
		throw new NotImplementedException();
	}

	@Override
	public DataGatherer<RealVector> setFilters(List<Filter<RealVector>> filters) {
		throw new NotImplementedException();
	}

	@Override
	public DataGatherer<RealVector> addAggregator(
			Aggregator<RealVector> aggregator) {
		throw new NotImplementedException();
	}

	@Override
	public List<Aggregator<RealVector>> getAggregators() {
		throw new NotImplementedException();
	}

	@Override
	public DataGatherer<RealVector> setAggregators(
			List<Aggregator<RealVector>> aggregators) {
		throw new NotImplementedException();
	}

	@Override
	public DataGatherer<RealVector> addListener(DataListener listener) {
		if (listeners == null)
			listeners = Lists.newArrayList();
		listeners.add(listener);
		return this;
	}

	@Override
	public Collection<DataListener> getListeners() {
		return listeners;
	}

	@Override
	public DataGatherer<RealVector> setListeners(
			Collection<DataListener> listeners) {
		this.listeners = Lists.newArrayList(listeners);
		return this;
	}

	@Override
	public DataGatherer<RealVector> setModel(Model<RealVector> model) {
		if (model instanceof DifferenceEquationsModel)
			this.model = (DifferenceEquationsModel) model;
		else
			throw new IllegalArgumentException("Model must be instance of "
					+ DifferenceEquationsModel.class.getSimpleName());
		return this;
	}

	@Override
	public Model<RealVector> getModel() {
		return model;
	}

	@Override
	public DataGatherer<RealVector> setInterval(int interval) {
		this.interval = interval;
		return this;
	}

	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return interval;
	}

}

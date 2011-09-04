package il.ac.tau.yoavram.simba;

import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;
import il.ac.tau.yoavram.pes.utils.SpringUtils;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class FitnessHistogramCollector {
	private static final Logger logger = Logger
			.getLogger(FitnessHistogramCollector.class);
	private static final Double[] EMPTY_DOUBLE_ARRAY = new Double[0];

	private SimbaModel model;
	private Filter<Bacteria> filter = null;
	private DataListener listener;
	private double fractionOfGenesToChange = 0;

	public static void main(String[] args) {
		FitnessHistogramCollector collector = SpringUtils.getBean(args,
				FitnessHistogramCollector.class);
		collector.start();
	}

	public void start() {
		logger.info("collecting from model " + model.getID());
		logger.info("writing data to " + listener.getClass().getSimpleName());

		collect();
		Simulation.getInstance().incrementTick();
		model.getEnvironment().change(getFractionOfGenesToChange());
		collect();
	}

	private void collect() {
		Multiset<Double> histogram = TreeMultiset.create();
		for (List<Bacteria> population : model.getPopulations()) {
			for (Bacteria b : population) {
				if (filter == null || filter.filter(b))
					histogram.add(b.getFitness());
			}
		}

		Double[] values = histogram.elementSet().toArray(EMPTY_DOUBLE_ARRAY);
		List<String> header = Lists.newArrayList();
		for (Double v : values) {
			header.add(v.toString());
		}
		listener.setDataFieldNames(header);
		Integer[] counts = new Integer[values.length];
		int i = 0;
		for (Double v : values) {
			counts[i++] = histogram.count(v);
		}
		listener.listen(counts);

	}

	public DataListener getListener() {
		return listener;
	}

	public void setListener(DataListener listener) {
		this.listener = listener;
	}

	public SimbaModel getModel() {
		return model;
	}

	public void setModel(SimbaModel model) {
		this.model = model;
	}

	public void setFilter(Filter<Bacteria> filter) {
		this.filter = filter;
	}

	public Filter<Bacteria> getFilter() {
		return filter;
	}

	public void setFractionOfGenesToChange(double fractionOfGenesToChange) {
		this.fractionOfGenesToChange = fractionOfGenesToChange;
	}

	public double getFractionOfGenesToChange() {
		return fractionOfGenesToChange;
	}

}
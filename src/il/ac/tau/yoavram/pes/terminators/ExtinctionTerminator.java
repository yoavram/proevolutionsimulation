package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.Simulation;
import il.ac.tau.yoavram.pes.filters.Filter;
import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.util.List;

import org.apache.log4j.Logger;

public class ExtinctionTerminator<T> extends AbstractTerminator implements
		Terminator {

	private static final Logger logger = Logger
			.getLogger(ExtinctionTerminator.class);

	Model<T> model;
	Filter<T> filter;

	@Override
	public boolean terminate() {
		for (List<T> population : getModel().getPopulations()) {
			for (T t : population) {
				if (filter.filter(t)) {
					return false;
				}
			}
		}
		logger.info(String.format(
				"Tick %d: %s found nothing in the population",
				NumberUtils.formatNumber(Simulation.getInstance().getTick()),
				filter.getClass().getSimpleName()));
		return true;
	}

	public Model<T> getModel() {
		return model;
	}

	public void setModel(Model<T> model) {
		this.model = model;
	}

	public Filter<T> getFilter() {
		return filter;
	}

	public void setFilter(Filter<T> filter) {
		this.filter = filter;
	}

}

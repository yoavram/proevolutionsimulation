package il.ac.tau.yoavram.pes.terminators;

import il.ac.tau.yoavram.pes.Model;
import il.ac.tau.yoavram.pes.filters.Filter;

import java.util.List;

public class ExtinctionTerminator<T> extends AbstractTerminator implements
		Terminator {
	/*private static final Logger logger = Logger
			.getLogger(ExtinctionTerminator.class);*/

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

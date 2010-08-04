package il.ac.tau.yoavram.pes;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public abstract class AbstractModel<T> implements Model<T> {

	private static final long serialVersionUID = 1L;
	private transient final Date date = Calendar.getInstance().getTime();

	protected Collection<T> population;

	@Override
	public Collection<T> getPopulation() {
		return population;
	}

	@Override
	public void setPopulation(Collection<T> population) {
		this.population = population;
	}
}

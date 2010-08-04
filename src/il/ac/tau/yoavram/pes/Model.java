package il.ac.tau.yoavram.pes;

import java.io.Serializable;
import java.util.Collection;

public interface Model<T> extends Serializable {
	Collection<Collection<T>> getPopulations();

	Model<T> setPopulations(Collection<Collection<T>> populations);

	Model<T> addPopulation(Collection<T> population);

	Collection<T> getPopulation();

	Model<T> setPopulation(Collection<T> population);

	// TODO map, resources,...

}

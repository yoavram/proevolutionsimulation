package il.ac.tau.yoavram.pes;

import java.util.List;

public interface Model<T> {
	void step();

	void init();

	List<List<T>> getPopulations();

	void setPopulations(List<List<T>> populations);

	Object getID();

	void setID(Object id);

	// TODO map, resources,...

}

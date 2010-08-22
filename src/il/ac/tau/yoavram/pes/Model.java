package il.ac.tau.yoavram.pes;

import java.util.Date;
import java.util.List;

public interface Model<T> {
	void step();

	void init();

	List<List<T>> getPopulations();

	void setPopulations(List<List<T>> populations);

	Date getTime();

	void setTime(Date time);

	// TODO map, resources,...

}

package il.ac.tau.yoavram.pes;

import java.io.Serializable;

public interface Model extends Serializable {
	void step();

	void init();

	// TODO map, resources,...

}

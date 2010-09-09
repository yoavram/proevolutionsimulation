package il.ac.tau.yoavram.pes.statistics.listeners;

import java.io.Closeable;
import java.util.List;

public interface DataListener extends Closeable {
	void listen(Number[] data);

	void setDataFieldNames(List<String> dataFieldNames);

}

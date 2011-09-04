package il.ac.tau.yoavram.pes.statistics.listeners;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface DataListener extends Closeable {

	void init();

	void close() throws IOException;

	void listen(Number[] data);

	void setDataFieldNames(List<String> dataFieldNames);

}

package il.ac.tau.yoavram.pes.statistics.listeners;

import java.util.List;

public interface DataListener {
	void listen(Number[] data);

	void setDataFieldNames(List<String> dataFieldNames);

}

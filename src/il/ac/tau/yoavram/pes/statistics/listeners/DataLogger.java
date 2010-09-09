package il.ac.tau.yoavram.pes.statistics.listeners;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class DataLogger implements DataListener {
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void listen(Number[] data) {
		logger.info(Arrays.toString(data));
	}

	@Override
	public void setDataFieldNames(List<String> aggList) {
		logger.info(Arrays.toString(aggList.toArray()));
	}

	@Override
	public void close() throws IOException {
	}

}

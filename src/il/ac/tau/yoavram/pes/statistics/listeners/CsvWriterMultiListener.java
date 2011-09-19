package il.ac.tau.yoavram.pes.statistics.listeners;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The close method of this class does nothing, as this class is expected to be used with multiple gatherers. 
 * Therefore one must take care that Spring will call <code>destroy()</code> when the application is over.
 * @author yoavram
 *
 */
public class CsvWriterMultiListener extends CsvWriterListener {
	private List<String> header = null;
	private List<Number> dataList = null;
	private boolean firstListen = true;

	public CsvWriterMultiListener() {
		dataList = Lists.newArrayList();
		header = Lists.newArrayList();
	}

	@Override
	public void setDataFieldNames(List<String> aggList) {
		header.addAll(aggList);
	}

	@Override
	public void listen(Number[] data) {
		if (firstListen) {
			writeHeader(header.toArray(new String[0]));
			firstListen = false;
		}

		dataList.addAll(Arrays.asList(data));

		if (dataList.size() == header.size()) {
			getCsvWriter().writeRow(dataList.toArray());
			dataList.clear();
		} else if (dataList.size() > header.size()) {
			throw new IllegalStateException("More values than headers!");
		}
	}

	@Override
	public void close() throws IOException {
		// do nothing, because multiple gatherers will use this and you don't
		// want the first one to close this.
	}

	public void destroy() throws IOException {
		super.close();
	}
}

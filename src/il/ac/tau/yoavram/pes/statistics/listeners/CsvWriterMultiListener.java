package il.ac.tau.yoavram.pes.statistics.listeners;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

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
}

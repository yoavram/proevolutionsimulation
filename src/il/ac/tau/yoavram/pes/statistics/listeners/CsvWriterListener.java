package il.ac.tau.yoavram.pes.statistics.listeners;

import java.util.List;

import il.ac.tau.yoavram.pes.io.CsvWriter;

public class CsvWriterListener implements DataListener {
	private CsvWriter writer;

	public CsvWriterListener() {
	}

	public CsvWriterListener(CsvWriter writer, String[] header) {
		setCsvWriter(writer);
	}

	public void writeHeader(String[] header) {
		writer.writeRow(header);
	}

	@Override
	public void listen(Number[] data) {
		writer.writeRow(data);
	}

	public void setCsvWriter(CsvWriter writer) {
		this.writer = writer;
	}

	public CsvWriter getCsvWriter() {
		return writer;
	}

	@Override
	public void close() {
		writer.close();
	}

	@Override
	public void setDataFieldNames(List<String> aggList) {
		writeHeader(aggList.toArray(new String[0]));

	}

}

package il.ac.tau.yoavram.pes.statistics.listeners;

import il.ac.tau.yoavram.pes.io.CsvWriter;

import java.util.List;

import org.apache.log4j.Logger;

public class CsvWriterListener extends ThreadListener {
	private static final Logger logger = Logger
			.getLogger(CsvWriterListener.class);

	private CsvWriter writer;

	public CsvWriterListener() {
		this(DEFAULT_TITLE);
	}

	public CsvWriterListener(String name) {
		super(name + " " + CsvWriterListener.class.getSimpleName());
	}

	public void writeHeader(String[] header) {
		getCsvWriter().writeRow(header);
	}

	@Override
	protected void consume(Number[] data) {
		getCsvWriter().writeRow(data);
	}

	public void setCsvWriter(CsvWriter writer) {
		this.writer = writer;
	}

	public CsvWriter getCsvWriter() {
		if (writer == null) {
			logger.error(CsvWriter.class.getSimpleName()
					+ " not set, can't proceed");
			throw new NullPointerException(CsvWriter.class.getSimpleName()
					+ " not set, can't proceed");
		}
		return writer;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void destroy() {
		super.destroy();
		if (writer != null)
			writer.close();
	}

	@Override
	public void setDataFieldNames(List<String> aggList) {
		writeHeader(aggList.toArray(new String[0]));
	}
}
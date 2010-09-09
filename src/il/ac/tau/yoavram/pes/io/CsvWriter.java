package il.ac.tau.yoavram.pes.io;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

public class CsvWriter implements Closeable {
	private static final Logger logger = Logger.getLogger(CsvWriter.class);
	private static final char ROW_SEPARATOR = '\n';
	private static final char VALUE_SEPARATOR = ',';

	private String filename = null;
	private String extension = "csv";
	private Date time;

	private FileWriter writer = null;
	private boolean newLine = true;
	private File file = null;

	public CsvWriter() {
		super();
	}

	public void init() throws IOException {
		if (getFilename().isEmpty()) {
			throw new IllegalArgumentException("Filename is empty");
		}
		String fname = getFilename();
		if (getTime() != null) {
			fname = fname + "." + TimeUtils.formatDate(getTime());
		}

		if (!getExtension().isEmpty()) {
			fname = fname + "." + getExtension();
		}
		file = new File(fname);
		Files.createParentDirs(file);
		writer = new FileWriter(fname);
		logger.info("Writing to file " + file.getAbsolutePath());
	}

	public void writeRow(String[] row) {
		writeRow((Object[]) row);
	}

	public void writeRow(Object[] row) {
		for (int i = 0; i < row.length; i++) {
			writeCell(row[i]);
		}
		newRow();
	}

	public void writeCell(String data) {
		try {
			if (!newLine) {
				writer.append(VALUE_SEPARATOR);
			}
			writer.append(data);
		} catch (IOException e) {
			logger.warn("Failed writing value to cell in file "
					+ file.getAbsolutePath() + ": " + e);
		}
		newLine = false;
	}

	public void writeCell(Object data) {
		writeCell(data.toString());
	}

	public void newRow() {
		try {
			writer.append(ROW_SEPARATOR);
		} catch (IOException e) {
			logger.warn("Failed writing new line in file "
					+ file.getAbsolutePath() + ": " + e);
		}
		newLine = true;

	}

	public void close() {
		Closeables.closeQuietly(writer);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

}

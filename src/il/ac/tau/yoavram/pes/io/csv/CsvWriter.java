package il.ac.tau.yoavram.pes.io.csv;

import il.ac.tau.yoavram.pes.io.gzip.GZIPFileWriter;
import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

public class CsvWriter implements Closeable {
	private static final Logger logger = Logger.getLogger(CsvWriter.class);
	private static final char ROW_SEPARATOR = '\n';
	private static final char VALUE_SEPARATOR = ',';
	private static final String COMPRESSION_EXTENSION = ".gz";
	private static final String CSV_EXTENISON = ".csv";
	private static final String DOT = ".";
	private static final String TEMP_DIR = "tmp";

	private String directory = null;
	private String filename = null;
	private String extension = CSV_EXTENISON;
	private Date time;
	private boolean compress = false;
	private boolean tmpFile = false;

	private OutputStreamWriter writer = null;
	private boolean newLine = true;
	private File file = null;

	public CsvWriter() {
	}

	public void init() throws IOException {
		if (isWriteTempFile()) {
			File dir = new File(getDirectory() + File.separator + TEMP_DIR);
			Files.createParentDirs(dir);
			dir.mkdir();
			dir.deleteOnExit();
			file = File.createTempFile(produceFilenamePrefix() + DOT,
					produceFilenameSuffix(), dir);
			file.deleteOnExit();
			logger.info("Writing to temporary file " + file.getAbsolutePath());
		} else {
			file = new File(getDirectory() + File.separator
					+ produceFilenamePrefix() + produceFilenameSuffix());
			logger.info("Writing to file " + file.getAbsolutePath());
		}

		Files.createParentDirs(file);
		if (isCompress()) {
			writer = new GZIPFileWriter(file);
		} else {
			writer = new FileWriter(file);
		}

	}

	private String produceFilenamePrefix() {
		if (getFilename().isEmpty()) {
			throw new IllegalArgumentException("Filename is empty");
		}
		String fname = getFilename();
		if (getTime() != null) {
			fname = fname + "." + TimeUtils.formatDate(getTime());
		}

		return fname;
	}

	private String produceFilenameSuffix() {
		String suffix = getExtension();
		if (isCompress()) {
			suffix = suffix + COMPRESSION_EXTENSION;
		}
		return suffix;
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

	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			logger.warn(e);
		}
	}

	@Override
	public void close() {
		if (writer != null) {
			if (!newLine) {
				newRow();
			}
			Closeables.closeQuietly(writer);
		}
		if (isWriteTempFile()) {
			File finalFile = new File(getDirectory() + File.separator
					+ produceFilenamePrefix() + produceFilenameSuffix());
			try {
				Files.copy(file, finalFile);
				logger.info("Copied temporary file to "
						+ finalFile.getAbsolutePath());
			} catch (IOException e) {
				logger.warn("Failed copying temp file: " + e);
			}
		}
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setExtension(String extension) {
		if (!extension.startsWith(DOT))
			extension = DOT + extension;
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

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public boolean isCompress() {
		return compress;
	}

	public void setWriteTempFile(boolean tmpFile) {
		this.tmpFile = tmpFile;
	}

	public boolean isWriteTempFile() {
		return tmpFile;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
}

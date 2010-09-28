package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.LineReader;

public class CsvReader {
	private static final Logger logger = Logger.getLogger(CsvReader.class);
	private static final String VALUE_SEPARATOR = ",";
	private static final String EMPTY_STRING = "";
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final String[][] EMPTY_STRING_MATRIX = new String[0][0];

	private Splitter splitter = Splitter.on(VALUE_SEPARATOR);
	private String filename = null;
	private File file = null;
	private Readable readable = null;
	private LineReader reader = null;
	private boolean header = true;
	private String firstLine;
	private String[] firstRow;

	public void init() throws FileNotFoundException {
		if (filename != null) {
			file = new File(getFilename());
		}
		if (file != null) {
			readable = new FileReader(file);
			logger.info("Reading from " + file.getAbsolutePath());
		}
		if (readable != null) {
			reader = new LineReader(readable);
		} else {
			throw new NullPointerException(
					"Either file, filename or readable must be set");
		}
		if (isHeader()) {
			try {
				firstLine = nextLine();
				firstRow = split(firstLine);
			} catch (IOException e) {
				logger.fatal(e);
			}
		} else {
			firstLine = EMPTY_STRING;
			firstRow = EMPTY_STRING_ARRAY;
		}
	}

	public String[] split(String line) {
		return line.split(VALUE_SEPARATOR);
	}

	public String nextLine() throws IOException {
		return reader.readLine();
	}

	public String[] nextRow() throws IOException {
		String line = reader.readLine();
		if (line == null) {
			return null;
		} else {
			return split(line);
		}
	}

	public String lastLine() throws IOException {
		String line, lastLine = null;
		while ((line = nextLine()) != null) {
			lastLine = line;
		}
		return lastLine;
	}

	public String[] lastRow() throws IOException {
		return split(lastLine());
	}

	public String[] firstRow() {
		return firstRow;
	}

	public String[][] allRows() throws IOException {
		List<String[]> list = Lists.newArrayList();
		String[] row = null;
		while ((row = nextRow()) != null) {
			list.add(row);
		}
		return list.toArray(EMPTY_STRING_MATRIX);
	}

	public int numberOfRows() throws IOException {
		int n = 0;
		while (nextRow() != null) {
			n++;
		}
		return n;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setReadable(Readable readable) {
		this.readable = readable;
	}

	public Readable getReadable() {
		return readable;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public boolean isHeader() {
		return header;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}

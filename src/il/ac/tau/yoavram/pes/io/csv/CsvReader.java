package il.ac.tau.yoavram.pes.io.csv;

import il.ac.tau.yoavram.pes.io.gzip.GZIPFileReader;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.common.io.LineReader;

public class CsvReader implements Closeable {
	private static final Logger logger = Logger.getLogger(CsvReader.class);
	private static final String VALUE_SEPARATOR = ",";
	private static final String EMPTY_STRING = "";
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final String[][] EMPTY_STRING_MATRIX = new String[0][0];

	private String filename = null;
	private File file = null;
	private Readable readable = null;
	private LineReader reader = null;
	private boolean header = true;
	private String firstLine;
	private String[] firstRow;
	private Pattern p = Pattern.compile(VALUE_SEPARATOR);
	private boolean compressed = false;

	public CsvReader() {
	}

	public CsvReader(String filename, boolean isHeader) {
		this();
		setFilename(filename);
		setHeader(isHeader);
		init();
	}

	public CsvReader(File file, boolean isHeader) {
		this();
		setFile(file);
		setHeader(isHeader);
		init();
	}

	public CsvReader(Readable readable, boolean isHeader) {
		this();
		setReadable(readable);
		setHeader(isHeader);
		init();
	}

	public void init() {
		if (filename != null) {
			file = new File(getFilename());
		}
		if (file != null) {
			try {
				if (isCompressed()) {
					readable = new GZIPFileReader(file);
				} else {
					readable = new FileReader(file);
				}
				// logger.info("Reading from " + file.getAbsolutePath());
			} catch (FileNotFoundException e) {
				logger.error("Failed opening file " + file.getAbsolutePath()
						+ ": " + e);
			} catch (IOException e) {
				logger.error("Failed opening compressed file "
						+ file.getAbsolutePath() + ": " + e);
			}

		}
		if (readable != null) {
			reader = new LineReader(readable);
		} else {
			throw new NullPointerException(
					"Either file, filename or readable must be set");
		}
		if (isHeader()) {
			firstLine = nextLine();
			firstRow = split(firstLine);
		} else {
			firstLine = EMPTY_STRING;
			firstRow = EMPTY_STRING_ARRAY;
		}
	}

	public void close() {
		if (readable instanceof Closeable && readable != null) {
			Closeables.closeQuietly((Closeable) readable);
		}
		file = null;
		readable = null;
		reader = null;
	}

	public String[] split(String line) {
		if (line == null)
			return null;
		return p.split(line, -1);
	}

	/**
	 * returns null if no more lines.
	 * 
	 * @return
	 */
	public String nextLine() {
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			logger.error(e);
		}
		return line;
	}

	/**
	 * returns null if no more rows.
	 * 
	 * @return
	 */
	public String[] nextRow() {
		String line = nextLine();
		if (Strings.isNullOrEmpty(line))
			return null;
		else
			return split(line);

	}

	public String lastLine() {
		String line, lastLine = null;
		while ((line = nextLine()) != null) {
			lastLine = line;
		}
		return lastLine;
	}

	public String[] lastRow() {
		return split(lastLine());
	}

	public String[] firstRow() {
		return firstRow;
	}

	public String[][] allRows() {
		List<String[]> list = Lists.newArrayList();
		String[] row = null;
		while ((row = nextRow()) != null) {
			list.add(row);
		}
		return list.toArray(EMPTY_STRING_MATRIX);
	}

	public int numberOfRows() {
		int n = 0;
		while (nextLine() != null) {
			n++;
		}
		return n;
	}

	public String getFilename() {
		if (filename == null && file != null)
			return file.getAbsolutePath();
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

	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	public boolean isCompressed() {
		return compressed;
	}
}

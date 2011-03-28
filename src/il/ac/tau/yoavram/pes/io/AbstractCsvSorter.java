package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Comparator;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.TreeMultimap;

public abstract class AbstractCsvSorter<T> {
	private static final Logger logger = Logger.getLogger(AbstractCsvSorter.class);

	private CsvReader reader = null;
	private CsvWriter writer = null;

	public void sort(String path) throws IOException{
		File input = new File(path);
		if (input.isFile()) {
			sortFile(input);
		} else if (input.isDirectory()) {
			sortDir(input);
		} else {
			logger.info("input is neither file nor directory: "
					+ input.getAbsolutePath());
		}
	}

	public void sortDir(File dir) throws IOException {
		logger.info("Sorting in dir " + dir.getAbsolutePath());
		FileFilter filter = FileFilterUtils.suffixFileFilter("csv");
		for (File file : dir.listFiles(filter)) {
			sortFile(file);
		}
	}

	public void sortFile(File file) throws IOException {
		logger.info("sorting file " + file.getAbsolutePath());
		CsvReader reader = new CsvReader(file, true);
		reader.init();
		CsvWriter writer = new CsvWriter();
		writer.setDirectory(reader.getFile().getParent());
		writer.setFilename("S" + reader.getFile().getName().replace(".csv", ""));
		writer.init();
		setReader(reader);
		setWriter(writer);
		sort();
		reader.close();
		writer.close();
	}

	public void sort() {
		TreeMultimap<T, String[]> map = createMap();

		logger.info("Reading from " + reader.getFilename());
		for (String[] row = reader.nextRow(); row != null; row = reader
				.nextRow()) {
			map.put(getField(row), row);
		}

		logger.info("Writing to " + writer.getFilename()
				+ " in ascending order");

		writer.writeRow(reader.firstRow());

		for (String[] row : map.values()) {
			writer.writeRow(row);
		}
	}

	protected abstract TreeMultimap<T, String[]> createMap();

	protected abstract T getField(String[] row) ;

	public void setReader(CsvReader reader) {
		this.reader = reader;
	}

	public CsvReader getReader() {
		return reader;
	}

	public void setWriter(CsvWriter writer) {
		this.writer = writer;
	}

	public CsvWriter getWriter() {
		return writer;
	}

	public static class StringArrayComparator implements Comparator<String[]> {

		@Override
		public int compare(String[] o1, String[] o2) {
			int cmp = 0;
			for (int i = 0; i < o1.length && i < o2.length && cmp == 0; i++) {
				cmp = o1[i].compareTo(o2[i]);
			}
			return cmp;
		}

		public static Comparator<String[]> create() {
			return new StringArrayComparator();
		}
	}
}

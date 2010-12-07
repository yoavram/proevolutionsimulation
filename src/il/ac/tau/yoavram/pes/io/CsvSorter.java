package il.ac.tau.yoavram.pes.io;

import il.ac.tau.yoavram.pes.utils.NumberUtils;
import il.ac.tau.yoavram.pes.utils.SprinUtils;

import java.math.BigDecimal;
import java.util.Comparator;

import org.apache.log4j.Logger;

import com.google.common.collect.TreeMultimap;

public class CsvSorter {
	private static final Logger logger = Logger.getLogger(CsvSorter.class);

	private int sortByColumn = 0;
	// private boolean descending = true;
	private CsvReader reader = null;
	private CsvWriter writer = null;

	public static void main(String[] args) {
		CsvSorter sorter = SprinUtils.getBean(args, CsvSorter.class);
		sorter.sort();
	}
	
	public void sort() {
		TreeMultimap<BigDecimal, String[]> map = TreeMultimap.create(
				NumberUtils.createNaturalBigDecimalComparator(),
				StringArrayComparator.createStringArrayComparator());

		logger.info("Reading from " + reader.getFilename());
		logger.info("Sorting by column: " + reader.firstRow()[sortByColumn]);
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

	private BigDecimal getField(String[] row) {
		return new BigDecimal(row[sortByColumn]);
	}

	public void setSortByColumn(int sortByColumn) {
		this.sortByColumn = sortByColumn;
	}

	public int getSortByColumn() {
		return sortByColumn;
	}

	/*
	 * public void setDescending(boolean descending) { this.descending =
	 * descending; }
	 * 
	 * public boolean isDescending() { return descending; }
	 */

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

		public static Comparator<String[]> createStringArrayComparator() {
			return new StringArrayComparator();
		}
	}
}

package il.ac.tau.yoavram.pes.io;

import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.math.BigDecimal;

import com.google.common.collect.TreeMultimap;

public class CsvSorter extends AbstractCsvSorter<BigDecimal> {
	private int column = 0;

	@Override
	protected TreeMultimap<BigDecimal, String[]> createMap() {
		return TreeMultimap.create(
				NumberUtils.createNaturalBigDecimalComparator(),
				AbstractCsvSorter.StringArrayComparator.create());
	}

	@Override
	protected BigDecimal getField(String[] row) {
		return new BigDecimal(row[getColumn()]);
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

}

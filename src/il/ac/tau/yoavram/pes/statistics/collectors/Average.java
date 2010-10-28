package il.ac.tau.yoavram.pes.statistics.collectors;

import il.ac.tau.yoavram.pes.io.CsvReader;

import java.math.BigDecimal;
import java.math.MathContext;

import com.google.common.base.Strings;

public class Average implements Collector {
	private int column = 0;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String collect(CsvReader reader) {
		String[] row = null;
		String ret = null;
		BigDecimal sum = BigDecimal.ZERO;
		int count = 0;
		while ((row = reader.nextRow()) != null) {
			if (row.length > column && !Strings.isNullOrEmpty(row[column])) {
				BigDecimal data = new BigDecimal(row[column]);
				sum = sum.add(data);
				count++;
			}
		}
		if (count > 0) {
			BigDecimal avg = sum.divide(new BigDecimal(count),
					MathContext.DECIMAL128);
			ret = avg.toString();
		}
		return ret;
	}
}

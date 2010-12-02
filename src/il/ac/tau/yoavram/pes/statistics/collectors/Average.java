package il.ac.tau.yoavram.pes.statistics.collectors;

import il.ac.tau.yoavram.pes.io.CsvReader;

import java.math.BigDecimal;
import java.math.MathContext;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public class Average implements Collector {
	private static final Logger logger = Logger.getLogger(Average.class);
	private int column = 0;
	private int rows = 0;

	@Override
	public String collect(CsvReader reader) {
		String[] row = null;
		String ret = null;
		BigDecimal sum = BigDecimal.ZERO;
		int count = 0;
		while ((row = reader.nextRow()) != null) {
			if (row.length > column && !Strings.isNullOrEmpty(row[column])) {
				try {
					BigDecimal data = new BigDecimal(row[column]);
					sum = sum.add(data);
				} catch (NumberFormatException e) {
					logger.error("couldn't parse '" + row[column] + "' in "
							+ reader.getFilename() + ", row " + count);
					return ret;
				}
				count++;
			}
		}
		if ((rows == 0 && count > 0) || (rows == count)) {
			BigDecimal avg = sum.divide(new BigDecimal(count),
					MathContext.DECIMAL128);
			ret = avg.toString();
		}
		return ret;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

}

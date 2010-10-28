package il.ac.tau.yoavram.pes.statistics.collectors;

import il.ac.tau.yoavram.pes.io.CsvReader;

public class LastRow implements Collector {
	private int ticks = 0;
	private int ticksColumn = 0;
	private int column = 0;

	@Override
	public String collect(CsvReader reader) {
		String[] row = reader.lastRow();
		String ret = null;
		if (ticks != 0) {
			if (row != null && row.length > column
					&& Integer.valueOf(row[ticksColumn]).equals(ticks)) {
				ret = row[column];
			}
		} else {
			if (row != null && row.length > column) {
				ret = row[column];
			}
		}
		return ret;
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

}

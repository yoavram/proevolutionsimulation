package il.ac.tau.yoavram.pes.statistics.collectors;

import il.ac.tau.yoavram.pes.io.CsvReader;

public interface Collector {
	public String collect(CsvReader reader);
}

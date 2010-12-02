package il.ac.tau.yoavram.pes.utils;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.io.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

public class LineSizer {
	public static void main(String[] args) throws IOException {
		String filename = args[0];
		File file = new File(filename);
		CsvReader reader = new CsvReader(file, true);
		CsvWriter writer = new CsvWriter();
		writer.setDirectory(file.getParent());
		writer.setFilename("_" + file.getName().replace(".csv", ""));
		writer.init();

		MathContext mc = new MathContext(100);
		// write header
		writer.writeRow(reader.firstRow());

		for (String[] row = reader.nextRow(); row != null; row = reader
				.nextRow()) {
			BigDecimal d = new BigDecimal(row[row.length - 1], mc);
			row[row.length - 1] = d.toString();
			writer.writeRow(row);
		}
		reader.close();
		writer.close();

	}
}

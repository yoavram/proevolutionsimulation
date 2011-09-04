package il.ac.tau.yoavram.pes.io.csv;

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

		MathContext mc = new MathContext(400);
		// write header
		writer.writeRow(reader.firstRow());

		for (String[] row = reader.nextRow(); row != null; row = reader
				.nextRow()) {
			String s = row[row.length - 1];
			s = s.substring(0, 500);
			BigDecimal d = new BigDecimal(s, mc);
			row[row.length - 1] = d.toString();
			writer.writeRow(row);
		}
		reader.close();
		writer.close();

	}
}

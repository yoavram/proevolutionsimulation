package il.ac.tau.yoavram.pes.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import il.ac.tau.yoavram.pes.io.csv.CsvReader;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class TestCsvReader {
	private static final String filename = "tests/test_csvreader.csv";

	private static final String[] header = new String[] { "tick",
			"sim fraction", "mean fitness", "max fitness", "min fitness",
			"stdev fitness", "mean mutation rate", "min mutation rate",
			"max mutation rate", "stdev mutation rate" };
	private static final String firstLine = "1000,0.5005,0.6555345730201563,0.6561000000000001,0.5314410000000002,0.006076608899169401,0.001000000000000782,0.0010,0.0010,NaN";
	private static final String lastLine = "40000000,1.0,0.7282698336001592,0.7290000000000001,0.5904900000000002,0.007284194691632503,0.001000000000000782,0.0010,0.0010,NaN";
	private static final String[] firstRow = new String[] { "1000", "0.5005",
			"0.6555345730201563", "0.6561000000000001", "0.5314410000000002",
			"0.006076608899169401", "0.001000000000000782", "0.0010", "0.0010",
			"NaN" };
	private static final String[] lastRow = new String[] { "40000000", "1.0",
			"0.7282698336001592", "0.7290000000000001", "0.5904900000000002",
			"0.007284194691632503", "0.001000000000000782", "0.0010", "0.0010",
			"NaN" };

	private CsvReader reader;

	@Before
	public void before() throws FileNotFoundException {
		reader = new CsvReader(filename, true);
	}

	@Test
	public void testClose() {
		reader.close();
	}

	@Test
	public void testSplit() {
		assertArrayEquals(new String[] { "little", "johnny", "ran", "in the",
				"morning", "to the", "gan" },
				reader.split("little,johnny,ran,in the,morning,to the,gan"));
	}

	@Test
	public void testNextLine() {
		assertEquals(firstLine, reader.nextLine());
	}

	@Test
	public void testNextRow() {
		assertArrayEquals(firstRow, reader.nextRow());
	}

	@Test
	public void testLastLine() {
		assertEquals(lastLine, reader.lastLine());
	}

	@Test
	public void testLastRow() {
		assertArrayEquals(lastRow, reader.lastRow());
	}

	@Test
	public void testFirstRow() {
		assertArrayEquals(header, reader.firstRow());
	}

	@Test
	public void testAllRows() {
		String[][] matrix = reader.allRows();

		assertEquals(40000, matrix.length);
		assertArrayEquals(firstRow, matrix[0]);
		assertArrayEquals(lastRow, matrix[matrix.length - 1]);
	}

	@Test
	public void testNumberOfRows() {
		assertEquals(40000, reader.numberOfRows());
	}

}

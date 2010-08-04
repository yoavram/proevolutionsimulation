package il.ac.tau.yoavram.pes.io;

import java.io.FileWriter;
import java.io.IOException;

//TODO - make this nice, test.
public class CsvWriter {
	private static final char ROW_SEPARATOR = '\n';
	private static final char VALUE_SEPARATOR = ',';
	private String filename = null;
	private FileWriter writer = null;
	private boolean newLine = true;

	// private int rows = 0;

	public CsvWriter(String filename) {
		super();
		this.filename = filename;
		// TODO check filename
		open();
	}

	public CsvWriter(String filename, String[] header) {
		this(filename);
		writeRow(header);
	}

	private void open() {
		try {
			writer = new FileWriter(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeRow(String[] row) {
		writeRow((Object[]) row);
	}

	public void writeRow(Object[] row) {
		for (int i = 0; i < row.length; i++) {
			writeCell(row[i]);
		}
		newRow();
	}

	public void writeCell(String data) {
		try {
			if (!newLine) {
				writer.append(VALUE_SEPARATOR);
			}
			writer.append(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newLine = false;
	}

	public void writeCell(Object data) {
		writeCell(data.toString());
	}

	public void newRow() {
		try {
			writer.append(ROW_SEPARATOR);
			/*
			 * if (rows++ % 1000 == 0) writer.flush();
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newLine = true;
	}

	public void close() {
		if (writer != null) {
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}

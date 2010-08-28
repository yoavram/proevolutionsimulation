package il.ac.tau.yoavram.pes.io;

import il.ac.tau.yoavram.pes.utils.TimeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

//TODO - make this nice, test.
public class CsvWriter {
	private static final char ROW_SEPARATOR = '\n';
	private static final char VALUE_SEPARATOR = ',';

	private String filename = null;
	private String extension = "csv";
	private Date time;

	private FileWriter writer = null;
	private boolean newLine = true;

	// private int rows = 0;

	public CsvWriter() {
		super();
	}

	public void init() {
		if (getFilename().isEmpty()) {
			throw new IllegalArgumentException("Filename is empty");
		}
		String fname = getFilename();
		if (getTime() != null) {
			fname = fname + "." + TimeUtils.formatDate(getTime());
		}
		if (!getExtension().isEmpty()) {
			fname = fname + "." + getExtension();
		}
		File file = new File(fname);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			writer = new FileWriter(fname);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newLine = true;

	}

	public void close() {
		if (writer != null) {
			try {
				writer.flush();
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

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

}

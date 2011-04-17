package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;

public class LinesCsvFileFilter implements FileFilter {
	private int lines = 0;
	private boolean header = true;
	private FileFilter csvFilter = FileFilterUtils.suffixFileFilter(".csv");

	@Override
	public boolean accept(File pathname) {
		if (!csvFilter.accept(pathname))
			return false;
		CsvReader reader = new CsvReader(pathname, isHeader());
		return reader.numberOfRows() == getLines();
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getLines() {
		return lines;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public boolean isHeader() {
		return header;
	}

}

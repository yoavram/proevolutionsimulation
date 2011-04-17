package il.ac.tau.yoavram.pes.io.csv;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;

public class TicksCsvFileFilter implements FileFilter {
	private int ticks = 0;
	private FileFilter csvFilter = FileFilterUtils.suffixFileFilter(".csv");

	@Override
	public boolean accept(File pathname) {
		if (!csvFilter.accept(pathname))
			return false;
		CsvReader reader = new CsvReader();
		reader.setFile(pathname);
		reader.init();
		return Integer.valueOf(reader.lastRow()[0]).equals(ticks);
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
}

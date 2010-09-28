package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TicksCsvFileFilter implements FileFilter {
	//TODO logger instead of syserr
	private int ticks = 0;

	@Override
	public boolean accept(File pathname) {
		CsvReader reader = new CsvReader();
		reader.setFile(pathname);
		try {
			reader.init();
			return Integer.valueOf(reader.lastRow()[0]) == ticks;

		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (NumberFormatException e) {
			System.err.println("Failed checking file "
					+ pathname.getAbsolutePath() + ": " + e);
		} catch (IOException e) {
			System.err.println("Failed checking file "
					+ pathname.getAbsolutePath() + ": " + e);
		}

		return true;
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
}

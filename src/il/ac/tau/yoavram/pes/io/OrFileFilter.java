package il.ac.tau.yoavram.pes.io;

import java.io.File;
import java.io.FileFilter;

public class OrFileFilter implements FileFilter {

	FileFilter filter1;
	FileFilter filter2;

	public OrFileFilter(FileFilter filter1, FileFilter filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	@Override
	public boolean accept(File pathname) {
		return filter1.accept(pathname) || filter2.accept(pathname);
	}

}

package il.ac.tau.yoavram.pes.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FilesDeleter {
	// TODO logger instead of syserr
	private static final String USAGE_STRING = "Usage: "
			+ FilesDeleter.class.getSimpleName()
			+ " <xml spring configuration>\n\txml spring configuration must be in classpath\n";

	private FileFilter filter;
	private String path;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println(USAGE_STRING);
			throw new IllegalArgumentException(USAGE_STRING);
		}

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				args[0]);
		FilesDeleter cleaner = ctx.getBean(FilesDeleter.class);
		System.out.println("Start to clean " + cleaner.getPath()
				+ " with filter "
				+ cleaner.getFileFilter().getClass().getSimpleName() + "?");
		System.in.read();
		cleaner.start();
	}

	public void start() throws FileNotFoundException {
		delete(new File(getPath()));
	}

	public void delete(File file) throws FileNotFoundException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				delete(f);
			}
		} else {
			if (!filter.accept(file)) {
				System.out.println("Deleting file " + file.getAbsolutePath());
				file.delete();
			}
		}
	}

	public void setFileFilter(FileFilter filter) {
		this.filter = filter;
	}

	public FileFilter getFileFilter() {
		return filter;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}

package il.ac.tau.yoavram.pes.utils;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.io.CsvWriter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataCollector {
	private static final String USAGE_STRING = "Usage: "
			+ FilesDeleter.class.getSimpleName()
			+ " <xml spring configuration>\n\txml spring configuration must be in classpath\n";
	private FileFilter filter = FileFilterUtils.orFileFilter(
			new WildcardFileFilter("pop*.csv"),
			FileFilterUtils.directoryFileFilter());
	private String path;
	private CsvWriter writer;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println(USAGE_STRING);
			throw new IllegalArgumentException(USAGE_STRING);
		}

		AbstractXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				args[0]);
		ctx.registerShutdownHook();
		DataCollector collector = ctx.getBean(DataCollector.class);
		System.out.println("Start to collect " + collector.getPath()
				+ " with filter "
				+ collector.getFilter().getClass().getSimpleName());
		collector.start();
	}

	public DataCollector() {
	}
	
	private void start() {
		collect(new File(getPath()));
	}

	private void collect(File path) {
		if (path.isDirectory()) {
			writer.flush();
			writer.newRow();
			writer.writeCell(path.getName());			
			File[] files= path.listFiles(getFilter());
			writer.writeCell(files.length);
			System.out.println(path.getName()+":"+files.length);
			for (File f : files) {
				collect(f);
			}
		} else {
			CsvReader reader = new CsvReader(path, true);
			String[] row = reader.lastRow();
			if (row != null && row.length > 0 && row[0].equals("40000000")) {
				writer.writeCell(row[1]);
			}
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setFilter(FileFilter filter) {
		// this.filter = new OrFileFilter(filter,
		// FileFilterUtils.directoryFileFilter());
		this.filter = filter;
	}

	public FileFilter getFilter() {
		return filter;
	}

	public CsvWriter getWriter() {
		return writer;
	}

	public void setWriter(CsvWriter writer) {
		this.writer = writer;
	}
}

package il.ac.tau.yoavram.pes.utils;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.io.CsvWriter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;

public class DataCollector {
	private static final String USAGE_STRING = "Usage: "
			+ DataCollector.class.getSimpleName()
			+ " <xml spring configuration>\n\txml spring configuration must be in classpath\n";
	private static final int TICKS_COLUMN = 0;
	private FileFilter filter = FileFilterUtils.orFileFilter(
			new WildcardFileFilter("pop*.csv"),
			FileFilterUtils.directoryFileFilter());
	private String path;
	private CsvWriter writer;
	private int ticks;
	private int column;

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

	private List<String> values;

	private void collect(File path) {
		if (path.isDirectory()) {
			values = Lists.newArrayList();
			System.out.println(path.getName());
			for (File f : path.listFiles(getFilter())) {
				collect(f);
			}
			writer.flush();
			if (values != null && values.size() > 0) {
				writer.writeCell(path.getName());
				writer.writeCell(values.size());
				writer.writeRow(values.toArray());
				values = null;
			}
		} else {
			CsvReader reader = new CsvReader(path, true);
			String[] row = reader.lastRow();
			if (row != null && row.length > 0
					&& Integer.valueOf(row[TICKS_COLUMN]).equals(ticks)) {
				values.add(row[column]);
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

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}

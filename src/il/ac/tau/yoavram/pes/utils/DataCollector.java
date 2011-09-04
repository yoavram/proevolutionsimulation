package il.ac.tau.yoavram.pes.utils;

import il.ac.tau.yoavram.pes.io.csv.CsvCollector;
import il.ac.tau.yoavram.pes.io.csv.CsvReader;
import il.ac.tau.yoavram.pes.io.csv.CsvWriter;
import il.ac.tau.yoavram.pes.statistics.collectors.Collector;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.google.common.collect.Lists;

/**
 * @deprecated Use {@link CsvCollector}
 * @author yoavram
 * 
 */
@Deprecated
public class DataCollector {
	private FileFilter filter = FileFilterUtils.orFileFilter(
			new WildcardFileFilter("pop*.csv"),
			FileFilterUtils.directoryFileFilter());

	private String path;
	private CsvWriter writer;
	private Collector collector;

	public static void main(String[] args) throws IOException {
		DataCollector collector = SpringUtils.getBean(args, DataCollector.class);
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
			String val = collector.collect(reader);
			if (val != null)
				values.add(val);
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

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public Collector getCollector() {
		return collector;
	}
}

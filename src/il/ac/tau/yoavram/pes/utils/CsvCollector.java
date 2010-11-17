package il.ac.tau.yoavram.pes.utils;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.io.CsvWriter;
import il.ac.tau.yoavram.pes.statistics.collectors.Collector;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;

import com.google.common.collect.Lists;

@SuppressWarnings("rawtypes")
public class CsvCollector extends DirectoryWalker {
	private FileFilter filter;
	private CsvWriter writer;
	private Collector collector;
	private List<String> values;
	private String path;

	public static void main(String[] args) throws IOException {
		CsvCollector collector = SprinUtils.getBean(args, CsvCollector.class);
		System.out.println("Start to collect " + collector.getPath());
		collector.start();
	}

	public void start() {
		File startDirectory = new File(getPath());
		try {
			values = Lists.newArrayList();
			walk(startDirectory, null);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		if (getFilter().accept(file)) {
			CsvReader reader = new CsvReader(file, true);
			String val = getCollector().collect(reader);
			if (val != null) {
				values.add(val);
			}
		}
	}

	@Override
	protected void handleStart(File startDirectory, Collection results)
			throws IOException {
		System.out.println(startDirectory.getName());

	}

	@Override
	protected void handleEnd(Collection results) throws IOException {
		writer.close();
	}

	@Override
	protected void handleDirectoryStart(File directory, int depth,
			Collection results) throws IOException {

		System.out.println(directory.getName());
	}

	@Override
	protected void handleDirectoryEnd(File directory, int depth,
			Collection results) throws IOException {
		writer.flush();
		if (values != null && values.size() > 0) {
			writer.writeCell(directory.getName());
			writer.writeCell(values.size());
			writer.writeRow(values.toArray());
			values.clear();
		}
	}

	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}

	public FileFilter getFilter() {
		return filter;
	}

	public void setWriter(CsvWriter writer) {
		this.writer = writer;
	}

	public CsvWriter getWriter() {
		return writer;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	public Collector getCollector() {
		return collector;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}

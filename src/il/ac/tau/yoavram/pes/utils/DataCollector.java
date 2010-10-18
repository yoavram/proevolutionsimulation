package il.ac.tau.yoavram.pes.utils;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.io.CsvWriter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.base.Strings;
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
	private Collector collector;

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

	public interface Collector {
		public String collect(CsvReader reader);
	}

	public static class LastRow implements Collector {
		private int ticks = 0;
		private int column = 0;

		@Override
		public String collect(CsvReader reader) {
			String[] row = reader.lastRow();
			String ret = null;
			if (ticks != 0) {
				if (row != null && row.length > column
						&& Integer.valueOf(row[TICKS_COLUMN]).equals(ticks)) {
					ret = row[column];
				}
			} else {
				if (row != null && row.length > column) {
					ret = row[column];
				}
			}
			return ret;
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

	public static class Average implements Collector {
		private int column = 0;

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		@Override
		public String collect(CsvReader reader) {
			String[] row = null;
			String ret = null;
			BigDecimal sum = BigDecimal.ZERO;
			int count = 0;
			while ((row = reader.nextRow()) != null) {
				if (row.length > column && !Strings.isNullOrEmpty(row[column])) {
					BigDecimal data = new BigDecimal(row[column]);
					sum = sum.add(data);
					count++;
				}
			}
			if (count > 0) {
				BigDecimal avg = sum.divide(new BigDecimal(count),
						MathContext.DECIMAL128);
				ret = avg.toString();
			}
			return ret;
		}
	}
}

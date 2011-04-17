package il.ac.tau.yoavram.simba.difeq;

import il.ac.tau.yoavram.pes.io.csv.CsvReader;
import il.ac.tau.yoavram.pes.io.csv.CsvWriter;
import il.ac.tau.yoavram.pes.statistics.listeners.ChartDrawer;
import il.ac.tau.yoavram.pes.statistics.listeners.CsvWriterListener;
import il.ac.tau.yoavram.pes.statistics.listeners.DataListener;
import il.ac.tau.yoavram.simba.difeq.DifeqCsvSorter.DifeqDataSet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.jfree.chart.axis.LogarithmicAxis;

import com.google.common.collect.Lists;

public class DifeqFigurer {
	private static final Logger logger = Logger.getLogger(DifeqFigurer.class);
	private CsvReader reader;

	public static void main(String[] args) throws IOException {

		File file = new File(args[0]);
		logger.info("figuring file " + file.getAbsolutePath());
		CsvReader reader = new CsvReader(file, true);
		reader.init();

		DifeqFigurer fig = new DifeqFigurer();
		fig.setReader(reader);
		fig.figure();
		reader.close();
	}

	public void figure() throws IOException {
		BigDecimal e60 = new BigDecimal("1e60");
		System.out.println(e60.toString());
		BigDecimal[] a1 = new BigDecimal[26];
		DifeqDataSet dataset = null;
		logger.info("Reading from " + reader.getFilename());
		for (String[] row = reader.nextRow(); row != null; row = reader
				.nextRow()) {
			dataset = new DifeqDataSet(row, 10);
			a1[dataset.pi] = dataset.meanW;
		}
		/*
		 * BigDecimal[] a2 = Arrays.copyOfRange(a1, 2, a1.length);
		 * Arrays.sort(a2, NumberUtils.createNaturalBigDecimalComparator());
		 * BigDecimal m = a2[0];
		 */
		BigDecimal nm = a1[a1.length - 1];
		DataListener listener = createWriter(dataset);
		reader.close();
		// double[] a2 = new double[a1.length];
		for (int pi = 0; pi < a1.length; pi++) {
			BigDecimal b = a1[pi].subtract(nm);
			b = b.multiply(e60);
			System.out.println(pi + ": " + b.toString());
			// if (b.compareTo(BigDecimal.ZERO) > 0)
			listener.listen(new Number[] { pi, b });
			// a2[pi] = b.doubleValue();
		}

		listener.close();

	}

	public DataListener createWriter(DifeqDataSet dataset) throws IOException {
		CsvWriter w = new CsvWriter();
		w.setDirectory(reader.getFile().getParent());
		w.setFilename("scaled_"
				+ reader.getFile().getName().replace(".csv", ""));
		w.init();

		CsvWriterListener l = new CsvWriterListener();
		l.setCsvWriter(w);
		l.setDataFieldNames(Lists.newArrayList("pi", "meanW"));
		l.init();

		return l;
	}

	public DataListener createDrawer(DifeqDataSet dataset) {
		ChartDrawer drawer = new ChartDrawer();
		drawer.setDataFieldNames(Lists.newArrayList("pi", "meanW"));
		String name = String.format("n.%d.s.%f.tau.%f.gamma.%f.phi.%f",
				dataset.n, dataset.s, dataset.tau, dataset.gamma, dataset.phi);
		drawer.setFilename(reader.getFile().getParent() + File.separator + name);
		drawer.setShowApplet(true);
		drawer.init();
		drawer.getChart().getXYPlot()
				.setRangeAxis(new LogarithmicAxis("mean w"));
		drawer.getChart().getXYPlot().getRangeAxis()
				.setTickLabelsVisible(false);
		logger.info("Writing to " + drawer.getFilename());
		return drawer;
	}

	public void setReader(CsvReader reader) {
		this.reader = reader;
	}

	public CsvReader getReader() {
		return reader;
	}

	public static class KeyComparator implements Comparator<DifeqDataSet> {

		@Override
		public int compare(DifeqDataSet o1, DifeqDataSet o2) {
			int cmp = o1.n.compareTo(o2.n);
			if (cmp == 0) {
				cmp = o1.s.compareTo(o2.s);
			}
			if (cmp == 0) {
				cmp = o1.tau.compareTo(o2.tau);
			}
			if (cmp == 0) {
				cmp = o1.gamma.compareTo(o2.gamma);
			}
			if (cmp == 0) {
				cmp = o1.phi.compareTo(o2.phi);
			}
			return cmp;
		}
	}

	public static class ValueComparator implements Comparator<String[]> {

		@Override
		public int compare(String[] o1, String[] o2) {
			return o1[3].compareTo(o2[3]);
		}

	}
}

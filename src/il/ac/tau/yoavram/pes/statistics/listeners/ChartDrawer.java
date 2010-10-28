package il.ac.tau.yoavram.pes.statistics.listeners;

import il.ac.tau.yoavram.pes.io.CsvReader;
import il.ac.tau.yoavram.pes.utils.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class ChartDrawer implements DataListener {
	private static final Logger logger = Logger.getLogger(ChartDrawer.class);

	private static final String DEFAULT_TITLE = "proevolutions simulation";
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static final String DEFAULT_OUTPUT_FILE_EXTENSION = ".png";
	private static final String DEFAULT_INPUT_FILE_EXTENSION = ".csv";
	private static final FilenameFilter DIR_OR_INPUT_FILE_FILTER = FileFilterUtils
			.orFileFilter(FileFilterUtils
					.suffixFileFilter(DEFAULT_INPUT_FILE_EXTENSION),
					FileFilterUtils.directoryFileFilter());

	private ApplicationFrame appFrame;
	private JFreeChart chart;
	private XYSeriesCollection dataset;
	private ChartPanel chartPanel;
	private String filename;
	// private List<String> dataFieldNames;
	private boolean showApplet = true;

	public ChartDrawer() {
		dataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart(DEFAULT_TITLE, null, null,
				dataset, PlotOrientation.VERTICAL, true, true, true);
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);
		chart.getXYPlot().setRenderer(renderer);
	}

	public void init() {
		if (isShowApplet()) {
			logger.debug("Creating applet for "
					+ ChartDrawer.class.getSimpleName()
					+ Character.SPACE_SEPARATOR + getTitle());
			chartPanel = new ChartPanel(chart, true, true, true, true, true);
			chartPanel.setSize(WIDTH, HEIGHT);
			appFrame = new ApplicationFrame(chart.getTitle().getText());
			appFrame.setSize(WIDTH, HEIGHT);
			appFrame.setContentPane(chartPanel);
			appFrame.setAlwaysOnTop(true);
			appFrame.pack();
			RefineryUtilities.centerFrameOnScreen(appFrame);
			appFrame.setVisible(true);
		}
	}

	@Override
	public void listen(Number[] data) {
		for (int i = 1; i < data.length; i++) {
			dataset.getSeries(i - 1).add(data[0], data[i]);
		}
	}

	@Override
	public void close() throws IOException {
		saveToFile();
	}

	private void saveToFile() {
		ImageOutputStream imgStream = null;
		if (!Strings.isNullOrEmpty(getFilename())) {
			try {
				File file = new File(getFilename());
				imgStream = ImageIO.createImageOutputStream(file);
				logger.info(ChartDrawer.class.getSimpleName()
						+ Character.SPACE_SEPARATOR + getTitle()
						+ " output file: " + file.getAbsolutePath());
			} catch (IOException e) {
				logger.error("Failed creating image file '" + filename + "': "
						+ e);
			}
		}
		if (imgStream != null) {

			try {
				ImageIO.write(chart.createBufferedImage(WIDTH, HEIGHT),
						ImageFormat.PNG, imgStream);
				logger.info("Saved chart to file " + filename);
			} catch (FileNotFoundException e) {
				logger.error("Failed creating image file '" + filename + "': "
						+ e);
			} catch (IOException e) {
				logger.error("Failed writing chart to image file: " + e);
			} finally {
				if (imgStream != null) {
					try {
						imgStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	@Override
	public void setDataFieldNames(List<String> dataFieldNames) {
		// this.dataFieldNames = dataFieldNames;
		for (int i = 1; i < dataFieldNames.size(); i++) {
			XYSeries series = new XYSeries(dataFieldNames.get(i));
			dataset.addSeries(series);
		}
	}

	public void setTitle(String title) {
		chart.setTitle(title);
	}

	public String getTitle() {
		return chart.getTitle().getText();
	}

	public void setFilename(String filename) {
		if (!filename.endsWith(DEFAULT_OUTPUT_FILE_EXTENSION)) {
			filename = filename.concat(DEFAULT_OUTPUT_FILE_EXTENSION);
		}
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setShowApplet(boolean showApplet) {
		this.showApplet = showApplet;
	}

	public boolean isShowApplet() {
		return showApplet;
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err
					.println("Usage: java "
							+ ChartDrawer.class.getSimpleName()
							+ " <CSV filename> <lines to read | default is 0, meaning all lines> <Optional-output "
							+ DEFAULT_OUTPUT_FILE_EXTENSION
							+ " file> | <Directory of CSV files>");
			System.exit(1);
		}

		File input = new File(args[0]);

		int linesToRead = 0;

		if (input.isDirectory()) {
			readDir(input);

		} else {
			File output = null;
			if (args.length > 1) {
				linesToRead = Integer.valueOf(args[1]);
			}

			if (args.length > 2) {
				output = new File(args[2]);
			}
			readFile(input, linesToRead, output);
		}
	}

	public static void readDir(File dir) throws IOException {
		System.out.println("Iterating directory " + dir.getAbsolutePath());
		for (File file : dir.listFiles(DIR_OR_INPUT_FILE_FILTER)) {
			if (file.isDirectory()) {
				readDir(file);
			} else {
				File output = new File(FilenameUtils.removeExtension(file
						.getAbsolutePath()) + DEFAULT_OUTPUT_FILE_EXTENSION);
				readFile(file, 0, output);
			}
		}
	}

	public static void readFile(File input, int linesToRead, File output)
			throws IOException {
		System.out.println("Opening file " + input);
		CsvReader reader = new CsvReader();
		reader.setFile(input);
		reader.init();
		String[] row = reader.firstRow(); // header line
		List<String> headerFields = Lists.newArrayList(row);
		System.out.println("Extracted header: " + row);
		System.out.println("Starting " + ChartDrawer.class.getSimpleName());
		ChartDrawer drawer = new ChartDrawer();
		drawer.setTitle(input.getName());
		drawer.setDataFieldNames(headerFields);
		if (output != null) {
			System.out.println("Saving to file " + output.getAbsolutePath());
			drawer.setShowApplet(false);
			drawer.setFilename(output.getAbsolutePath());
		}
		drawer.init();

		int lines = 0;
		if (lines == 0) {
			linesToRead = Integer.MAX_VALUE;
			System.out.print("Reading all lines...");
		} else {
			System.out.print("Reading " + NumberUtils.formatNumber(linesToRead)
					+ " lines...");
		}
		while ((row = reader.nextRow()) != null && lines < linesToRead) {
			Double[] data = new Double[row.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = Double.valueOf(row[i]);
				if (Double.isInfinite(data[i]))
					data[i] = Double.NaN;
			}
			drawer.listen(data);
			if (lines++ % 100 == 0) {
				System.out.print('.');
			}
		}
		System.out.println();
		System.out.println(NumberUtils.formatNumber(lines) + " lines read");
		drawer.close();
	}
}

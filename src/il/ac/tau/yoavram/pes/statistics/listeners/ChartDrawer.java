package il.ac.tau.yoavram.pes.statistics.listeners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.LineReader;
import com.sun.image.codec.jpeg.ImageFormatException;

//TODO make the main method nicer.
public class ChartDrawer implements DataListener {
	private static final Logger logger = Logger.getLogger(ChartDrawer.class);

	private static final String COMMA = ",";
	private static final String DEFAULT_TITLE = "proevolutions simulation";
	private static final float QUALITY = 1.0f;
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static final String DEFAULT_FILE_EXTENSION = ".jpg";

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

	public void destroy() {
		if (!Strings.isNullOrEmpty(getFilename())) {
			try {
				ChartUtilities.saveChartAsJPEG(new File(filename), QUALITY,
						chart, WIDTH, HEIGHT);
				logger.info("Saved chart to file " + filename);
			} catch (ImageFormatException e) {
				logger.error("Failed formating chart to image: " + e);
			} catch (IOException e) {
				logger.error("Failed writing chart to image file: " + e);
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
		if (filename.endsWith(DEFAULT_FILE_EXTENSION)) {
			filename = filename.concat(DEFAULT_FILE_EXTENSION);
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
							+ DEFAULT_FILE_EXTENSION + " file>");
			System.exit(1);
		}
		System.out.println("Opening file " + args[0]);
		File file = new File(args[0]);
		LineReader reader = new LineReader(new FileReader(file));

		int linesToRead = 0;
		if (args.length > 1) {
			linesToRead = Integer.valueOf(args[1]);
		}
		if (linesToRead == 0) {
			linesToRead = Integer.MAX_VALUE;
		}

		String line = reader.readLine(); // header line
		List<String> headerFields = Lists.newArrayList(line.split(COMMA));
		System.out.println("Extracted header: " + line);
		System.out.println("Starting " + ChartDrawer.class.getSimpleName());
		ChartDrawer drawer = new ChartDrawer();
		drawer.setTitle(file.getName());
		drawer.setDataFieldNames(headerFields);
		if (args.length > 2) {
			System.out.println("Saving to file " + args[1]);
			drawer.setShowApplet(false);
			drawer.setFilename(args[2]);
		}
		drawer.init();

		int lines = 0;
		System.out.print("Reading " + linesToRead + " lines...");
		while ((line = reader.readLine()) != null && lines < linesToRead) {
			String[] sp = line.split(COMMA);
			Number[] data = new Number[sp.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = Double.valueOf(sp[i]);
			}
			drawer.listen(data);
			if (lines++ % 100 == 0) {
				System.out.print('.');
			}
		}
		System.out.println();
		System.out.println(lines + " lines read");
		drawer.destroy();
	}
}

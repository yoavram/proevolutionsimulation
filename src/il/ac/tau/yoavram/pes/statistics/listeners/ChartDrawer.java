package il.ac.tau.yoavram.pes.statistics.listeners;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ChartDrawer extends ThreadListener {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ChartDrawer.class);

	ApplicationFrame appFrame;
	private JFreeChart chart;
	private XYSeriesCollection dataset;
	private ChartPanel chartPanel;

	public ChartDrawer() {
		this(DEFAULT_TITLE);
	}

	public ChartDrawer(String title) {
		super(title + " " + ChartDrawer.class.getSimpleName());
		appFrame = new ApplicationFrame(title);
		dataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart(title, "tick", null, dataset,
				PlotOrientation.VERTICAL, true, true, true);
		chartPanel = new ChartPanel(chart, true, true, true, true, true);
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);
		chart.getXYPlot().setRenderer(renderer);
		appFrame.setContentPane(chartPanel);
		appFrame.setAlwaysOnTop(true);
	}

	@Override
	public void run() {
		// i want this part of the init to happen in the new thread
		appFrame.pack();
		RefineryUtilities.centerFrameOnScreen(appFrame);
		appFrame.setVisible(true);

		for (int i = 1; i < dataFieldNames.size(); i++) {
			XYSeries series = new XYSeries(dataFieldNames.get(i));
			dataset.addSeries(series);
		}
		super.run();
	}

	@Override
	protected void consume(Number[] data) {
		for (int i = 1; i < data.length; i++) {
			dataset.getSeries(i - 1).add(data[0], data[i]);
		}
	}

	public void setTitle(String title) {
		chart.setTitle(title);
	}
}

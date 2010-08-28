package il.ac.tau.yoavram.pes.statistics.listeners;

import java.awt.Dialog.ModalExclusionType;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ChartDrawer extends ApplicationFrame implements DataListener {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_TITLE = "proevolution simulation";
	private JFreeChart chart;
	private List<String> dataFieldNames;
	private XYSeriesCollection dataset;
	private ChartPanel chartPanel;

	private boolean first = true;

	public ChartDrawer() {
		this(DEFAULT_TITLE);
	}

	public ChartDrawer(String title) {
		super(title);
		dataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart(title, "tick", null, dataset,
				PlotOrientation.VERTICAL, true, true, true);
		chartPanel = new ChartPanel(chart, true, true, true, true, true);
		setContentPane(chartPanel);
	}

	public void init() {
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);
		chart.getXYPlot().setRenderer(renderer);

		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
		setAlwaysOnTop(true);
	}

	public void destroy() {
	}

	@Override
	public void listen(Number[] data) {
		if (first) {
			for (int i = 1; i < dataFieldNames.size(); i++) {
				XYSeries series = new XYSeries(dataFieldNames.get(i));
				dataset.addSeries(series);
			}
			first = false;
		}

		for (int i = 1; i < data.length; i++) {
			dataset.getSeries(i - 1).add(data[0], data[i]);
		}
	}

	@Override
	public void setDataFieldNames(List<String> aggList) {
		dataFieldNames = aggList;
	}

	public void setTitle(String title) {

		chart.setTitle(title);
	}
}

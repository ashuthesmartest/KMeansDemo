package tasty.minerals.kmeans.dataplotter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import tasty.minerals.kmeans.cluster.Cluster;

public class Plotter extends JFrame {
	private static final String title = "K-Means clustering test";
	private ArrayList<Cluster> clusterBasket = new ArrayList<Cluster>();
	private static final String[] COLORS = { "#f14952", "#fff546", "#4574f1",
			"#339933", "#cd6600", "#ff9e5e", "#9ed330", "#191970", "#11c530",
			"#ccff00", "#ae66d5", "#d739b7", "#11f5dc", "#0c3f96", "#a63435" };

	public Plotter(ArrayList<Cluster> data) {
		this.clusterBasket = data;
		final ChartPanel chartPanel = createDemoPanel();
		this.add(chartPanel, BorderLayout.CENTER);
	}

	private List<List<Float>> extractData(Cluster c) {
		List<List<Float>> xy = new ArrayList<List<Float>>();
		for (List<Float> f : c.getPopul()) {
			if (!c.getPopul().isEmpty()) {
				xy.add(Arrays.asList(f.get(0), f.get(1)));
			}
		}
		return xy;
	}

	private ChartPanel createDemoPanel() {
		JFreeChart jfreechart = ChartFactory.createScatterPlot(title, "X", "Y",
				plotData(), PlotOrientation.VERTICAL, true, true, false);

		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) xyPlot
				.getRenderer();

		xyPlot.setRenderer(rend);
		for (int i = 0; i != this.clusterBasket.size(); i++) {
			rend.setSeriesShape(i, ShapeUtilities.createRegularCross(1, 2));
			// rend.setSeriesShape(i, ShapeUtilities.createDiamond(3));
			rend.setSeriesShapesVisible(i, true);
			rend.setSeriesPaint(i, Color.decode(COLORS[i]));
		}

		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setRange(0, 20);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setVerticalTickLabels(true);

		NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setRange(0, 20);
		range.setTickUnit(new NumberTickUnit(1));

		return new ChartPanel(jfreechart);
	}

	private XYDataset plotData() {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		List<XYSeries> clusters = new ArrayList<XYSeries>();
		int name = 1;
		for (Cluster c : this.clusterBasket) {
			clusters.add(new XYSeries("Cluster " + name + " (" + c.getX()
					+ ", " + c.getY() + ")"));
			name++;
		}

		int i = 0;
		for (XYSeries s : clusters) {
			Cluster c = this.clusterBasket.get(i);
			for (List<Float> xy : extractData(c)) {
				s.add(xy.get(0), xy.get(1));
			}
			i++;
		}

		for (XYSeries s : clusters) {
			xySeriesCollection.addSeries(s);
		}
		return xySeriesCollection;
	}
}

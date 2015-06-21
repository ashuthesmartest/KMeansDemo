package tasty.minerals.kmeans;

import java.util.ArrayList;

import javax.swing.JFrame;

import tasty.minerals.kmeans.cluster.Cluster;
import tasty.minerals.kmeans.dataplotter.Plotter;
import tasty.minerals.kmeans.datareader.DataReader;

public class RunKmeans {

	public static void main(String[] args) {
		DataReader dr = new DataReader("/testdata/R15.txt");
		Classificator cl = new Classificator(dr.readData());
		ArrayList<Cluster> clusteredData = new ArrayList<Cluster>();

		/*
		 * Set the number of clusters "k" and iterations "runs". The more items
		 * in data you have the more clusters and iterations you may need.
		 */
		clusteredData = cl.runClassificator(15, 500);

		/* Plotting data */
		Plotter demo = new Plotter(clusteredData);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.pack();
		demo.setLocationRelativeTo(null);
		demo.setVisible(true);
	}
}

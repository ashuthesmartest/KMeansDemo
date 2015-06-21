package tasty.minerals.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import tasty.minerals.kmeans.cluster.Cluster;

public class Classificator {
	private String data;
	private List<List<String>> ls = new ArrayList<List<String>>();
	private ArrayList<Cluster> clusterBasket = new ArrayList<Cluster>();

	// Initializing dataset
	public Classificator(String dataset) {
		data = dataset;
	}

	/*
	 * We create an list of lists from file data by iterating on the lines of
	 * the splitted dataset, we get rid of the class column as well.
	 */
	private List<List<String>> mapDataset() {
		for (String line : data.split("\n|\r\n")) {
			ls.add(Arrays.asList(Arrays.copyOfRange(line.split(" |\t"), 0, 2)));
		}
		return ls;
	}

	// Calculating Euclidic distance given x1, y1, x2, y2
	private Double getDistance(Float a, Float b, Float c, Float d) {
		return Math.sqrt(Math.pow(a - c, 2) + Math.pow(b - d, 2));
	}

	// Creating a random index list of all data elements
	private List<Integer> getRandomCentroids(List<List<String>> ls) {
		List<Integer> nums = new ArrayList<Integer>();
		for (int a = 0; a != ls.size(); a++) {
			nums.add(a);
		}
		Collections.shuffle(nums);
		return nums;
	}

	/*
	 * Check if clusters changed after the previous run, if not, the optimal
	 * distribution has been found.
	 */
	private boolean areClustersComplete() {
		boolean isComplete = false;
		for (Cluster c : clusterBasket) {
			if (c.isChanged()) {
				isComplete = false;
			} else {
				isComplete = true;
			}
		}
		return isComplete;
	}

	// Analyzing candidates and assigning them to corresponding cluster.
	private void arrangeAmongClusters(List<Float> candi) {
		Float x2 = candi.get(0);
		Float y2 = candi.get(1);
		double dd = 0.0;
		Cluster tc = null;

		/*
		 * preventing canidates from getting into the wrong cluster due to dd ==
		 * 0.0 initially
		 */
		for (Cluster c : clusterBasket) {
			if (c.getXmean() != x2 && c.getYmean() != y2) {
				double d = getDistance(c.getXmean(), c.getYmean(), x2, y2);
				if (dd < d) {
					dd = d;
				}
			}
		}

		for (Cluster c : clusterBasket) {
			double d = getDistance(c.getXmean(), c.getYmean(), x2, y2);
			/*
			 * we need to check the distance from all centroids and only then
			 * assign the candidate to the correct cluster. That's why
			 * tc.add(x2, y2) is outside of the loop
			 */
			if (dd > d) {
				tc = c;
				dd = d;
			}
		}

		if (tc != null) {
			tc.add(x2, y2);
			tc.setChanged(true);
		}
	}

	private List<List> pickRandomCentroids(int k) {
		List<List> rndcents = new ArrayList<List>();
		// ls = mapDataset();

		int n = 0;
		for (int r : getRandomCentroids(ls)) {
			if (n == k) // limiting the number of sample centroids
				break;
			float xa = Float.valueOf(ls.get(r).get(0));
			float ya = Float.valueOf(ls.get(r).get(1));
			rndcents.add(Arrays.asList(xa, ya));
			n++;
		}
		return rndcents;
	}

	/* just a helper function */
	private List<List<Float>> convertToListFloats(List<List<String>> ls) {
		List<List<Float>> fl = new ArrayList<List<Float>>();
		for (List<String> sline : ls) {
			Float a = Float.valueOf(sline.get(0));
			Float b = Float.valueOf(sline.get(1));
			fl.add(Arrays.asList(a, b));
		}
		return fl;
	}

	/*
	 * Analyzing dataset for most distant centroids. Assigning closest vectors
	 * to the correspoding centroids and recalculating vector means. After the
	 * first set of clusters has been formed iterate over vectors of each
	 * cluster and recalculate their distance from centroids. Based on the new
	 * distance values rearrange vectors to new clusters and repeat again.
	 */
	public ArrayList<Cluster> runClassificator(int k, int runs) {
		List<List> cents = new ArrayList<List>();
		ls = mapDataset();

		/* Picking random centroids */
		cents = pickRandomCentroids(k);

		// Removing centroids from main dataset
		Iterator<List> it = cents.iterator();
		List<List<Float>> fl = convertToListFloats(ls);
		// for (List<Float> f : cents) {
		// Iterator<List<Float>> remns = fl.iterator();
		// while (remns.hasNext()) {
		// List<Float> r = remns.next();
		// if (r.equals(f)) {
		// remns.remove();
		// }
		// }
		// }

		// Adding clusters with their respective centroid values
		while (it.hasNext()) {
			List<Float> str = it.next();
			clusterBasket.add(new Cluster(Float.valueOf(str.get(0)), Float
					.valueOf(str.get(1))));
			it.remove();
		}

		/* Arranging candidates to randomly picked clusters */
		for (List<Float> candi : fl) {
			arrangeAmongClusters(candi);
		}

		/* Recalculating the xmeans, ymeans for clusters */
		for (Cluster c : clusterBasket) {
			c.updatePopulation();
		}

		/*
		 * Regrouping individuals (until optimal distribution is reached or
		 * until i > runs)
		 */
		for (int i = 0; i != runs; i++) {
			for (List<Float> candi : fl) {
				arrangeAmongClusters(candi);
			}

			if (areClustersComplete()) {
				// System.out.println("Clusters are complete");
				break;
			}

			for (Cluster c : clusterBasket) {
				c.updatePopulation();
				// c.reportPopulation();
				if ((i + 1) < runs) {
					c.flushAll();
				}
			}
		}

		return clusterBasket;
	}
}

package tasty.minerals.kmeans.cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * This is a Cluster class, which gets instantiated k times.
 */
public class Cluster {
	private float xmean;
	private float ymean;
	private final float X; // debug
	private final float Y; // debug
	private boolean changed = false;
	private List<List<Float>> popul = new ArrayList<List<Float>>();
	private int total;

	public Cluster(float a, float b) {
		X = a;
		Y = b;
		xmean = a;
		ymean = b;
		popul.add(Arrays.asList(a, b)); // adding a cluster to total population
		total = 1;
	}

	// debug
	public void reportPopulation() {
		System.out.println("\n");
		System.out.println("Cluster: " + getX() + " " + getY() + " contains: "
				+ popul);
	}

	public void flushAll() {
		popul.clear(); // flushing population because we recalculated the means
		total = 1;
		changed = false;
	}

	/* Putting vectors (individuals) into cluster */
	public void add(float x, float y) {
		List<Float> mbr = Arrays.asList(x, y);
		popul.add(mbr);
		total++;
	}

	/* Updating the cluster population */
	public void updatePopulation() {
		Float mx = 0.0f;
		Float my = 0.0f;
		Float allmx = 0.0f;
		Float allmy = 0.0f;
		for (List<Float> m : popul) {
			mx = m.get(0);
			my = m.get(1);
			allmx += mx;
			allmy += my;
		}
		xmean = allmx / total;
		ymean = allmy / total;
	}

	public float getXmean() {
		return xmean;
	}

	public void setXmean(float xmean) {
		this.xmean = xmean;
	}

	public float getYmean() {
		return ymean;
	}

	public void setYmean(float ymean) {
		this.ymean = ymean;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<List<Float>> getPopul() {
		return popul;
	}

	public float getX() {
		return X;
	}

	public float getY() {
		return Y;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}
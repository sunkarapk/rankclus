/**
 * SingleCluster - Single Cluster store for the program
 */

import Jama.*;
import java.util.ArrayList;

/**
 * @author pksunkara
 */
public class SingleCluster {

	public ArrayList<Integer> l;

	public Matrix ri = null;
	public Matrix ry = null;
	public Matrix rx = null;

	public double [] s;

	public SingleCluster(int k) {
		this.l = new ArrayList<Integer>();
		this.s = new double[k];
	}
}

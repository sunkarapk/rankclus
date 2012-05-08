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

	public Matrix ri;
	public Matrix ry;
	public Matrix rx;

	public double [] s;

	public SingleCluster(int k) {
		this.l = new ArrayList<Integer>();
		this.s = new double[k];
	}
}

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

	public SingleCluster() {
		this.l = new ArrayList<Integer>();
	}
}

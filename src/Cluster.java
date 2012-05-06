/**
 * Store - Cluster store for the program
 */

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * @author pksunkara
 */
public class Cluster {

	public Store s = null;
	public int k = 0;
	public int a = 1;

	public ArrayList<Set<Integer>> c = null;

	public Cluster(int k, Store s, int a) {
		this.k = k;
		this.s = s;
		this.a = a

		this.c = new ArrayList<Set<Integer>>();
		for (int i=0; i<k; i++) {
			this.c.add(new HashSet<Integer>());
		}

		randomize();
	}

	public void iterate() {
		step1();
		step2();
		step3();
	}

	public void step1() {

	}

	public void step2() {

	}

	public void step3() {

	}

	public void randomize() {
		Iterator<String> it = this.s.v.keySet().iterator();
		while (it.hasNext()) {
			Vertex v = this.s.v.get(it.next());
			if (v.type == Vertex.CONFERENCE) {
				int clusterNum = (int) (Math.floor(Math.random() * this.k));
				v.cluster = clusterNum;
				this.c.get(clusterNum).add(v.id);
			}
		}

		for (int i=0; i<k; i++) {
			if (this.c.get(i).isEmpty()) {
				clearAll();
				randomize();
				break;
			}
		}
	}

	public void clearAll() {
		for (int i=0; i<k; i++) {
			this.c.get(i).clear();
		}
	}
}

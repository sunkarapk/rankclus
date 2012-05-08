/**
 * Store - Data store for the program
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

/**
 * @author pksunkara
 */
public class Store {

	public HashMap<String, Vertex> v;
	public HashMap<String, Edge> e;

	public int a = 0, c = 0;

	public HashMap<String, String> d;

	public HashMap<String, Set<String>> pa;
	public HashMap<String, String> pc;

	public double[][] wyy = null;
	public double[][] wxy = null;

	public Store() {
		this.v = new HashMap<String, Vertex>();
		this.e = new HashMap<String, Edge>();

		this.d = new HashMap<String, String>();

		this.pa = new HashMap<String, Set<String>>();
		this.pc = new HashMap<String, String>();
	}

	public void author(String itemId, String name) {
		this.v.put(itemId, new Vertex(name, a, Vertex.PERSON));
		this.a++;
	}

	public void conference(String itemId, String name) {
		if (!this.v.containsKey(name)) {
			this.v.put(name, new Vertex(name, this.c, Vertex.CONFERENCE));
			this.c++;
		}
		this.d.put(itemId, name);
	}

	public void paper(String itemId) {
		this.pa.put(itemId, new HashSet<String>());
	}

	public double xx(int i, int j) {
		return 0;
	}

	public double yy(int i, int j) {
		return this.wyy[i][j];
	}

	public double xy(int i, int j) {
		return this.wxy[i][j];
	}

	public double yx(int i, int j) {
		return this.wxy[j][i];
	}

	public void build() {
		this.prepare();
		System.gc();
		this.formWyy();
		this.formWxy();
		this.cleanse();
		System.gc();
	}

	public void prepare() {
		Iterator<String> it = this.e.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Edge val = this.e.get(key);
			if (this.pa.containsKey(val.dst)) {
				this.pa.get(val.dst).add(val.src);
			} else if (this.d.containsKey(val.dst)) {
				this.pc.put(val.src, this.d.get(val.dst));
			}
		}
		this.e = null;
		this.d = null;
	}

	public void cleanse() {
		this.pa = null;
		this.pc = null;
	}

	public void formWyy() {
		this.wyy = new double[this.a][this.a];

		Iterator<String> it = this.pa.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Set<String> val = this.pa.get(key);
			String [] as = new String[val.size()];
			int j = 0, k = 0;
			Iterator<String> ait = val.iterator();
			while (ait.hasNext()) {
				as[k++] = ait.next();
			}
			for(k = 0; k < as.length; k++) {
				for (j = 0; j < as.length; j++) {
					if (!as[j].equals(as[k])) {
						this.wyy[this.v.get(as[j]).id][this.v.get(as[k]).id]++;
					}
				}
			}
		}
	}

	public void formWxy() {
		this.wxy = new double[this.c][this.a];

		Iterator<String> it = this.pc.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String val = this.pc.get(key);
			Iterator<String> ait = this.pa.get(key).iterator();
			while (ait.hasNext()) {
				this.wxy[this.v.get(val).id][this.v.get(ait.next()).id]++;
			}
		}
	}
}

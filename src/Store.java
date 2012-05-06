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

	public int aId = 0, cId = 0;

	public HashMap<String, String> d;

	public HashMap<String, Set<String>> pa;
	public HashMap<String, String> pc;

	public int[][] wyy = null;
	public int[][] wxy = null;

	public Store() {
		this.v = new HashMap<String, Vertex>();
		this.e = new HashMap<String, Edge>();

		this.d = new HashMap<String, String>();

		this.pa = new HashMap<String, Set<String>>();
		this.pc = new HashMap<String, String>();
	}

	public void author(String itemId, String name) {
		this.v.put(itemId, new Vertex(name, aId, Vertex.PERSON));
		this.aId++;
	}

	public void conference(String itemId, String name) {
		if (!this.v.containsKey(name)) {
			this.v.put(name, new Vertex(name, this.cId, Vertex.CONFERENCE));
			this.cId++;
		}
		this.d.put(itemId, name);
	}

	public void paper(String itemId) {
		this.pa.put(itemId, new HashSet<String>());
	}

	public int xx(int i, int j) {
		return 0;
	}

	public int yy(int i, int j) {
		return this.wyy[i][j];
	}

	public int xy(int i, int j) {
		return this.wxy[i][j];
	}

	public int yx(int i, int j) {
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
		this.wyy = new int[this.aId][this.aId];

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
		this.wxy = new int[this.cId][this.aId];

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

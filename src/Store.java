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

	public int aId = 0;
	public HashMap<Integer, String> a;

	public int cId = 0;
	public HashMap<Integer, String> c;

	public HashMap<String, Set<String>> p;

	public Store() {
		this.v = new HashMap<String, Vertex>();
		this.e = new HashMap<String, Edge>();

		this.a = new HashMap<Integer, String>();
		this.c = new HashMap<Integer, String>();

		this.p = new HashMap<String, Set<String>>();
	}

	public void author(String itemId) {
		this.v.put(itemId, new Vertex(itemId, "person"));
		this.a.put(this.aId, itemId);
		this.aId++;
	}

	public void conference(String itemId) {
		this.v.put(itemId, new Vertex(itemId, "proceedings"));
		this.c.put(this.cId, itemId);
		this.cId++;
	}

	public void paper(String itemId) {
		this.p.put(itemId, new HashSet<String>());
	}

	public void cleanse() {
		Iterator<String> it = this.e.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			Edge val = this.e.get(key);
			if (this.p.containsKey(val.dst)) {
				this.p.get(val.dst).add(val.src);
			}
		}

		it = this.p.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			Set<String> val = this.p.get(key);
			System.out.println(val.size());
		}
	}

	public void formWxx() {
	}

	public void formWxy() {
	}
}

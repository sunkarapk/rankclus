/**
 * Store - Data store for the program
 */

import java.util.ArrayList;
import java.util.HashMap;

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

	public HashMap<String, ArrayList<String>> p;

	public Store() {
		this.v = new HashMap<String, Vertex>();
		this.e = new HashMap<String, Edge>();

		this.a = new HashMap<Integer, String>();
		this.c = new HashMap<Integer, String>();
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
		this.p.put(itemId, new ArrayList<String>());
	}

	public void formWxx() {
	}

	public void formWxy() {
	}
}

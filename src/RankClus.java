/**
 * Main class for rankclus
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * @author pksunkara
 */
public class RankClus {

	public static Store s;
	public static Cluster c;
	public static Rank r;
	public static int k = 0, t = 10, a = 100;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: RankClus <k> <a> <t>");
			System.exit(1);
		}

		k = Integer.parseInt(args[0]);
		a = Integer.parseInt(args[1]);
		t = Integer.parseInt(args[2]);

		s = new Store();
		System.out.println("Initialized data store!");

		new Parser("data/input.xml", s);
		System.out.println("Parsing input finished!");

		s.build();
		System.out.println("Building data structure finished!");

		c = new Cluster(k, s, a);
		System.out.println("Initialized clustering!");

		r = new Rank(s, c);
		System.out.println("Intialized ranking!");

		for (int i=0; i<t; i++) {
			c.iterate();
		}

		System.out.println("Printing results!\n");
		print();
	}

	public static void print() {
		HashMap<Integer, String> cl = new HashMap<Integer, String>();
		HashMap<Integer, String> ca = new HashMap<Integer, String>();

		Iterator<String> it = s.v.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (s.v.get(name).type == Vertex.CONFERENCE) {
				cl.put(s.v.get(name).id, name);
			} else {
				ca.put(s.v.get(name).id, s.v.get(name).name);
			}
		}

		c.step1();

		for (int i=0; i<k; i++) {
			System.out.println("Cluster " + i);

			int cs = c.c.get(i).l.size();

			for (int j=0; j<cs; j++) {
				System.out.println("\t" + cl.get(c.c.get(i).l.get(j)));
			}

			System.out.println("");

			ArrayList<RankAuthor> l = r.rank(i);
			Iterator<RankAuthor> itr = l.iterator();
			while (itr.hasNext()) {
				System.out.println("\t" + ca.get(itr.next().id));
			}

			System.out.println("");
		}
	}
}

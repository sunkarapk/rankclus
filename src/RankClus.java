/**
 * Main class for rankclus
 */

import java.util.*;

/**
 * @author pksunkara
 */
public class RankClus {

	public static Store s;
	public static Cluster c;
	public static int k = 0, t = 10, a = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: RankClus <k> <a> <t>");
			System.exit(1);
		}

		k = Integer.parseInt(args[0]);
		a = Integer.parseInt(args[1]);
		t = Integer.parseInt(args[2]);

		s = new Store();
		System.out.println("Initialized data store!");

		new Parser("../data/input.xml", s);
		System.out.println("Parsing input finished!");

		s.build();
		System.out.println("Building data structure finished!");

		for (int i=0; i<s.aId; i++) {
			for (int j=0; j<s.aId; j++) {
				System.out.print(s.yy(i,j));
			}
			System.out.println("");
		}
		System.out.println("");
		for (int i=0; i<s.cId; i++) {
			for (int j=0; j<s.aId; j++) {
				System.out.print(s.xy(i,j));
			}
			System.out.println("");
		}

		c = new Cluster(k, s, a);
		System.out.println("Initialized clustering!");

		for (int i=0; i<k; i++) {
			Iterator it = c.c.get(i).iterator();
			while (it.hasNext()) {
				System.out.print(" " + it.next());
			}
			System.out.println("");
		}

		for (int i=0; i<t; i++) {
			c.iterate();
		}
	}
}

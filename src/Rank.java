/**
 * Rank - Rank the authors in a cluster
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

/**
 * @author pksunkara
 */
public class Rank {

	public Store s = null;
	public Cluster c = null;

	public Rank (Store s, Cluster c) {
		this.s = s;
		this.c = c;
	}

	public ArrayList<RankAuthor> rank(int n) {
		ArrayList<RankAuthor> r = new ArrayList<RankAuthor>();

		for (int i=0; i<s.a; i++) {
			r.add(new RankAuthor(c.c.get(n).ry.get(i, 0), i));
		}

		Collections.sort(r, new RankComparator());

		return r;
	}

	private class RankComparator implements Comparator<RankAuthor> {

		public int compare(RankAuthor a, RankAuthor b) {
			if (a.v > b.v) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}

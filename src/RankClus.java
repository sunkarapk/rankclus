/**
 * Main class for rankclus
 */

import java.util.*;

/**
 * @author pksunkara
 *
 */
public class RankClus {

	private static HashMap<String, Vertex> v;
	private static HashMap<String, Edge> e;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Parser("data/input.xml", v, e);
		System.out.println("Parsing input finished!");
	}

}

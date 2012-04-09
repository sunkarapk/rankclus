/**
 * Main class for rankclus
 */

/**
 * @author pksunkara
 */
public class RankClus {

	private static Store s;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		s = new Store();
		System.out.println("Initialized data store!");

		new Parser("data/input.xml", s);
		System.out.println("Parsing input finished!");

		s.formWxx();
		s.formWxy();
	}

}

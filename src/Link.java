/**
 * Link type in DBLP
 */

/**
 * @author pksunkara
 * @version 1.0
 */
public class Link {

	public String id = null;
	public String src = null;
	public String dst = null;
	public String type = null;

	/**
	 * Constructor
	 *
	 * @param id
	 */
	public Link(String id, String src, String dst) {
		this.id = id;
		this.src = src;
		this.dst = dst;
	}
}

/**
 * Extracts only the required data from DBLP
 */

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.sun.org.apache.xml.internal.serialize.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author pksunkara
 * @version 1.0
 */
public class Extract extends DefaultHandler {

	private HashMap<String, Obj> myObjs;
	private HashMap<String, Link> myLinks;

	private Document dom;

	private int attrType = 0;
	private String itemId = null;
	private String tmp = null;

	/**
	 * Constructor
	 */
	public Extract() {
		myObjs = new HashMap<String, Obj>();
		myLinks = new HashMap<String, Link>();
	}

	/**
	 * Run extractor
	 *
	 * @param xmlFile
	 */
	private void run(String xmlFile) {
		parse(xmlFile);
		generate();
	}

	/**
	 * Parse existing document
	 *
	 * @param xmlFile
	 */
	private void parse(String xmlFile) {
		SAXParserFactory spf = SAXParserFactory.newInstance();

		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(xmlFile, this);
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pe) {
			pe.printStackTrace();
		}catch (IOException io) {
			io.printStackTrace();
		}
	}

	/**
	 * Generated reduced document
	 */
	private void generate() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
		} catch (ParserConfigurationException pe) {
			pe.printStackTrace();
		}

		Iterator<String> it;

		Element root = dom.createElement("PROX3DB");
		dom.appendChild(root);

		System.out.println("OBJECTS");
		Element objectsEle = dom.createElement("OBJECTS");
		for (it = myObjs.keySet().iterator(); it.hasNext();) {
			Obj o = myObjs.get(it.next());
			if (o.type.equals("person") || o.type.equals("proceedings") || o.type.equals("paper")) {
				Element objectEle = dom.createElement("OBJECT");
				objectEle.setAttribute("ID", o.id);
				objectsEle.appendChild(objectEle);
				System.out.println(o.id);
			}
		}
		root.appendChild(objectsEle);

		System.out.println("LINKS");
		Element linksEle = dom.createElement("LINKS");
		for (it = myLinks.keySet().iterator(); it.hasNext();) {
			Link l = myLinks.get(it.next());
			if (l.type.equals("author-of")) {
				Obj dst = myObjs.get(l.dst);
				if (dst != null && (dst.type.equals("paper") || dst.type.equals("proceedings"))) {
					Element linkEle = dom.createElement("LINK");
					linkEle.setAttribute("ID", l.id);
					linkEle.setAttribute("O1-ID", myObjs.get(l.src).id);
					linkEle.setAttribute("O2-ID", dst.id);
					linksEle.appendChild(linkEle);
					System.out.println(l.id);
				}
			}
		}
		root.appendChild(linksEle);

		Element attrsEle = dom.createElement("ATTRIBUTES");

		Element oTypeEle = dom.createElement("ATTRIBUTE");
		oTypeEle.setAttribute("NAME", "object-type");
		oTypeEle.setAttribute("ITEM-TYPE", "O");
		oTypeEle.setAttribute("DATA-TYPE", "str");
		for (it = myObjs.keySet().iterator(); it.hasNext();) {
			Obj o = myObjs.get(it.next());
			if (o.type.equals("proceedings") || o.type.equals("person") || o.type.equals("paper")) {
				Element aValEle = dom.createElement("ATTR-VALUE");
				aValEle.setAttribute("ITEM-ID", o.id);
				Element cValEle = dom.createElement("COL-VALUE");
				cValEle.appendChild(dom.createTextNode(o.type));
				aValEle.appendChild(cValEle);
				oTypeEle.appendChild(aValEle);
				System.out.println(o.type);
			}
		}
		attrsEle.appendChild(oTypeEle);

		Element confEle = dom.createElement("ATTRIBUTE");
		confEle.setAttribute("NAME", "conference");
		confEle.setAttribute("ITEM-TYPE", "O");
		confEle.setAttribute("DATA-TYPE", "str");
		for (it = myObjs.keySet().iterator(); it.hasNext();) {
			Obj o = myObjs.get(it.next());
			if (o.type.equals("proceedings")) {
				Element aValEle = dom.createElement("ATTR-VALUE");
				aValEle.setAttribute("ITEM-ID", o.id);
				Element cValEle = dom.createElement("COL-VALUE");
				cValEle.appendChild(dom.createTextNode(o.name));
				aValEle.appendChild(cValEle);
				confEle.appendChild(aValEle);
				System.out.println(o.name);
			}
		}
		attrsEle.appendChild(confEle);

		Element nameEle = dom.createElement("ATTRIBUTE");
		nameEle.setAttribute("NAME", "name");
		nameEle.setAttribute("ITEM-TYPE", "O");
		nameEle.setAttribute("DATA-TYPE", "str");
		for (it = myObjs.keySet().iterator(); it.hasNext();) {
			Obj o = myObjs.get(it.next());
			if (o.type.equals("person")) {
				Element aValEle = dom.createElement("ATTR-VALUE");
				aValEle.setAttribute("ITEM-ID", o.id);
				Element cValEle = dom.createElement("COL-VALUE");
				cValEle.appendChild(dom.createTextNode(o.name));
				aValEle.appendChild(cValEle);
				nameEle.appendChild(aValEle);
				System.out.println(o.name);
			}
		}
		attrsEle.appendChild(nameEle);

		root.appendChild(attrsEle);

		try {
			OutputFormat fmt = new OutputFormat(dom);
			fmt.setIndenting(true);
			XMLSerializer slzr = new XMLSerializer(new FileOutputStream(new File("data/dblp-small.xml")), fmt);
			slzr.serialize(dom);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
		if (qName.equalsIgnoreCase("LINK")) {
			String id = attr.getValue("ID");
			System.out.println(id);
			myLinks.put(id, new Link(id, attr.getValue("O1-ID"), attr.getValue("O2-ID")));
		} else if (qName.equalsIgnoreCase("ATTRIBUTE")) {
			String name = attr.getValue("NAME");
			System.out.println(name);
			if (name.equals("object-type")) {
				attrType = 1;
			} else if (name.equals("conference")) {
				attrType = 2;
			} else if (name.equals("name")) {
				attrType = 3;
			} else if (name.equals("link-type")) {
				attrType = 4;
			}
		} else if (qName.equalsIgnoreCase("ATTR-VALUE")) {
			itemId = attr.getValue("ITEM-ID");
			System.out.println(itemId);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		tmp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("ATTRIBUTE")) {
			attrType = 0;
		} else if (qName.equalsIgnoreCase("ATTR-VALUE")) {
			itemId = null;
		} else if (qName.equalsIgnoreCase("COL-VALUE")) {
			Obj o; Link l;
			switch (attrType) {
			case 1:
				if (tmp.equals("paper") || tmp.equals("person") || tmp.equals("proceedings")) {
					o = new Obj(itemId);
					o.type = tmp;
					myObjs.put(itemId, o);
				}
				break;
			case 2:
				o = myObjs.get(itemId);
				o.name = tmp;
				myObjs.put(itemId, o);
				break;
			case 3:
				o = myObjs.get(itemId);
				o.name = tmp;
				myObjs.put(itemId, o);
				break;
			case 4:
				if (tmp.equals("author-of")) {
					l = myLinks.get(itemId);
					l.type = tmp;
					myLinks.put(itemId, l);
				} else {
					myLinks.remove(itemId);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Extract e = new Extract();
		e.run("data/dblp.xml");
	}

}

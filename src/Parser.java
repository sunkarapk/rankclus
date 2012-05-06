/**
 * Parser.java - To parse the dataset
 */

import java.io.IOException;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author pksunkara
 */
public class Parser extends DefaultHandler {

	private Store s;

	private int attrType = 0;
	private String itemId = null;
	private String text = null;

	public Parser(String file, Store s) {
		this.s = s;

		SAXParserFactory spf = SAXParserFactory.newInstance();

		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(file, this);
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
		if (qName.equalsIgnoreCase("LINK")) {
			String id = attr.getValue("ID");
			s.e.put(id, new Edge(id, attr.getValue("O1-ID"), attr.getValue("O2-ID")));
		} else if (qName.equalsIgnoreCase("ATTRIBUTE")) {
			String name = attr.getValue("NAME");
			if (name.equalsIgnoreCase("object-type")) {
				attrType = 1;
			} else if (name.equalsIgnoreCase("conference")) {
				attrType = 2;
			} else if (name.equalsIgnoreCase("name")) {
				attrType = 3;
			}
		} else if (qName.equalsIgnoreCase("ATTR-VALUE")) {
			itemId = attr.getValue("ITEM-ID");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		text = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("ATTRIBUTE")) {
			attrType = 0;
		} else if(qName.equalsIgnoreCase("ATTR-VALUE")) {
			itemId = null;
		} else {
			switch (attrType) {
			case 1:
				if (text.equals("paper")) {
					s.paper(itemId);
				}
				break;
			case 2:
				s.conference(itemId, text);
				break;
			case 3:
				s.author(itemId, text);
				break;
			default:
				break;
			}
		}
	}
}

/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2010 UFMA/LAWS (Laboratory of Advanced Web Systems), All
 * Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.club.rss;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xerces.dom.NamedNodeMapImpl;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class RSSReader {
	protected URL url;
	protected RSSRoot rssRoot;
	protected DOMParser parser;
	protected Document doc;

	private boolean log = false;

	public RSSReader() {

	}

	public RSSReader(File file) {

	}

	public RSSReader(URL url) {
		this.url = url;
	}

	public boolean parse() throws SAXException, IOException {
		parser = new DOMParser();
		parser.parse(new InputSource(url.openStream()));
		Document doc = parser.getDocument();
		NodeList nodeList = doc.getChildNodes();

		parser.parse(new InputSource(url.openStream()));
		doc = parser.getDocument();

		if (log)
			System.out.println("log#RSSReader::parse # Parsing rss file: "
					+ url);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals("rss")) {
				parseRSS(node);
			}
		}
		return true;
	}

	public boolean parse(URL url) throws SAXException, IOException {
		this.url = url;
		parse();

		return true;
	}

	protected boolean parseRSS(Node nodeRSS) {
		rssRoot = new RSSRoot();

		if (log)
			System.out
					.println("log#RSSReader::parseRSS # Parsing rss root Element");

		NodeList nodeList = nodeRSS.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals("channel")) {
				parseChannel(node);
			}
		}
		return true;
	}

	protected boolean parseChannel(Node nodeChannel) {
		RSSChannel channel = new RSSChannel();

		NodeList nodeList = nodeChannel.getChildNodes();

		if (log)
			System.out
					.println("log#RSSReader::parseChannel $ Parsing channel element");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals("title")) {
				channel.setTitle(node.getTextContent());
			} else if (node.getNodeName().equals("link")) {
				try {
					channel.setLink(new URL(node.getTextContent()));
				} catch (MalformedURLException e) {
					System.out.println("MalformedURLException");
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					System.out.println("DOMException");
				}
			} else if (node.getNodeName().equals("description")) {
				channel.setDescription(node.getTextContent());
			} else if (node.getNodeName().equals("language")) {
				channel.setLanguage(node.getTextContent());
			} else if (node.getNodeName().equals("item")) {
				parseItem(channel, node);
			}
		}
		rssRoot.addChannel(channel);
		return true;
	}

	protected boolean parseItem(RSSChannel channel, Node nodeItem) {
		RSSItem item = new RSSItem();

		NodeList nodeList = nodeItem.getChildNodes();

		if (log)
			System.out
					.println("log#RSSReader::parseItem $ Parsing item element");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals("title")) {
				item.setTitle(node.getTextContent());

				if (log)
					System.out.println("\tTitle = " + item.getTitle());
			} else if (node.getNodeName().equals("link")) {
				try {
					item.setLink(new URL(node.getTextContent()));
				} catch (MalformedURLException e) {
					System.out.println("MalformedURLException");
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					System.out.println("DOMException");
				}
			} else if (node.getNodeName().equals("description")) {
				item.setDescription(node.getTextContent());
			} else if (node.getNodeName().equals("comments")) {
				item.setComments(node.getTextContent());
			} else if (node.getNodeName().equals("enclosure")) {
				parseEnclosure(item, node);
			} else if (node.getNodeName().equals("category")) {
				parseCategory(item, node);
			} else if (node.getNodeName().equals("dc:creator")) {
				item.setAuthor(node.getTextContent());
			}
		}

		channel.addItem(item);
		return true;

	}

	//TODO: parse Enclosure
	//FIXME: there are two enclosure elements in each item (one for image and one for zip application)
	//		How the best way to treat this
	protected boolean parseEnclosure(RSSItem item, Node node) {
		NamedNodeMapImpl attrList = (NamedNodeMapImpl) node.getAttributes();

		if (log)
			System.out
					.println("log#RSSReader::parseEnclosure $ Parsing enclosure element");

		URL url = null;
		try {
			url = new URL(attrList.getNamedItem("url").getNodeValue());
			if (log)
				System.out.println("log#RSSReader::parseEnclosure $ URL = "
						+ attrList.getNamedItem("url").getNodeValue());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		}
		if (attrList.getNamedItem("type").getNodeValue().startsWith("image"))
			item.setImageUrl(url);
		else
			item.setResourcesZipUrl(url);
		return true;
	}

	//TODO:	parse Category
	protected boolean parseCategory(RSSItem item, Node node) {
		return true;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public RSSRoot getRssRoot() {
		return rssRoot;
	}

	public void setRssRoot(RSSRoot rssRoot) {
		this.rssRoot = rssRoot;
	}

	public static void main(String args[]) {
		URL url;
		try {
			url = new URL("http://club.ncl.org.br/?q=rss.xml");
			RSSReader reader = new RSSReader(url);
			reader.parse();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

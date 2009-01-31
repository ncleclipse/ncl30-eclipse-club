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

public class RSSReader {
	protected URL url;
	protected RSSRoot rssRoot;
	protected DOMParser parser;
	protected Document doc;

	private boolean log = true;
	
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
			System.out.println("log#RSSReader::parse # Parsing rss file: " + url);
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeName().equals("rss")){
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

	protected boolean parseRSS(Node nodeRSS){
		rssRoot = new RSSRoot();
		
		if (log)
			System.out.println("log#RSSReader::parseRSS # Parsing rss root Element");
		
		NodeList nodeList = nodeRSS.getChildNodes();
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeName().equals("channel")){
				parseChannel(node);
			}
		}
		return true;
	}
	
	protected boolean parseChannel(Node nodeChannel){
		RSSChannel channel = new RSSChannel();
		
		NodeList nodeList = nodeChannel.getChildNodes();
		
		if (log)
			System.out.println("log#RSSReader::parseChannel $ Parsing channel element");
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeName().equals("title")){
				channel.setTitle(node.getTextContent());
			}
			else if(node.getNodeName().equals("link")){
				try {
					channel.setLink(new URL(node.getTextContent()));
				} catch (MalformedURLException e) {
					System.out.println("MalformedURLException");
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					System.out.println("DOMException");
				}
			}
			else if(node.getNodeName().equals("description")){
				channel.setDescription(node.getTextContent());
			}
			else if(node.getNodeName().equals("language")){
				channel.setLanguage(node.getTextContent());
			}
			else if(node.getNodeName().equals("item")){
				parseItem(channel, node);
			}
		}
		rssRoot.addChannel(channel);
		return true;
	}
	
	protected boolean parseItem(RSSChannel channel, Node nodeItem){
		RSSItem item = new RSSItem();
		
		NodeList nodeList = nodeItem.getChildNodes();
		
		if (log)
			System.out.println("log#RSSReader::parseItem $ Parsing item element");
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeName().equals("title")){
				item.setTitle(node.getTextContent());
				
				if (log)
					System.out.println("\tTitle = " + item.getTitle());
			}
			else if(node.getNodeName().equals("link")){
				try {
					item.setLink(new URL(node.getTextContent()));
				} catch (MalformedURLException e) {
					System.out.println("MalformedURLException");
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					System.out.println("DOMException");
				}
			}
			else if(node.getNodeName().equals("description")){
				item.setDescription(node.getTextContent());
			}
			else if(node.getNodeName().equals("comments")){
				item.setComments(node.getTextContent());
			}
			else if(node.getNodeName().equals("enclosure")){
				parseEnclosure(item, node);
			}
			else if(node.getNodeName().equals("category")){
				parseCategory(item, node);
			}
			else if(node.getNodeName().equals("dc:creator")){
				item.setAuthor(node.getTextContent());
			}
		}
		
		channel.addItem(item);
		return true;
		
	}
	//TODO: parse Enclosure
	//FIXME: there are two enclosure elements in each item (one for image and one for zip application)
	//		How the best way to treat this
	protected boolean parseEnclosure(RSSItem item, Node node){
		NamedNodeMapImpl attrList = (NamedNodeMapImpl) node.getAttributes();
		
		if (log)
			System.out.println("log#RSSReader::parseEnclosure $ Parsing enclosure element");
		
		URL url = null;
		try {
			url = new URL(attrList.getNamedItem("url").getNodeValue());
			if (log)
				System.out.println("log#RSSReader::parseEnclosure $ URL = " + attrList.getNamedItem("url").getNodeValue());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		}
		if(attrList.getNamedItem("type").getNodeValue().startsWith("image"))
			item.setImageUrl(url);
		else item.setResourcesZipUrl(url);
		return true;
	}
	
	//TODO:	parse Category
	protected boolean parseCategory(RSSItem item, Node node){
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

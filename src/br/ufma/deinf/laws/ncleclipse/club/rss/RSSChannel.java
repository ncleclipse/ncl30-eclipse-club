package br.ufma.deinf.laws.ncleclipse.club.rss;

import java.net.URL;
import java.util.Vector;

public class RSSChannel extends RSSElement{
	protected String description;
	protected String language;
	protected Vector<RSSItem> items;

	RSSChannel (){
		super();
		items = new Vector<RSSItem>();
	}
	
	public RSSChannel(String title, URL link, String description,
			String language, Vector<RSSItem> items) {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.items = items;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Vector<RSSItem> getItems() {
		return items;
	}

	public void setItems(Vector<RSSItem> items) {
		this.items = items;
	}
	
	public boolean addItem(RSSItem item){
		if(items == null)
			items = new Vector<RSSItem>();
		
		return items.add(item);
	}
}

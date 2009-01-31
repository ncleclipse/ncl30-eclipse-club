package br.ufma.deinf.laws.ncleclipse.club.rss;

import java.net.URL;

public class RSSElement {
	protected String title;
	protected URL link;
	protected String description;
	
	public RSSElement(){
		
	}
	
	public RSSElement(String title, URL link, String description) {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URL getLink() {
		return link;
	}

	public void setLink(URL link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

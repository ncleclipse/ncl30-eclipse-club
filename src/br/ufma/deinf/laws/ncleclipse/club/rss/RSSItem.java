package br.ufma.deinf.laws.ncleclipse.club.rss;

import java.net.URL;
import java.util.Date;
import java.util.Vector;

public class RSSItem extends RSSElement{
	public URL getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(URL imageUrl) {
		this.imageUrl = imageUrl;
	}

	public URL getResourcesZipUrl() {
		return resourcesZipUrl;
	}

	public void setResourcesZipUrl(URL resourcesZipUrl) {
		this.resourcesZipUrl = resourcesZipUrl;
	}

	protected Date pubDate;
	protected String author;
	protected String comments;
	protected URL imageUrl;
	protected URL resourcesZipUrl;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	Vector<String> category;
	
	public RSSItem(){
		super();
	}

	public RSSItem(Date pubDate, String author, String comments, URL imageUrl,
			URL resourcesZipUrl, Vector<String> category) {
		super();
		this.pubDate = pubDate;
		this.author = author;
		this.comments = comments;
		this.imageUrl = imageUrl;
		this.resourcesZipUrl = resourcesZipUrl;
		this.category = category;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Vector<String> getCategory() {
		return category;
	}

	public void setCategory(Vector<String> category) {
		this.category = category;
	}


	// TODO: guid isPermanentlink
}
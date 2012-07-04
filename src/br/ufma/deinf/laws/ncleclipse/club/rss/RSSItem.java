/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.club.rss;

import java.net.URL;
import java.util.Date;
import java.util.Vector;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class RSSItem extends RSSElement {
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

	public RSSItem() {
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

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

import java.net.URL;
import java.util.Vector;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class RSSChannel extends RSSElement {
	protected String description;
	protected String language;
	protected Vector<RSSItem> items;

	RSSChannel() {
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

	public boolean addItem(RSSItem item) {
		if (items == null)
			items = new Vector<RSSItem>();

		return items.add(item);
	}
}

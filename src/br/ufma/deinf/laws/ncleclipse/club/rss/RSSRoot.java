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

import java.util.Vector;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class RSSRoot extends RSSElement {
	Vector<RSSChannel> rssChannels;

	public RSSRoot() {
		rssChannels = new Vector<RSSChannel>();
	}

	public boolean addChannel(RSSChannel channel) {
		if (rssChannels == null)
			rssChannels = new Vector<RSSChannel>();

		return rssChannels.add(channel);
	}

	public Vector<RSSChannel> getChannels() {
		return rssChannels;
	}

	public Vector<RSSItem> getAllItems() {
		Vector<RSSItem> ret = new Vector();
		for (int i = 0; i < rssChannels.size(); i++) {
			RSSChannel channel = rssChannels.get(i);
			Vector<RSSItem> items = channel.getItems();
			for (int j = 0; j < items.size(); j++) {
				ret.add(items.get(j));
			}
		}
		return ret;
	}
}

package br.ufma.deinf.laws.ncleclipse.club.rss;

import java.util.Vector;

public class RSSRoot extends RSSElement{
	Vector <RSSChannel> rssChannels;
	
	public RSSRoot(){
		rssChannels = new Vector<RSSChannel>();
	}
	
	public boolean addChannel(RSSChannel channel){
		if(rssChannels == null)
				rssChannels = new Vector<RSSChannel>();
		
		return rssChannels.add(channel);
	}
	
	public Vector<RSSChannel> getChannels(){
		return rssChannels;
	}
	
	public Vector <RSSItem> getAllItems(){
		Vector <RSSItem> ret = new Vector();
		for(int i = 0; i < rssChannels.size(); i++){
			RSSChannel channel = rssChannels.get(i);
			Vector <RSSItem> items = channel.getItems();
			for(int j = 0; j < items.size(); j++){
				ret.add(items.get(j));
			}
		}
		return ret;
	}
}

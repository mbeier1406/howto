package de.gmxhome.golkonda.howto.jse.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * Datenmodell für eine einzelne Nachricht innerhalb eines RSS-Feeds:
 * Diese Klasse repräsentiert (Teile!) einer RSS-Feed-Nachricht im Pfad {@code /rss/channel/item}. 
 * @author mbeier
 * @see RssStaxFeed
 */
public class RssStaxFeedMessage {

	private final String title;
    private final String description;
    private final List<RssStaxFeed> feedList = new ArrayList<>();
    // TODO: weitere Felder wie Link, Autor usw.

	public RssStaxFeedMessage(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public void addRssStaxFeed(RssStaxFeed rssStaxFeed) {
		feedList.add(rssStaxFeed);
	}

	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public List<RssStaxFeed> getFeedList() {
		return feedList;
	}

	@Override
	public String toString() {
		return "RssStaxFeedMessage [title=" + title + ", description=" + description + ", feedList=" + feedList + "]";
	}

}

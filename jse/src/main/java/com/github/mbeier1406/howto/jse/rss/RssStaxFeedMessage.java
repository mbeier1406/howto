package com.github.mbeier1406.howto.jse.rss;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Datenmodell für eine einzelne Nachricht innerhalb eines RSS-Feeds:
 * Diese Klasse repräsentiert (Teile!) einer RSS-Feed-Nachricht im Pfad {@code /rss/channel/item}. 
 * @author mbeier
 * @see RssStaxFeed
 */
public class RssStaxFeedMessage implements RssInterface {

	private final String title;
	private final BigDecimal version;
    private final String link;
    private final String description;
    private final List<RssStaxFeed> feedList = new ArrayList<>();
    // TODO: weitere Felder wie Link, Autor usw.

	public RssStaxFeedMessage(String title, BigDecimal version, String link, String description) {
		super();
		this.title = title;
		this.version = version;
		this.link = link;
		this.description = description;
	}

	public void addRssStaxFeed(RssStaxFeed rssStaxFeed) {
		feedList.add(rssStaxFeed);
	}

	/** {@inheritDoc} */
	@Override
	public String getTitle() {
		return title;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getVersion() {
		return version;
	}

	/** {@inheritDoc} */
	@Override
	public String getLink() {
		return link;
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return description;
	}

	public List<RssStaxFeed> getFeedList() {
		return feedList;
	}

	@Override
	public String toString() {
		return "RssStaxFeedMessage [title=" + title + ", version=" + version + ", link=" + link + ", description="
				+ description + ", feedList=" + feedList + "]";
	}

}

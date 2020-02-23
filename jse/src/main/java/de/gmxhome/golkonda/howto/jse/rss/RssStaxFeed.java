package de.gmxhome.golkonda.howto.jse.rss;

/**
 * Datenmodell für einen RSS-Feed:
 * Diese Klasse repräsentiert (Teile!) eines RSS-Feeds im Pfad {@code /rss/channel}. 
 * @author mbeier
 */
public class RssStaxFeed {

    private final String title;
    private final String link;
    private final String description;
    // TODO: weitere Felder wie Language, Autor usw.

	public RssStaxFeed(String title, String link, String description) {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}
	public String getLink() {
		return link;
	}
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "RssStaxFeed [title=" + title + ", link=" + link + ", description=" + description + "]";
	}

}

package de.gmxhome.golkonda.howto.jse.rss;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Prüft die Funktionen von {@linkplain RssStaxFeed}.
 * @author mbeier
 */
public class RssStaxFeedTest {

	public static final Logger LOGGER = LogManager.getLogger(RssStaxFeed.class);

	/** Link für {@linkplain RssStaxFeed#getTitle()} ist {@value} */
	public static final String TITLE = "DER SPIEGEL - Schlagzeilen - Tops";

	/** Link für {@linkplain RssStaxFeed#getLink()} ist {@value} */
	public static final String LINK = "https://www.spiegel.de/";

	/** Link für {@linkplain RssStaxFeed#getLink()} ist {@value} */
	public static final String DESCRIPTION = "Deutschlands führende Nachrichtenseite.";

	/** Das zu testende Objekt */
	public static RssStaxFeed rssStaxFeed;

	/** Initialisierung {@linkplain #rssStaxFeed} */
	@BeforeClass
	public static void init() {
		rssStaxFeed = new RssStaxFeed(TITLE, LINK, DESCRIPTION);
	}

	/** Die Getter müssen die korrekten Werte zurückgeben */
	@Test
	public void testeProperties() {
		LOGGER.info("rssStaxFeed={}", rssStaxFeed);
		assertThat(rssStaxFeed, allOf(
				hasProperty("title", is(TITLE)),
				hasProperty("link", is(LINK)),
				hasProperty("description", is(DESCRIPTION))));
	}

}

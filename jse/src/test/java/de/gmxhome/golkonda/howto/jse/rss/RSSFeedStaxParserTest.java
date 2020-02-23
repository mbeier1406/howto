package de.gmxhome.golkonda.howto.jse.rss;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * Prüft die Funktionen von {@linkplain RSSFeedStaxParser} indem eine RSS-Nachricht eingelesen wird.
 * @author mbeier
 */
public class RSSFeedStaxParserTest {

	public static final Logger LOGGER = LogManager.getLogger(RSSFeedStaxParserTest.class);

	/** Die URL zum RSS-Feed, der gelesen werden soll, ist {@value} */
	public static final String URL = "https://www.spiegel.de/schlagzeilen/tops/index.rss";

	/** Das zu testende Objekt */
	public RSSFeedStaxParser rssFeedStaxParser;

	/** Die Nachricht als {@linkplain RssStaxFeedMessage} */
	public RssInterface rssFeed;

	/** Liest den RSS-Fed aus {@value #URL} in ein {@linkplain RssStaxFeedMessage}-Objekt ein */
	@Before
	public void init() throws Exception {
		Instant start = Instant.now();
		rssFeedStaxParser = new RSSFeedStaxParser(URL);
		rssFeed = rssFeedStaxParser.readRssFed();
		Instant ende = Instant.now();
		LOGGER.info("Dauer in ns: {}", Duration.between(start, ende).getNano()); 
	}

	/** Liest den RSS-Fed aus {@value #URL} in ein {@linkplain RssStaxFeedMessage}-Objekt ein */
	@Test
	public void testeParser() throws Exception {
		LOGGER.info("rssFeed={}", rssFeed);
		assertNotNull("RSS-Feed ist NULL!", rssFeed);
		assertTrue("Nachricht nicht vom Spiegel!", rssFeed.getTitle().contains("SPIEGEL"));
	}

}

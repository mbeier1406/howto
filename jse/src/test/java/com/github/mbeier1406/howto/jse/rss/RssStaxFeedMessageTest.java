package com.github.mbeier1406.howto.jse.rss;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeasy.random.EasyRandom;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.mbeier1406.howto.jse.rss.RssStaxFeed;
import com.github.mbeier1406.howto.jse.rss.RssStaxFeedMessage;

/**
 * Prüft die Funktionen von {@linkplain RssStaxFeedMessage}.
 * @author mbeier
 */
public class RssStaxFeedMessageTest {

	public static final Logger LOGGER = LogManager.getLogger(RssStaxFeedMessageTest.class);

	/** Es werden {@value} Feeds eingestellt */
	public static final int ANZAHL_FEEDS = 3;

	/** RSS-Version ist {@value} */
	public static final BigDecimal VERSION = new BigDecimal("2.0");

	/** Das zu testende Objekt */
	public static RssStaxFeedMessage rssStaxFeedMessage;

	/** Mit Hilfe des {@linkplain EasyRandom}-Generators wird der {@linkplain RssStaxFeed} mit Testdaten befüllt */
	public static EasyRandom generator;

	/** Initialisierung {@linkplain #rssStaxFeedMessage} */
	@BeforeClass
	public static void init() {
		generator = new EasyRandom();
		rssStaxFeedMessage = new RssStaxFeedMessage(RssStaxFeedTest.TITLE, VERSION, RssStaxFeedTest.LINK, RssStaxFeedTest.DESCRIPTION);
		IntStream.range(0, ANZAHL_FEEDS).forEach(i -> rssStaxFeedMessage.addRssStaxFeed(generator.nextObject(RssStaxFeed.class)));
	}

	/** Prüfen, ob die Getter die korrekten Wert liefern */
	@Test
	public void testeWerte() {
		LOGGER.info("rssStaxFeedMessage={}", rssStaxFeedMessage);
		assertThat(rssStaxFeedMessage, allOf(
				hasProperty("title", is(RssStaxFeedTest.TITLE)),
				hasProperty("version", is(VERSION)),
				hasProperty("link", is(RssStaxFeedTest.LINK)),
				hasProperty("description", is(RssStaxFeedTest.DESCRIPTION)),
				hasProperty("feedList", hasSize(ANZAHL_FEEDS))));
	}

}

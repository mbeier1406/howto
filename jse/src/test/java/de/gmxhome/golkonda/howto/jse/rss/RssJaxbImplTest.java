package de.gmxhome.golkonda.howto.jse.rss;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests für die Implementierung {@linkplain RssJaxbImpl}.
 * @author mbeier
 * @see RssInterface
 */
public class RssJaxbImplTest {

	public static final Logger LOGGER = LogManager.getLogger(RssJaxbImplTest.class);

	/** Die Datei mit dem RSS-Feed zum Test ist {@value} */
	public static final String RSS_FEED = "src/test/resources/de/gmxhome/golkonda/howto/jse/rss/spiegel.rss.xml";

	/** Das zu testende Objekt */
	public static RssInterface rss;

	/** Erzeugt {@linkplain #rss} aus der Datei in {@value #RSS_FEED} */
	@BeforeClass
	public static void init() throws Exception {
		rss = new RssJaxbImpl(RSS_FEED);
	}

	/** Prüft, ob die Version korrekt ermittelt wird ({@linkplain RssInterface#getVersion()}) */
	@Test
	public void testeVersion() {
		String versionErwartet = "2.0";
		LOGGER.info("Version={} (erwartet={})", rss.getVersion(), versionErwartet);
		assertEquals("Erwartet: Version "+versionErwartet, new BigDecimal(versionErwartet), rss.getVersion());
	}

	/** Prüft, ob der Titel korrekt ermittelt wird ({@linkplain RssInterface#getTitle())}) */
	@Test
	public void testeTitel() {
		String titelErwartet = "DER SPIEGEL - Schlagzeilen - Tops";
		LOGGER.info("Titel={} (erwartet={})", rss.getVersion(), titelErwartet);
		assertEquals("Erwartet: Titel "+titelErwartet, titelErwartet, rss.getTitle());
	}

	/** Prüft, ob der Link zur Webseite korrekt ermittelt wird ({@linkplain RssInterface#getLink())}) */
	@Test
	public void testeLink() {
		String linkErwartet = "https://www.spiegel.de/";
		LOGGER.info("Link={} (erwartet={})", rss.getLink(), linkErwartet);
		assertEquals("Erwartet: Link "+linkErwartet, linkErwartet, rss.getLink());
	}

}

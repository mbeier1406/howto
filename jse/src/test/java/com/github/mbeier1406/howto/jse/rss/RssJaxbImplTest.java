package com.github.mbeier1406.howto.jse.rss;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.howto.jse.rss.RssInterface;
import com.github.mbeier1406.howto.jse.rss.RssJaxbImpl;

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

	/**
	 * Prüft ein bestimmtes Property aus {@linkplain #rss}.
	 * @param text Hinweis, welches Attribut überprüft wird
	 * @param expected der erwartete Wert
	 * @param s der Getter aus {@linkplain #rss}, der den zu überprüfenden Wert liefert
	 */
	@ParameterizedTest
	@MethodSource("rssArgs")
	public void testeRss(String text, Object expected, Supplier<?> s) {
		LOGGER.trace("text={}, expected={}, get={}", text, expected, s.get());
		assertEquals("Erwartet "+text+":"+expected, expected, s.get());
	}

	/**
	 * Liefert die Testwerte für {@linkplain #testeRss(String, Object, Supplier)} und
	 * erzeugt {@linkplain #rss} aus der Datei in {@value #RSS_FEED}. Folgende Testfälle
	 * werden überprüft:
	 * <ol>
	 * <li><u>Version</u>: Prüft, ob die Version korrekt ermittelt wird ({@linkplain RssInterface#getVersion()})</li>
	 * <li><u>Titel</u>: Prüft, ob der Titel korrekt ermittelt wird ({@linkplain RssInterface#getTitle())})</li>
	 * <li><u>Link</u>: Prüft, ob der Link zur Webseite korrekt ermittelt wird ({@linkplain RssInterface#getLink())})</li>
	 * </ol>
	 * @return den jeweiligen Testfall
	 * @throws Exception falls der Feed {@value #RSS_FEED} nicht eingelesen werden kann
	 */
	private static Stream<Arguments> rssArgs() throws Exception {
		rss = new RssJaxbImpl(RSS_FEED);
	    return Stream.of(
	    	Arguments.of("Version", new BigDecimal("2.0"), (Supplier<?>) rss::getVersion),
	    	Arguments.of("Titel", "DER SPIEGEL - Schlagzeilen - Tops", (Supplier<?>) rss::getTitle),
	    	Arguments.of("Link", "https://www.spiegel.de/", (Supplier<?>) rss::getLink),
	    	Arguments.of("description", "Deutschlands führende Nachrichtenseite.", (Supplier<?>) rss::getDescription)
	    );
	}

}

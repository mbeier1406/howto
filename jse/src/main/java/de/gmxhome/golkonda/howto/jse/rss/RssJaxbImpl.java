package de.gmxhome.golkonda.howto.jse.rss;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Eine Implementierung des {@linkplain RssInterface} auf Basis von JAXB2.
 * @author mbeier
 */
public class RssJaxbImpl implements RssInterface {

	public static final Logger LOGGER = LogManager.getLogger(RssJaxbImpl.class);

	/** Der Kontext wird nur einmal initialisiert. */
	private static final JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(TRss.class);
		} catch (JAXBException e) {
			throw new IllegalArgumentException("JAXB2-Kontext wurde nicht initialisiert!", e);
		}
	}

	/** Repräsentiert einen RSS-Feed, der aus einer vorgegebenen Datei eingelesen wird */
	private TRss tRss;

	/** Enthält die Informationen wie Tittel, Beschreibung, Link usw. aus {@linkplain #tRss} */
	private HashMap<QName, String> tRssInfo = new HashMap<>();

	/**
	 * Erzeugt einen {@linkplain TRss RSS-Feed} aus einer vorgegebenen Datei.
	 * @param dateiname Pfad zur XML-Datei mit dem RSS-Feed
	 * @throws Exception
	 */
	public RssJaxbImpl(String dateiname) throws Exception {
		LOGGER.trace("dateiname={}", dateiname);
		tRss = jaxbContext
				.createUnmarshaller()
				.unmarshal(new StreamSource(new File(dateiname)), TRss.class)
				.getValue();
		tRss.getChannel()
			.getTitleOrLinkOrDescription()
			.stream()
			.peek(LOGGER::trace)
			.forEach(
				o -> tRssInfo.put(((JAXBElement<?>) o).getName(), ((JAXBElement<?>) o).getValue().toString()));
	}

	/* TODO: weitere Konstruktoren mit verschiedenen Eingabequellen wie URL usw. */

	/** {@inheritDoc} */
	@Override
	public BigDecimal getVersion() {
		return tRss.getVersion();
	}

	/** {@inheritDoc} */
	@Override
	public String getTitle() {
		return tRssInfo.get(new QName("title"));
	}

}

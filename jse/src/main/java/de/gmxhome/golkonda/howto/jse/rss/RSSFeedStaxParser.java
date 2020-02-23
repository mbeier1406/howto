package de.gmxhome.golkonda.howto.jse.rss;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Liest einen RSS-Feed mit Hilfe des XML-Stax-Parsers in ein Objekt {@linkplain RssStaxFeedMessage} ein.
 * @author mbeier
 * @see RssInterface
 */
public class RSSFeedStaxParser {

	public static final Logger LOGGER = LogManager.getLogger(RSSFeedStaxParser.class);

	/** Die Adresse, von der der Feed {@linkplain RssStaxFeedMessage} eingelesen wird */
	private final URL rssFeedUrl;

	public RSSFeedStaxParser(URL url) {
		this.rssFeedUrl = url;
		LOGGER.debug("rssFeedUrl={}", rssFeedUrl);
	}

	public RSSFeedStaxParser(String url) throws MalformedURLException {
		this(new URL(url));
	}

	/**
	 * Liest einen RSS-Feed von der Adresse {@linkplain #rssFeedUrl} aus und wandelt sie in
	 * das Objekt {@linkplain RssStaxFeedMessage} um.
	 * @return den Feed als Objekt
	 * @throws Exception bei technischen Fehlern
	 */
	public RssStaxFeedMessage readRssFed() throws Exception {
		LOGGER.debug("lese rssFeedUrl={}", rssFeedUrl);
		RssStaxFeedMessage rssStaxFeedMessage = null;
		String title = "", link = "", description = "";
		BigDecimal version =  new BigDecimal("0.0");
		boolean rssTitleOrLinkOrDescriptionGelesen = false;
		try ( InputStream in = rssFeedUrl.openStream(); ) {
			XMLEventReader rssReader = XMLInputFactory.newInstance().createXMLEventReader(in);
			for ( ; rssReader.hasNext(); ) {
				XMLEvent event = rssReader.nextEvent();
				if ( event.isStartElement() ) {
					String localPart = event.asStartElement().getName().getLocalPart();
					LOGGER.debug("localPart={}", localPart);
					switch ( localPart ) {
						case "rss":
							Iterator<Attribute> attributes = event.asStartElement().getAttributes();
						    while( attributes.hasNext() ) {
						        Attribute att = attributes.next();
						        if ( att.getName().toString().equals("version") )
						            version = new BigDecimal(att.getValue());
						    }
							break;
						case "item":
							if ( !rssTitleOrLinkOrDescriptionGelesen ) {
								// Title, Link, Description jetzt gelesen -> Nachricht erzeugen
								rssStaxFeedMessage = new RssStaxFeedMessage(title, version, link, description);
								rssTitleOrLinkOrDescriptionGelesen = true;
							}
							event = rssReader.nextEvent();
							break;
						case "title":
							title = getCharacterData(event, rssReader).get();
							break;
						case "link":
							link = getCharacterData(event, rssReader).get();
							break;
						case "description":
							description = getCharacterData(event, rssReader).get();
							break;
					}
				}
				else if ( event.isEndElement() ) {
					if ( event.asEndElement().getName().getLocalPart().equals("item") ) {
						// ein Item wurde gelesen
						rssStaxFeedMessage.addRssStaxFeed(new RssStaxFeed(title, link, description));
						event = rssReader.nextEvent();
					}
				}
			}
			return rssStaxFeedMessage;
		}
		catch ( Exception e ) {
			LOGGER.error("rssFeedUrl={}", rssFeedUrl, e);
			throw e;
		}
	}

	/**
	 * Liefert den Inhalt eines XML-Tags
	 * @param event repräsentiert hier den Inhalt eines Tags 
	 * @param eventReader der Reader, der den RSS-Feed ausliedt
	 * @return {@code Optional.empty()}, falls das Tag keine zeichen enthielt, sonst ein {@code Optional} mit dem String
	 * @throws XMLStreamException falls das Tag nicht ausgelesen werdne kann
	 */
	private Optional<String> getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        try {
	        event = eventReader.nextEvent();
	        if ( event instanceof Characters )
	            return Optional.of(event.asCharacters().getData());
	        else
	        	return Optional.empty();
        }
        catch ( XMLStreamException e ) {
        	LOGGER.error("event={}", event, e);
        	throw e;
        }
    }

	public URL getRssFeedUrl() {
		return rssFeedUrl;
	}

	@Override
	public String toString() {
		return "RSSFeedStaxParser [rssFeedUrl=" + rssFeedUrl + "]";
	}

}

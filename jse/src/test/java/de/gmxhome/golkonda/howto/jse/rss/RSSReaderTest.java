package de.gmxhome.golkonda.howto.jse.rss;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RSSReaderTest {

	public static final Logger LOGGER = LogManager.getLogger(RSSReaderTest.class);

	@Test
	public void test() throws Exception {
		JAXBContext jaxbContext2 = JAXBContext.newInstance(TRss.class);
		Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
		TRss tRss = jaxbUnmarshaller2.unmarshal(new StreamSource(new File("src/test/resources/de/gmxhome/golkonda/howto/jse/rss/spiegel.rss.xml")), TRss.class).getValue();
		LOGGER.info("Version {}, Info {}, Title {}",
				tRss.getVersion(),
				tRss.getChannel().getOtherAttributes().toString(),
				tRss.getChannel().getTitleOrLinkOrDescription());
		for ( Object o : tRss.getChannel().getTitleOrLinkOrDescription() ) {
			LOGGER.info("o {} - {}", ((JAXBElement<?>) o).getValue(), ((JAXBElement<?>) o).getName());
		}
			
		
	}

	@Test
	public void testeRSSFeederAuslesen() throws Exception {
		String urlSpiegelTopStr = "https://www.spiegel.de/schlagzeilen/tops/index.rss";
		URL urlSpiegelTop = new URL(urlSpiegelTopStr);
		try ( InputStream inputStreamSpiegelTop = urlSpiegelTop.openStream(); ) {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStreamSpiegelTop);
			for ( ; eventReader.hasNext(); ) {
                XMLEvent event = eventReader.nextEvent();
            	LOGGER.trace("event={}", event);
                if (event.isStartElement()) {
                	
                }
                else if (event.isEndElement()) {
                	
                }
                else {
                }
			}
		}
	}

}

package de.gmxhome.golkonda.howto.jse.rss;

import java.io.File;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author mbeier
 *
 */
public class RssJaxbImpl implements RssInterface {

	public static final Logger LOGGER = LogManager.getLogger(RssJaxbImpl.class);

	private static final JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(TRss.class);
		} catch (JAXBException e) {
			throw new IllegalArgumentException();
		}
	}

	private TRss tRss;

	public RssJaxbImpl() throws Exception {
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		tRss = jaxbUnmarshaller.unmarshal(new StreamSource(new File("src/test/resources/de/gmxhome/golkonda/howto/jse/rss/spiegel.rss.xml")), TRss.class).getValue();
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getVersion() {
		return tRss.getVersion();
	}

}

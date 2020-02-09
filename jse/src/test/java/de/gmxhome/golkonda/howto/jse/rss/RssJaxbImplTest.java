package de.gmxhome.golkonda.howto.jse.rss;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class RssJaxbImplTest {

	public static final Logger LOGGER = LogManager.getLogger(RssJaxbImplTest.class);

	public static RssInterface rss;

	@BeforeClass
	public static void init() throws Exception {
		rss = new RssJaxbImpl();
	}
	
	@Test
	public void testeVersion() {
		String versionErwartet = "2.0";
		LOGGER.info("Version={} (erwartet={})", rss.getVersion(), versionErwartet);
		assertEquals("Erwartet: Version "+versionErwartet, new BigDecimal(versionErwartet), rss.getVersion());
	}

}

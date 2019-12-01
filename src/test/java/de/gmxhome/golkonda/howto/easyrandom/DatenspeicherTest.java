package de.gmxhome.golkonda.howto.easyrandom;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeasy.random.EasyRandom;
import org.junit.Test;

public class DatenspeicherTest {

	public static final Logger LOGGER = LogManager.getLogger(DatenspeicherTest.class);
	@Test
	public void testeDatenerzeugung() {
		EasyRandom generator = new EasyRandom();
		Datenspeicher datenspeicher = generator.nextObject(Datenspeicher.class);
		LOGGER.info("datenspeicher.getTestInteger()={}", datenspeicher.getTestInteger());
	    assertNotNull(datenspeicher.getTestInteger());
	}
}

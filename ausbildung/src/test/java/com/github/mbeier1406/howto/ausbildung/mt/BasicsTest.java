package com.github.mbeier1406.howto.ausbildung.mt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Basics}
 * @author mbeier
 */
public class BasicsTest {

	public static final Logger LOGGER = LogManager.getLogger(BasicsTest.class);

	/** Zeigt über das Logging die verschiedenen Arten der Threaderzeugung und das Setzen von Parametern */
	@Test
	public void showThreadCreation() {
		new Basics();
	}

}

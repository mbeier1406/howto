package com.github.mbeier1406.howto.ausbildung.gof.structural;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Test für die Proxy-Implementierung {@linkplain ProxyMetrics}.
 */
public class ProxyTest {

	public static final Logger LOGGER = LogManager.getLogger(ProxyTest.class);

	/** Das zu testende Objekt */
	public ProxyInterface proxy = new ProxyMetrics();

	/** Stellt sicher, dass die Initialisierung der eigentlichen Implementierung lazy ist */
	@Test
	public void testeLazyInitialisierung() {
		assertThat(((ProxyMetrics) proxy).getDELEGATE(), nullValue());
	}

	/** Stellt sicher, dass die Laufzeit eines Aufrufs vom Proxy gemessen wird */
	@Test
	public void testeZeitmessung() {
		assertThat(((ProxyMetrics) proxy).getLaufzeitLetzterAufruf(), equalTo(0L));
		proxy.method();
		long laufzeit = ((ProxyMetrics) proxy).getLaufzeitLetzterAufruf();
		assertThat(laufzeit, not(equalTo(0L)));
		LOGGER.info("Laufzeit: {}", laufzeit);
	}

}

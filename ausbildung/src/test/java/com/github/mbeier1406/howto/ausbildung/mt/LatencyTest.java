package com.github.mbeier1406.howto.ausbildung.mt;

import static com.github.mbeier1406.howto.ausbildung.mt.Latency.getArgb;
import static com.github.mbeier1406.howto.ausbildung.mt.Latency.ARGB.ALPHA;
import static com.github.mbeier1406.howto.ausbildung.mt.Latency.ARGB.GREEN;
import static com.github.mbeier1406.howto.ausbildung.mt.Latency.ARGB.BLUE;
import static com.github.mbeier1406.howto.ausbildung.mt.Latency.ARGB.RED;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Latency}.
 * @author mbeier
 */
public class LatencyTest {

	public static final Logger LOGGER = LogManager.getLogger(LatencyTest.class);

	/** Stellt sicher, dass der Alpha-Wert eines ARGB-Pixels korrekt ermittelt wird */
	@Test
	public void testeAlphaWert() {
		assertThat(Latency.getArgb(ALPHA, (int) 0x12345678), Matchers.equalTo(18));
	}

	/** Stellt sicher, dass der Rot-Wert eines ARGB-Pixels korrekt ermittelt wird */
	@Test
	public void testeRotWert() {
		assertThat(Latency.getArgb(RED, (int) 0x12345678), Matchers.equalTo(52));
	}

	/** Stellt sicher, dass der Grün-Wert eines ARGB-Pixels korrekt ermittelt wird */
	@Test
	public void testeGruenWert() {
		assertThat(getArgb(GREEN, (int) 0x12345678), Matchers.equalTo(86));
	}

	/** Stellt sicher, dass der Blau-Wert eines ARGB-Pixels korrekt ermittelt wird */
	@Test
	public void testeBlauWert() {
		assertThat(getArgb(BLUE, (int) 0x12345678), Matchers.equalTo(120));
	}

}

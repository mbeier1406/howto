package com.github.mbeier1406.howto.ausbildung.basic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.basic.Datatypes;

/**
 * Tests für {@linkplain Datatypes}
 * @author mbeier
 */
public class DatatypesTest {

	public static final Logger LOGGER = LogManager.getLogger(DatatypesTest.class);

	/** Prüft den maximalen Wert für Short */
	@Test
	public void testeGetMaxShort() {
		assertThat(Datatypes.getMaxShort(), equalTo((short) 32767));
	}

	/** Prüft den minimalen Wert für Short */
	@Test
	public void testeGetMinShort() {
		assertThat(Datatypes.getMinShort(), equalTo((short) -32768));
	}

	/** Prüft ob der maximale Wer Short plus eins den minimalen Wert für Short ergibt */
	@Test
	public void testeIntToShortMitIGroesserMaxShort() {
		assertThat(Datatypes.intToShort(Datatypes.getMaxShort()+(short) 1), equalTo(Datatypes.getMinShort()));
	}

	/** Prüft ob der minimale Wert für Short minus eins den maximalen Wert für Short ergibt */
	@Test
	public void testeIntToShortMitIKleinerMinShort() {
		assertThat(Datatypes.intToShort(Datatypes.getMinShort()-(short) 1), equalTo(Datatypes.getMaxShort()));
	}

}

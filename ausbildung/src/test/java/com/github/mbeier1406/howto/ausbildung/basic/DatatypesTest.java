package com.github.mbeier1406.howto.ausbildung.basic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.basic.impl.DatatypesImpl;

/**
 * Tests für die {@linkplain Datatypes} Implementierung {@linkplain DatatypesImpl}.
 * @author mbeier
 */
public class DatatypesTest {

	public static final Logger LOGGER = LogManager.getLogger(DatatypesTest.class);

	/** Das zu testende Objekt */
	public Datatypes datatypes;

	/** Initialisierung {@linkplain #datatypes} */
	@BeforeEach
	public void init() {
		datatypes = new DatatypesImpl();
	}

	/** Prüft den maximalen Wert für Short */
	@Test
	public void testeGetMaxShort() {
		assertThat(datatypes.getMaxShort(), equalTo((short) 32767));
	}

	/** Prüft den minimalen Wert für Short */
	@Test
	public void testeGetMinShort() {
		assertThat(datatypes.getMinShort(), equalTo((short) -32768));
	}

	/** Prüft ob der maximale Wer Short plus eins den minimalen Wert für Short ergibt */
	@Test
	public void testeIntToShortMitIGroesserMaxShort() {
		assertThat(datatypes.intToShort(datatypes.getMaxShort()+(short) 1), equalTo(datatypes.getMinShort()));
	}

	/** Prüft ob der minimale Wert für Short minus eins den maximalen Wert für Short ergibt */
	@Test
	public void testeIntToShortMitIKleinerMinShort() {
		assertThat(datatypes.intToShort(datatypes.getMinShort()-(short) 1), equalTo(datatypes.getMaxShort()));
	}

	/** Prüft, ob ein vorgegebener Codepoint/Unicode Character korrekt als UTF-16-Char konvertiert wird */
	@Test
	public void intToCharTest() {
		int codePoint = 4321;			// decimal codepoint unicode
		char unicodeChar = '\u10e1';	// hex unicode zum codepoint
		assertThat(String.valueOf(unicodeChar).codePointAt(0), equalTo(codePoint)); // Testdaten prüfen

		LOGGER.info("Prüfungen zu Codepoint {} (dec {}, char '{}')", "\\u"+Integer.toHexString(codePoint), codePoint, unicodeChar);
		char ch = datatypes.intToChar(codePoint);	// Funktion testen
		LOGGER.info("ch='{}' ({} -> {})", ch, Character.MIN_CODE_POINT, Character.MAX_CODE_POINT);

		assertThat(ch, equalTo(unicodeChar)); // ermittelten Charcter prüfen
		assertThat(ch, equalTo(Character.toChars(codePoint)[0])); // ermittelten Charcter prüfen
		assertThat(codePoint, equalTo(Character.toString(codePoint).codePointAt(0))); // Codepoint aus ermittelten Character prüfen
	}

	/**
	 * Demonstration Bitbelegung mit Vorzeichen bei Datentyp {@linkplain Byte} (auf <i>127</i> folgt <i>-128</i>)
	 * mit hexadezimaler Codierung der Konstanten.
	 * @see #testeIntToShortMitIGroesserMaxShort()
	  */
	@Test
	public void demonstrationByteUeberlaufHex() {
		LOGGER.info("(byte) 0x79='{}'", (byte) 0x79);
		LOGGER.info("(byte) 0x80='{}'", (byte) 0x80);
	}

	/**
	 * Demonstration Bitbelegung mit Vorzeichen bei Datentyp {@linkplain Byte} (auf <i>127</i> folgt <i>-128</i>)
	 * mit hexadezimaler Codierung der Konstanten.
	 * @see #testeIntToShortMitIGroesserMaxShort()
	  */
	@Test
	public void demonstrationShortUeberlaufBinaer() {
		LOGGER.info("(short) 0b01111111_11111111='{}'", (short) 0b01111111_11111111);
		LOGGER.info("(short) 0b10000000_00000000='{}'", (short) 0b10000000_00000000);
	}

}

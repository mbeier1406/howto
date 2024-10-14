package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface.Value;

/**
 * Test für die Klasse {@linkplain GanzzahlToken}.
 */
public class GanzzahlTokenTest {

	public static final Logger LOGGER = LogManager.getLogger(GanzzahlTokenTest.class);

	/** Das zu testende Objekt */
	public GanzzahlToken ganzzahlToken = new GanzzahlToken();


	/** Testet verschiedene Ganzzahlen */
	@ParameterizedTest
	@MethodSource("getTestdaten")
	public void testeEinlesen(String input, int ergebnis) {
		Value value = ganzzahlToken.read(input);
		LOGGER.info("input='{}'; value={}", input, value);
		assertThat(value.token().getClass(), equalTo(GanzzahlToken.class));
		assertThat(value.token().getValue().get(), equalTo(ergebnis));
		assertThat(value.length(), equalTo(String.valueOf(value.token().getValue().get()).length()));
	}

	/** Liefert die Testdaten für den parametrisierten Test {@linkplain #testeEinlesen(String, int)} */
	public static Stream<Arguments> getTestdaten() {
		return Stream.of(
				Arguments.of("12 3", 12),
				Arguments.of("123", 123),
				Arguments.of("1 ", 1),
				Arguments.of("0", 0));
	}


	/** Text muss mit einer Ziffer beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> new GanzzahlToken().read("abc") );
	}

/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> new GanzzahlToken().read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> new GanzzahlToken().read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Das Token zum Einlesen von {@linkplain GanzzahlToken} enthält selber keine Werte */
	@Test
	public void testeReaderToken() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> ganzzahlToken.getValue() );
		assertThat(ex.getMessage(), equalTo("Kein Token eingelesen!"));
	}

}

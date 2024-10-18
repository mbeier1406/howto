package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
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
	public static final GanzzahlToken GANZZAHL = new GanzzahlToken();


	/** Testet verschiedene Ganzzahlen */
	@ParameterizedTest
	@MethodSource("getTestdaten")
	public void testeEinlesen(String input, int ergebnis) {
		Value value = GANZZAHL.read(input);
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
		assertThrows(IllegalArgumentException.class, () -> GANZZAHL.read("abc") );
	}

/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> GANZZAHL.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> GANZZAHL.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Das Token zum Einlesen von {@linkplain GanzzahlToken} enthält selber keine Werte */
	@Test
	public void testeReaderToken() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> GANZZAHL.getValue() );
		assertThat(ex.getMessage(), equalTo("Kein Token eingelesen!"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(GANZZAHL.getSymbols(), not(equalTo(null)));
		assertThat(GANZZAHL.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(GANZZAHL.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain ganzzahlTokenToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(GANZZAHL.toString(), containsString(GANZZAHL.getClass().getSimpleName()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(GANZZAHL, equalTo(new GanzzahlToken()));
		assertThat(GANZZAHL, not(equalTo(new PlusToken())));
		assertThat(GANZZAHL, not(equalTo(new MinusToken())));
		assertThat(GANZZAHL, not(equalTo(new PeriodToken())));
		assertThat(GANZZAHL, not(equalTo(new DivisionToken())));
		assertThat(GANZZAHL, not(equalTo(new DezimalToken(0))));
		assertThat(GANZZAHL, not(equalTo(new CommaToken())));
	}

}

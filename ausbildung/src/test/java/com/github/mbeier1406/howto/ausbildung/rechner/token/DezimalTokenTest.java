package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.rechner.Lexer;

/**
 * Test für die Klasse {@linkplain DezimalToken}.
 */
public class DezimalTokenTest {

	/** Das zu testende Objekt */
	public static final DezimalToken DECIMAL = new DezimalToken(12.34);

	/** Dezimalzahlen werden nicht eingelesen sondern im {@linkplain Lexer} zusammengebaut */
	@Test
	public void testeEinlesen() {
		assertThrows(IllegalAccessError.class, () ->  DECIMAL.read(""));
	}

	/** Stellt sicher, dass das Token den korrekten Wert zurückgibt */
	@Test
	public void testeReaderToken() {
		assertThat(DECIMAL.getValue().get(), equalTo(12.34));
	}

	/** Dezimalzahlen werden nicht eingelesen sondern im {@linkplain Lexer} zusammengebaut */
	@Test
	public void testeSymbolListe() {
		assertThrows(IllegalAccessError.class, () -> DECIMAL.getSymbols());
	}

	/** Da es sich <u>nicht</u> um ein Einlese-Token handelt, darf es nicht entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(DezimalToken.class.getAnnotation(Token.class), nullValue()); 
	}

	/** {@linkplain DezimalToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(DECIMAL.toString(), containsString(DECIMAL.getClass().getSimpleName()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(DECIMAL, equalTo(new DezimalToken(12.34)));
		assertThat(DECIMAL, not(equalTo(new PlusToken())));
		assertThat(DECIMAL, not(equalTo(new MinusToken())));
		assertThat(DECIMAL, not(equalTo(new PeriodToken())));
		assertThat(DECIMAL, not(equalTo(new DivisionToken())));
		assertThat(DECIMAL, not(equalTo(new GanzzahlToken())));
		assertThat(DECIMAL, not(equalTo(new CommaToken())));
	}

}

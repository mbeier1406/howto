package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;
import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface.Value;

/**
 * Test für die Klasse {@linkplain DivisionToken}
 */
public class DivisionTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface DIVISION = new DivisionToken();

	/** Testet das '+'-Symbol alleine und mit folgenden Zeichen, muss immer das korrekte Token mit Länge 1 ergeben */
	@Test
	public void testeEinlesen() {
		Stream.of("/", "//", "/abc", "/ abc", "/123")
			.forEach(text -> {
				Value value = DIVISION.read(text);
				assertThat(value.token().getClass(), equalTo(DivisionToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '+'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> DIVISION.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> DIVISION.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> DIVISION.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(DIVISION.getSymbols(), not(equalTo(null)));
		assertThat(DIVISION.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(DIVISION.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain PlusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(DIVISION.toString(), containsString(DIVISION.getClass().getSimpleName()));
	}

	/** {@linkplain PlusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(DIVISION.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(DIVISION, equalTo(new DivisionToken()));
		assertThat(DIVISION, not(equalTo(new PlusToken())));
		assertThat(DIVISION, not(equalTo(new MinusToken())));
		assertThat(DIVISION, not(equalTo(new PeriodToken())));
		assertThat(DIVISION, not(equalTo(new GanzzahlToken())));
		assertThat(DIVISION, not(equalTo(new DezimalToken(0))));
		assertThat(DIVISION, not(equalTo(new CommaToken())));
	}

}

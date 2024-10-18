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
 * Test für die Klasse {@linkplain PeriodToken}
 */
public class PeriodTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface PERIOD = new PeriodToken();

	/** Testet das '*'-Symbol alleine und mit folgenden Zeichen, muss immer das korrekte Token mit Länge 1 ergeben */
	@Test
	public void testeEinlesen() {
		Stream.of("*", "**", "*abc", "* abc", "*123")
			.forEach(text -> {
				Value value = PERIOD.read(text);
				assertThat(value.token().getClass(), equalTo(PeriodToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '-'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> PERIOD.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> PERIOD.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> PERIOD.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(PERIOD.getSymbols(), not(equalTo(null)));
		assertThat(PERIOD.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(PERIOD.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain MinusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(PERIOD.toString(), containsString(PERIOD.getClass().getSimpleName()));
	}

	/** {@linkplain MinusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(PERIOD.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(PERIOD, equalTo(new PeriodToken()));
		assertThat(PERIOD, not(equalTo(new PlusToken())));
		assertThat(PERIOD, not(equalTo(new MinusToken())));
		assertThat(PERIOD, not(equalTo(new DivisionToken())));
		assertThat(PERIOD, not(equalTo(new GanzzahlToken())));
		assertThat(PERIOD, not(equalTo(new DezimalToken(0))));
		assertThat(PERIOD, not(equalTo(new CommaToken())));
	}

}

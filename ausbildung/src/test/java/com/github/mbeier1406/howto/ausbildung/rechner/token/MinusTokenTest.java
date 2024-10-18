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
 * Test für die Klasse {@linkplain MinusToken}
 */
public class MinusTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface MINUS = new MinusToken();

	/** Testet das '-'-Symbol alleine und mit folgenden Zeichen, muss immer das korrekte Token mit Länge 1 ergeben */
	@Test
	public void testeEinlesen() {
		Stream.of("-", "--", "-abc", "- abc", "-123")
			.forEach(text -> {
				Value value = MINUS.read(text);
				assertThat(value.token().getClass(), equalTo(MinusToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '-'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> MINUS.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> MINUS.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> MINUS.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(MINUS.getSymbols(), not(equalTo(null)));
		assertThat(MINUS.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(MINUS.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain MinusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(MINUS.toString(), containsString(MINUS.getClass().getSimpleName()));
	}

	/** {@linkplain MinusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(MINUS.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(MINUS, equalTo(new MinusToken()));
		assertThat(MINUS, not(equalTo(new PlusToken())));
		assertThat(MINUS, not(equalTo(new PeriodToken())));
		assertThat(MINUS, not(equalTo(new DivisionToken())));
		assertThat(MINUS, not(equalTo(new GanzzahlToken())));
		assertThat(MINUS, not(equalTo(new DezimalToken(0))));
		assertThat(MINUS, not(equalTo(new CommaToken())));
	}

}

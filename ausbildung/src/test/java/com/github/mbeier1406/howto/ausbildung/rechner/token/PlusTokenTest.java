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
 * Test für die Klasse {@linkplain PlusToken}
 */
public class PlusTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface PLUS = new PlusToken();

	/** Testet das '+'-Symbol alleine und mit folgenden Zeichen, muss immer das korrekte Token mit Länge 1 ergeben */
	@Test
	public void testeEinlesen() {
		Stream.of("+", "++", "+abc", "+ abc", "+123")
			.forEach(text -> {
				Value value = PLUS.read(text);
				assertThat(value.token().getClass(), equalTo(PlusToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '+'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> PLUS.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> PLUS.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> PLUS.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(PLUS.getSymbols(), not(equalTo(null)));
		assertThat(PLUS.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(PLUS.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain PlusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(PLUS.toString(), containsString(PLUS.getClass().getSimpleName()));
	}

	/** {@linkplain PlusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(PLUS.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(PLUS, equalTo(new PlusToken()));
		assertThat(PLUS, not(equalTo(new MinusToken())));
		assertThat(PLUS, not(equalTo(new PeriodToken())));
		assertThat(PLUS, not(equalTo(new DivisionToken())));
		assertThat(PLUS, not(equalTo(new GanzzahlToken())));
		assertThat(PLUS, not(equalTo(new DezimalToken(0))));
		assertThat(PLUS, not(equalTo(new CommaToken())));
	}

}

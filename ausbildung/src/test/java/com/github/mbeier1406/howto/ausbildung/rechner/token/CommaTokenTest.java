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
 * Test für die Klasse {@linkplain CommaToken}
 */
public class CommaTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface COMMA = new CommaToken();

	/** Testet das ','-Symbol in einer Dezimalzahl */
	@Test
	public void testeEinlesen() {
		Stream.of(",123", ", ", ",abc")
			.forEach(text -> {
				Value value = COMMA.read(text);
				assertThat(value.token().getClass(), equalTo(CommaToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '+'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> COMMA.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> COMMA.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> COMMA.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(COMMA.getSymbols(), not(equalTo(null)));
		assertThat(COMMA.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(COMMA.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain PlusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(COMMA.toString(), containsString(COMMA.getClass().getSimpleName()));
	}

	/** {@linkplain PlusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(COMMA.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(COMMA, equalTo(new CommaToken()));
		assertThat(COMMA, not(equalTo(new PlusToken())));
		assertThat(COMMA, not(equalTo(new MinusToken())));
		assertThat(COMMA, not(equalTo(new PeriodToken())));
		assertThat(COMMA, not(equalTo(new DivisionToken())));
		assertThat(COMMA, not(equalTo(new GanzzahlToken())));
		assertThat(COMMA, not(equalTo(new DezimalToken(0))));
	}

}

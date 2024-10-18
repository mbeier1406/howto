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
 * Test für die Klasse {@linkplain KlammeraufToken}
 */
public class KlammeraufTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface KLAMMERAUF = new KlammeraufToken();

	/** Testet das ','-Symbol in einer Dezimalzahl */
	@Test
	public void testeEinlesen() {
		Stream.of("(123", "( ", "(abc")
			.forEach(text -> {
				Value value = KLAMMERAUF.read(text);
				assertThat(value.token().getClass(), equalTo(KlammeraufToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '('-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> KLAMMERAUF.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> KLAMMERAUF.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> KLAMMERAUF.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(KLAMMERAUF.getSymbols(), not(equalTo(null)));
		assertThat(KLAMMERAUF.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(KLAMMERAUF.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain PlusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(KLAMMERAUF.toString(), containsString(KLAMMERAUF.getClass().getSimpleName()));
	}

	/** {@linkplain PlusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(KLAMMERAUF.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(KLAMMERAUF, equalTo(new KlammeraufToken()));
		assertThat(KLAMMERAUF, not(equalTo(new PlusToken())));
		assertThat(KLAMMERAUF, not(equalTo(new MinusToken())));
		assertThat(KLAMMERAUF, not(equalTo(new PeriodToken())));
		assertThat(KLAMMERAUF, not(equalTo(new DivisionToken())));
		assertThat(KLAMMERAUF, not(equalTo(new GanzzahlToken())));
		assertThat(KLAMMERAUF, not(equalTo(new DezimalToken(0))));
		assertThat(KLAMMERAUF, not(equalTo(new CommaToken())));
	}

}

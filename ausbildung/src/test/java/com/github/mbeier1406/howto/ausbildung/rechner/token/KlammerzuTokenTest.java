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
 * Test für die Klasse {@linkplain KlammerzuToken}
 */
public class KlammerzuTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface KLAMMERZU = new KlammerzuToken();

	/** Testet das ','-Symbol in einer Dezimalzahl */
	@Test
	public void testeEinlesen() {
		Stream.of(")123", ") ", ")abc")
			.forEach(text -> {
				Value value = KLAMMERZU.read(text);
				assertThat(value.token().getClass(), equalTo(KlammerzuToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem ')'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> KLAMMERZU.read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> KLAMMERZU.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> KLAMMERZU.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(KLAMMERZU.getSymbols(), not(equalTo(null)));
		assertThat(KLAMMERZU.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(KLAMMERZU.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain PlusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(KLAMMERZU.toString(), containsString(KLAMMERZU.getClass().getSimpleName()));
	}

	/** {@linkplain PlusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(KLAMMERZU.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(KLAMMERZU, equalTo(new KlammerzuToken()));
		assertThat(KLAMMERZU, not(equalTo(new PlusToken())));
		assertThat(KLAMMERZU, not(equalTo(new MinusToken())));
		assertThat(KLAMMERZU, not(equalTo(new PeriodToken())));
		assertThat(KLAMMERZU, not(equalTo(new DivisionToken())));
		assertThat(KLAMMERZU, not(equalTo(new GanzzahlToken())));
		assertThat(KLAMMERZU, not(equalTo(new DezimalToken(0))));
		assertThat(KLAMMERZU, not(equalTo(new CommaToken())));
		assertThat(KLAMMERZU, not(equalTo(new KlammeraufToken())));
	}

}

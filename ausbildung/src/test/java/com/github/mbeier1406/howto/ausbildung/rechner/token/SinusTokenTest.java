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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;
import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface.Value;

/**
 * Test für die Klasse {@linkplain SinusToken}
 */
public class SinusTokenTest {

	/** Das zu testende Objekt */
	public static final TokenInterface SINUS = new SinusToken();

	/** Testet das 'sin'-Symbol */
	@Test
	public void testeEinlesen() {
		Stream.of("sin", "Sin(", "SINxy")
			.forEach(text -> {
				Value value = SINUS.read(text);
				assertThat(value.token().getClass(), equalTo(SinusToken.class));
				assertThat(value.length(), equalTo(3));
			});
	}

	/** Text muss mit dem 'sin'-Symbol beginnen */
	@ParameterizedTest
	@MethodSource("getFehlehrhafteTestdaten")
	public void testeFalschesSymbol(String text) {
		assertThrows(IllegalArgumentException.class, () -> SINUS.read("text") );
	}

	public static Stream<Arguments> getFehlehrhafteTestdaten() {
		return Stream.of(Arguments.of("xSINy", "S", "Si" ));
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> SINUS.read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> SINUS.read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

	/** Da es sich um ein Einlesetoken handelt, müssen auch Erkennungssymbole definiert sein */
	@Test
	public void testeSymbolListe() {
		assertThat(SINUS.getSymbols(), not(equalTo(null)));
		assertThat(SINUS.getSymbols().length, greaterThan(0));
	}

	/** Da es sich um ein Einlese-Token handelt, muss es entsprechend annotiert sein */
	@Test
	public void testeAnnotation() {
		assertThat(SINUS.getClass().getAnnotation(Token.class), notNullValue()); 
	}

	/** {@linkplain SinusToken#toString()} soll den Klassennamen enthalten */
	@Test
	public void testeToString() {
		assertThat(SINUS.toString(), containsString(SINUS.getClass().getSimpleName()));
	}

	/** {@linkplain SinusToken#getValue()} soll nichts liefern */
	@Test
	public void testeGetValue() {
		assertThat(SINUS.getValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass die Token für die Operanden auseinander gehalten werden können */
	@Test
	public void testeEquals() {
		assertThat(SINUS, equalTo(new SinusToken()));
		assertThat(SINUS, not(equalTo(new PlusToken())));
		assertThat(SINUS, not(equalTo(new MinusToken())));
		assertThat(SINUS, not(equalTo(new PeriodToken())));
		assertThat(SINUS, not(equalTo(new DivisionToken())));
		assertThat(SINUS, not(equalTo(new GanzzahlToken())));
		assertThat(SINUS, not(equalTo(new DezimalToken(0))));
		assertThat(SINUS, not(equalTo(new CommaToken())));
	}

}

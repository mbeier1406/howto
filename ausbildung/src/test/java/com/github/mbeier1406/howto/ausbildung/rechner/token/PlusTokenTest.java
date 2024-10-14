package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface.Value;

/**
 * Test für die Klasse {@linkplain PlusToken}
 */
public class PlusTokenTest {

	/** Testet das '+'-Symbol alleine und mit folgenden Zeichen, muss immer das korrekte Token mit Länge 1 ergeben */
	@Test
	public void testeEinlesen() {
		Stream.of("+", "++", "+abc", "+ abc", "+123")
			.forEach(text -> {
				Value value = new PlusToken().read(text);
				assertThat(value.token().getClass(), equalTo(PlusToken.class));
				assertThat(value.length(), equalTo(1));
			});
	}

	/** Text muss mit dem '+'-Symbol beginnen */
	@Test
	public void testeFalschesSymbol() {
		assertThrows(IllegalArgumentException.class, () -> new PlusToken().read("abc") );
	}

	/** Text darf nicht leer sein */
	@Test
	public void testeLeererTextl() {
		assertThrows(IllegalArgumentException.class, () -> new PlusToken().read("") );
	}

	/** Text darf nicht <b>null</b> sein */
	@Test
	public void testeNullTextl() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> new PlusToken().read(null) );
		assertThat(ex.getMessage(), containsString("text"));
	}

}

package com.github.mbeier1406.howto.ausbildung.rechner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenReader.Value;

/**
 * Tests für die Klasse {@linkplain TokenReader};
 */
public class TokenReaderTest {

	/** Stellt sicher, dass ein gelesener Token nicht <b>null</b> ist und die Fehlermeldung passt */
	@Test
	public void testeTokenValueNull() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> new TokenReader.Value(null, 1));
		assertThat(ex.getMessage(), containsString("value"));
	}

	/** Stellt sicher, dass die Länge des gelesenen Tokens nicht null oder negativ ist */
	@Test
	public void testeTokenLaengeGroesserNull() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new TokenReader.Value(new Object(), 0));
		assertThat(ex.getMessage(), containsString("<= 0"));
	}

}

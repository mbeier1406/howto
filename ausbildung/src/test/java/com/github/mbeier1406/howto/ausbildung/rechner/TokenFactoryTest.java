package com.github.mbeier1406.howto.ausbildung.rechner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;

/**
 * Tests für die Klasse {@linkplain TokenFactory}.
 */
public class TokenFactoryTest {

	public static final Logger LOGGER = LogManager.getLogger(TokenFactoryTest.class);

	/** Stellt sicher, dass die Factory {@linkplain TokenInterface Token} lädt und testet eine Symbolzuordnung */
	@Test
	public void testeToeknListeErzeugen() {
		Map<Character, TokenInterface> tokens = TokenFactory.getTokens();
		LOGGER.info("tokens={}", tokens);
		assertThat(tokens, notNullValue());
		assertThat(tokens.size(), greaterThan(0));
		assertThat(tokens.keySet(), hasItems((Character) (new GanzzahlToken().getSymbols()[0])));
	}

}

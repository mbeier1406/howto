package com.github.mbeier1406.howto.ausbildung.rechner;

import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.LIST_OF_BLANKS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.howto.ausbildung.rechner.Lexer.LexerException;
import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.MinusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.Token;

/**
 * Tests für die Klasse {@linkplain LexerImpl}.
 */
public class LexerImplTest {

	public static Logger LOGGER = LogManager.getLogger(LexerImplTest.class);

	public static final TokenInterface PLUS_TOKEN = new PlusToken();
	public static final TokenInterface MINUS_TOKEN = new MinusToken();
	public static final TokenInterface Z123 = new GanzzahlToken(123);


	/** Das zu testende Objekt */
	public Lexer lexer = new LexerImpl();


	/** Prüft, ob bei einer NULL-Text die korrekte Exception geworfen wird*/
	@Test
	public void testeNullText() throws LexerException {
		assertThrows(LexerException.class, () -> lexer.getTokens(null));
	}

	/** Stellt sicher, dass bei einem leeren Text eine leere Liste als Ergebnis */
	@Test
	public void testeLeerenText() throws LexerException {
		assertThat(lexer.getTokens(""), equalTo(new ArrayList<>()));
	}

	/** Stellt sicher, dass bei einem Text aus Leerstellen eine leere Liste als Ergebnis */
	@Test
	public void testeLeerzeichenText() throws LexerException {
		assertThat(
				lexer.getTokens(Lexer
						.LIST_OF_BLANKS
						.stream()
						.map(String::valueOf)
						.collect(Collectors.joining())),
				equalTo(new ArrayList<>()));
	}


	/**
	 * Prüft für <b>korrekte</b> gegebene Zeichenketten, ob die korrekte Liste der {@linkplain Token} geliefert wird.
	 * @see #getKorrekteTestdaten()
	 */
	@ParameterizedTest
	@MethodSource("getKorrekteTestdaten")
	public void testeKorrekteDaten(String input, List<TokenInterface> expected) throws LexerException {
		assertThat(lexer.getTokens(input), equalTo(expected));
	}

	/** Liefert die Testdaten für den parametrisierten Test {@linkplain #testeKorrekteDaten(String, List)} */
	@SuppressWarnings("serial")
	public static Stream<Arguments> getKorrekteTestdaten() {
		return Stream.of(
				Arguments.of("123 +123", new ArrayList<TokenInterface>() {{ add(Z123); add(Z123); }}),
				Arguments.of("123+ 123", new ArrayList<TokenInterface>() {{ add(Z123); add(PLUS_TOKEN); add(Z123); }}),
				Arguments.of("123 + 123", new ArrayList<TokenInterface>() {{ add(Z123); add(PLUS_TOKEN); add(Z123); }}),
				Arguments.of("123+123", new ArrayList<TokenInterface>() {{ add(Z123); add(PLUS_TOKEN); add(Z123); }}),
				Arguments.of("++123", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(Z123); }}),
				Arguments.of("+ +123", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(Z123); }}),
				Arguments.of("- 123", new ArrayList<TokenInterface>() {{ add(MINUS_TOKEN); add(Z123); }}),
				Arguments.of("+ 123", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(Z123); }}),
				Arguments.of("-123", new ArrayList<TokenInterface>() {{ add(new GanzzahlToken(-123)); }}),
				Arguments.of("+123", new ArrayList<TokenInterface>() {{ add(Z123); }}),
				Arguments.of("123", new ArrayList<TokenInterface>() {{ add(Z123); }}),
				Arguments.of("+"+LIST_OF_BLANKS.get(0)+"--", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(MINUS_TOKEN); add(MINUS_TOKEN); }}),
				Arguments.of("+-+"+LIST_OF_BLANKS.get(1), new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(MINUS_TOKEN); add(PLUS_TOKEN); }}),
				Arguments.of(LIST_OF_BLANKS.get(1)+"-+-", new ArrayList<TokenInterface>() {{ add(MINUS_TOKEN); add(PLUS_TOKEN); add(MINUS_TOKEN); }}),
				Arguments.of("--", new ArrayList<TokenInterface>() {{ add(MINUS_TOKEN); add(MINUS_TOKEN); }}),
				Arguments.of("-", new ArrayList<TokenInterface>() {{ add(MINUS_TOKEN); }}),
				Arguments.of("+++", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(PLUS_TOKEN); add(PLUS_TOKEN); }}),
				Arguments.of("++", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); add(PLUS_TOKEN); }}),
				Arguments.of("+", new ArrayList<TokenInterface>() {{ add(PLUS_TOKEN); }}),
				Arguments.of("	 ", new ArrayList<TokenInterface>()));
	}


	/**
	 * Prüft für <b>fehlerhafte</b> gegebene Zeichenketten, ob die korrekte Exception geworfen wird.
	 * @see #getKorrekteTestdaten()
	 */
	@ParameterizedTest
	@MethodSource("getFehlerhafteTestdaten")
	public void testeFehlerhafteDaten(String input, String fehlerText) throws LexerException {
		LexerException exception = assertThrows(LexerException.class, () -> lexer.getTokens(input));
		assertThat(exception.getMessage(), equalTo(fehlerText));
	}

	/** Liefert die Testdaten für den parametrisierten Test {@linkplain #testeFehlerhafteDaten(String, String)} */
	public static Stream<Arguments> getFehlerhafteTestdaten() {
		return Stream.of(
				Arguments.of("+a", "text='+a': Unbekanntes Symbol 'a'; index=1"),
				Arguments.of("a", "text='a': Unbekanntes Symbol 'a'; index=0"));
	}


}

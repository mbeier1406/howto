package com.github.mbeier1406.howto.ausbildung.rechner;

import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.PLUS_TOKEN;
import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.TokenTyp.GANZZAHL;
import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.MINUS_TOKEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.howto.ausbildung.rechner.Lexer.LexerException;
import com.github.mbeier1406.howto.ausbildung.rechner.Lexer.Token;

/**
 * Tests für die Klasse {@linkplain LexerImpl}.
 */
public class LexerImplTest {

	public static Logger LOGGER = LogManager.getLogger(LexerImplTest.class);

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


	/**
	 * Prüft für <b>korrekte</b> gegebene Zeichenketten, ob die korrekte Liste der {@linkplain Token} geliefert wird.
	 * @see #getKorrekteTestdaten()
	 */
	@ParameterizedTest
	@MethodSource("getKorrekteTestdaten")
	public void testeKorrekteDaten(String input, List<Token> expected) throws LexerException {
		assertThat(lexer.getTokens(input), equalTo(expected));
	}

	/** Liefert die Testdaten für den parametrisierten Test {@linkplain #testeKorrekteDaten(String, List)} */
	@SuppressWarnings("serial")
	public static Stream<Arguments> getKorrekteTestdaten() {
		return Stream.of(
				Arguments.of("+--", new ArrayList<Token>() {{ add(PLUS_TOKEN); add(MINUS_TOKEN); add(MINUS_TOKEN); }}),
				Arguments.of("+-+", new ArrayList<Token>() {{ add(PLUS_TOKEN); add(MINUS_TOKEN); add(PLUS_TOKEN); }}),
				Arguments.of("-+-", new ArrayList<Token>() {{ add(MINUS_TOKEN); add(PLUS_TOKEN); add(MINUS_TOKEN); }}),
				Arguments.of("--", new ArrayList<Token>() {{ add(MINUS_TOKEN); add(MINUS_TOKEN); }}),
				Arguments.of("-", new ArrayList<Token>() {{ add(MINUS_TOKEN); }}),
				Arguments.of("+++", new ArrayList<Token>() {{ add(PLUS_TOKEN); add(PLUS_TOKEN); add(PLUS_TOKEN); }}),
				Arguments.of("++", new ArrayList<Token>() {{ add(PLUS_TOKEN); add(PLUS_TOKEN); }}),
				Arguments.of("+", new ArrayList<Token>() {{ add(PLUS_TOKEN); }}));
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
				Arguments.of("+a", "text=+a: Ungültiges Zeichen 'a' an Position 1!"),
				Arguments.of("a", "text=a: Ungültiges Zeichen 'a' an Position 0!"));
	}


	/** Prüfungen der Methode {@linkplain LexerImpl#getOperandOrNumber(String, int, List, Token, int)} */
	@Test
	public void testGetOperandOrNumber() {
		var l = new LexerImpl();
		String text = "++123--456";
		int i = 0;
		final List<Token> listOfTokens = new ArrayList<>();
		i = l.getOperandOrNumber(text, i, listOfTokens, PLUS_TOKEN, 1); // Token '+' einlesen
		validateGetOperandOrNumber(listOfTokens, 1, PLUS_TOKEN, i, 0);
		i = l.getOperandOrNumber(text, ++i, listOfTokens, PLUS_TOKEN, 1); // Token '+123' einlesen
		validateGetOperandOrNumber(listOfTokens, 2, new Token(GANZZAHL, 123), i, 4);
		i = l.getOperandOrNumber(text, ++i, listOfTokens, MINUS_TOKEN, -1); // Token '-' einlesen
		validateGetOperandOrNumber(listOfTokens, 3, MINUS_TOKEN, i, 5);
		i = l.getOperandOrNumber(text, ++i, listOfTokens, MINUS_TOKEN, -1); // Token '-456' einlesen
		validateGetOperandOrNumber(listOfTokens, 4, new Token(GANZZAHL, -456), i, 9);
		i = l.getOperandOrNumber(text+"+", ++i, listOfTokens, PLUS_TOKEN, 1); // Token '+' am Ende der Formel einlesen
		validateGetOperandOrNumber(listOfTokens, 5, PLUS_TOKEN, i, 10);
	}

	/** Prüfung für das Auslesen in {@linkplain #testGetOperandOrNumber()} */
	public void validateGetOperandOrNumber(final List<Token> listOfTokens, int anzahlTokenErwartet, final Token tokenErwartet, int neuerIndex, int indexErwartet) {
		assertThat(neuerIndex, equalTo(indexErwartet)); // Index, an der die Verarbeitung forgesetzt werdne musss, prüfen
		assertThat(listOfTokens.size(), equalTo(anzahlTokenErwartet)); // Anzahl Tokens prüfen
		assertThat(listOfTokens.get(listOfTokens.size()-1), equalTo(tokenErwartet)); // zuletzt hinzugefügten Token prüfen
	}

}

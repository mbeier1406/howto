package com.github.mbeier1406.howto.ausbildung.rechner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.howto.ausbildung.rechner.Parser.ParserException;
import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.MinusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;

/**
 * Tests für die Klasse {@linkplain ParserImpl}.
 */
public class ParserImplTest {

	public static final Logger LOGGER = LogManager.getLogger(ParserImplTest.class);

	public static final TokenInterface PLUS = new PlusToken();
	public static final TokenInterface MINUS = new MinusToken();
	public static final TokenInterface  EINS = new GanzzahlToken(1);
	public static final TokenInterface  DREI = new GanzzahlToken(3);
	public static final TokenInterface  MINUS_ZWEI = new GanzzahlToken(-2);

	/** Das zu testende Objekt */
	public Parser parser = new ParserImpl();


	/** Testet den berechneten Wert korrekter mathematischer Formeln */
	@ParameterizedTest
	@MethodSource("getKorrekteTestdaten")
	public void testeKorrekteFormel(final List<TokenInterface> listOfTokens, double erwartetesErgebnis) {
		try {
			double ergebnis = parser.evaluate(listOfTokens);
			LOGGER.trace("Formael {}: erwartet {}; ist {}", listOfTokens, erwartetesErgebnis, ergebnis);
			assertThat(ergebnis, equalTo(erwartetesErgebnis));
		} catch (ParserException e) {
			LOGGER.trace("Formael {}: erwartet {}", listOfTokens, erwartetesErgebnis, e);
			throw new RuntimeException();
		}
	}

	/** Liefert die syntaktisch korrekten Testfälle */
	@SuppressWarnings("serial")
	public static Stream<Arguments> getKorrekteTestdaten() {
		return Stream.of(
				Arguments.of(new ArrayList<TokenInterface>() {{ add(EINS); add(PLUS); add(EINS); add(MINUS); add(MINUS_ZWEI); }}, (double) 4),
				Arguments.of(new ArrayList<TokenInterface>() {{ add(EINS); add(PLUS); add(EINS); add(MINUS); add(DREI); }}, (double) -1),
				Arguments.of(new ArrayList<TokenInterface>() {{ add(EINS); add(PLUS); add(MINUS_ZWEI); }}, (double) -1),
				Arguments.of(new ArrayList<TokenInterface>() {{ add(EINS); add(PLUS); add(EINS); }}, (double) 2),
				Arguments.of(new ArrayList<TokenInterface>() {{ add(new GanzzahlToken(123)); }}, (double) 123));
	}


	/** Stellt sicher, dass fehlerhafte mathematische Formeln die korrekte Exception erzeugen */
	@ParameterizedTest
	@MethodSource("getFehlerhafteTestdaten")
	public void testeFehlerhafteFormel(final List<TokenInterface> listOfTokens, final Exception exceptionErwartet) {
		var exception = assertThrows(ParserException.class, () -> parser.evaluate(listOfTokens));
		assertThat(exception.getClass(), equalTo(exceptionErwartet.getClass()));
		assertThat(exception.getMessage(), containsString(exceptionErwartet.getMessage()));
	}

	/** Liefert die syntaktisch fehlerhaften Testfälle */
	@SuppressWarnings("serial")
	public static Stream<Arguments> getFehlerhafteTestdaten() {
		return Stream.of(
			Arguments.of(new ArrayList<TokenInterface>() {{ add(EINS); add(MINUS_ZWEI); }}, new ParserException("'GanzzahlToken [value=-2]' an Index 2")),
			Arguments.of(new ArrayList<TokenInterface>() {{ add(EINS); add(PLUS); add(MINUS); }}, new ParserException("'MinusToken '-'' an Index 3")),
			Arguments.of(new ArrayList<TokenInterface>() {{ add(PLUS); }}, new ParserException("'PlusToken '+'' an Index 1")));
	}

}

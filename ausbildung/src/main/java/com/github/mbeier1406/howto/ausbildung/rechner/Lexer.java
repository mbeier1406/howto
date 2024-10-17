package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.Arrays;
import java.util.List;

/**
 * Lexikalische Analsyse für den Taschenrechner.
 */
public interface Lexer {

	/** Liste der Leerzeichen, die in der lexikalischen Analyse übersprungen werden */
	public static final List<Character> LIST_OF_BLANKS = Arrays.asList(' ', '\t');

	/** Damit Token ohne eigenen Wert mittels {@code equals()} verglichen/untersc hieden werdne können */
	public static enum STD_TOKEN_HASHCODES {
		PLUS_TOKEN_HASHCODE,
		MINUS_TOKEN_HASHCODE,
		PERIOD_TOKEN_HASHCODE,
		DIVISION_TOKEN_HASHCODE,
		COMMA_TOKEN_HASHCODE
	};

	/** Eine solche Exception wird geworfen, wenn in der Eingabeformel ungültige Token verwendet werden */
	public static class LexerException extends Exception {
		private static final long serialVersionUID = -5793180396296325508L;
		public LexerException() { super(); }
		public LexerException(String msg) { super(msg); }
		public LexerException(String msg, Throwable t) { super(msg, t); }
	}

	/**
	 * Lexikalische Analyse der Eingabeformel: Erzeugt eine Liste von Tokens aus
	 * der zu berechendenen, mathematischen Formel. Zzum Beispiel würde aus der
	 * Formel {@code 5+13*2} eine List mit fünf Token (drei Zahlen, zwei Operanden).
	 * @param text die Formel
	 * @return die Liste der Tokens
	 * @throws LexerException
	 */
	public List<TokenInterface> getTokens(String text) throws LexerException;

}

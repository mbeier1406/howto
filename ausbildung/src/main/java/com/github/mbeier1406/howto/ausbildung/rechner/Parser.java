package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.List;

/**
 * Definiert das Interface zu dem Service, der die syntaktische Korrektheit
 * der eingegebenen Formel (siehe {@linkplain Lexer#getTokens(String)}) prüft
 * und den Ausdruck auswertet.
 * @see TokenInterface
 */
public interface Parser {

	/** Eine solche Exception wird geworfen, wenn die Eingabeformel syntaktisch inkorrekt ist */
	public static class ParserException extends Exception {
		private static final long serialVersionUID = -5793180396296325508L;
		public ParserException() { super(); }
		public ParserException(String msg) { super(msg); }
		public ParserException(String msg, Throwable t) { super(msg, t); }
	}

	/**
	 * Prüft die Liste der gelesenen Tokens auf syntaktische Korrektheit und
	 * wertet den Ausdruck aus, indem dessen Resultat berec hnet und zurückgegeben wird.
	 * @param listOfTokens die mathematisch eFormal als Liste von Tokens
	 * @return das Ergebnis der Auswertung als Fließkommazahl
	 * @throws ParserException bei syntaktischen Fehlern
	 */
	public double evaluate(final List<TokenInterface> listOfTokens) throws ParserException;

}

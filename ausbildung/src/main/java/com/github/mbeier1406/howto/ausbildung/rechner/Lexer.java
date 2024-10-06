package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.List;

/**
 * Lexikalische Analsyse für den Taschenrechner.
 */
public interface Lexer {

	/** Das Plus-Zeichen <b>{@value}</b> für die Addition */
	public static final char PLUS_SIGN = '+';
	/** Das Minus-Zeichen <b>{@value}</b> für die Subtraktion */
	public static final char MINUS_SIGN = '-';

	/** Eingabeformel des Taschenrechners verwendet ausschließlich diese Arten von Token */
	public enum TokenTyp {
		PLUS(String.valueOf(PLUS_SIGN)),
		MINUS(String.valueOf(MINUS_SIGN)),
		GANZZAHL("Ganzzahl");
		private String symbol;
		private TokenTyp(String symbol) {
			this.symbol = symbol;
		}
		public String getSymbol() {
			return this.symbol;
		}
	};

	/** Definiert einen Token aus der Eingabeformel */
	public record Token (TokenTyp tokenTyp, Object value) {
		public Token(TokenTyp tokenTyp, Object value) {
			this.tokenTyp = tokenTyp;
			switch ( tokenTyp ) {
				case PLUS:
				case MINUS:
					if ( value != null ) throw new IllegalArgumentException("Token "+tokenTyp.getSymbol()+" erwartet keinen Parameter: '"+value+"'");
					break;
				case GANZZAHL:
					if ( value == null ) throw new IllegalArgumentException("Token "+tokenTyp.getSymbol()+" erwartet einen Parameter!");
					break;
				default:
			}
			this.value = value;
		}
	};

	/** Eine solche Exception wird geworfen, wenn in der Eingabeformel ungültige Token verwendet werden */
	public static class LexerException extends Exception {
		private static final long serialVersionUID = -5793180396296325508L;
		public LexerException() { super(); }
		public LexerException(String msg) { super(msg); }
		public LexerException(String msg, Throwable t) { super(msg, t); }
	}

	/** Das Token für die Addition */
	public static final Token PLUS_TOKEN = new Token(TokenTyp.PLUS, null);
	/** Das Token für die Subtraktion */
	public static final Token MINUS_TOKEN = new Token(TokenTyp.MINUS, null);

	/**
	 * Lexikalische Analyse der Eingabeformel: Erzeugt eine Liste von Tokens aus
	 * der zu berechendenen, mathematischen Formel. Zzum Beispiel würde aus der
	 * Formel {@code 5+13*2} eine List mit fünf Token (drei Zahlen, zwei Operanden).
	 * @param text die Formel
	 * @return die Liste der Tokens
	 * @throws LexerException
	 */
	public List<Token> getTokens(String text) throws LexerException;

}

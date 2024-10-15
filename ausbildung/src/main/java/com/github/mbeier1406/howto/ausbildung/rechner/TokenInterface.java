package com.github.mbeier1406.howto.ausbildung.rechner;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * Definiert das Interface zu einem Token, aus denen die
 * auszuwertende mathematische Formel besteht (zum Beispiel
 * Zahlen, arithmetische Operationen usw.);
 */
public interface TokenInterface {

	/** Das Ergebnis des Einlesens eines Tokens: der Toen selber und seine textuelle Länge */
	public static record Value(TokenInterface token, int length) {
		public Value(TokenInterface token, int length) {
			this.token = requireNonNull(token, "token");
			this.length = length;
			if ( length <= 0) throw new IllegalArgumentException("length <= 0: "+this.length);
		}
	}

	/** Liest einen Token ein */
	public Value read(String text);

	/**
	 * Liefert die Liste der Zeichen, an der ein Token (als
	 * erstes Zeichen der Zeichenkette, aus der das Token besteht)
	 * erkannt wird. Zum Beispiel '+' für die Addition, oder [0-9] für eine Zahl.
	 * @return Liste der Zeichen, darf nicht <b>null</b> oder leer sein!
	 */
	public char[] getSymbols();

	/**
	 * Der Wert des Tokens, sofern es sich um eine Zahlenwert handelt. Darf nicht <b>null</b> liefern!
	 * @return {@linkplain Optional#empty()} bei mathematischen Operationen, ansonsten der Zahlenwert
	 */
	public Optional<Object> getValue();

}

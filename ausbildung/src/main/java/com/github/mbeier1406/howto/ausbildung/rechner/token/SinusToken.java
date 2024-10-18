package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.STD_TOKEN_HASHCODES.SIN_TOKEN_HASHCODE;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.github.mbeier1406.howto.ausbildung.rechner.ParserImpl;
import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;

/**
 * Das Token für die Sinus-Funktion (wird im {@linkplain ParserImpl} von Radiant in Grad umgerechnet.
 */
@Token
public class SinusToken implements TokenInterface {

	private static final String SYMBOL = "sin";
	private static final char[] SYMBOL_LIST = new char[] { SYMBOL.toLowerCase().charAt(0), SYMBOL.toUpperCase().charAt(0) };


	/** {@inheritDoc} */
	@Override
	public Value read(String text) {
		if ( requireNonNull(text, "text").length() < 3 || !text.substring(0, 3).equalsIgnoreCase(SYMBOL) )
			throw new IllegalArgumentException("Literal erwartet: 'sin'");
		return new Value(this, SYMBOL.length()); // 'sin'-Token der Länge 3
	}

	/** {@inheritDoc} */
	@Override
	public char[] getSymbols() {
		return SYMBOL_LIST;
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Object> getValue() {
		return Optional.empty();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" '"+SYMBOL+"'";
	}

	@Override
	public int hashCode() {
		return SIN_TOKEN_HASHCODE.ordinal(); // gibt nur ein 'sin'
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) // gleich Klasse, gleiches Objekt
			return false;
		return true;
	}

}

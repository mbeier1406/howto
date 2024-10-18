package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.STD_TOKEN_HASHCODES.MINUS_TOKEN_HASHCODE;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;

/**
 * Das Token für die Subtraktion.
 */
@Token
public class MinusToken implements TokenInterface {

	private static final char SYMBOL = '-';
	private static final char[] SYMBOL_LIST = new char[] { SYMBOL };

	/** {@inheritDoc} */
	@Override
	public Value read(String text) {
		if ( requireNonNull(text, "text").length() < 1 || text.charAt(0) != SYMBOL )
			throw new IllegalArgumentException("'"+SYMBOL+"' erwartet: '"+text+"'");
		return new Value(this, 1); // '-'-Token der Länge 1
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
		return MINUS_TOKEN_HASHCODE.ordinal(); // gibt nur ein -
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

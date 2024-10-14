package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;

/**
 * Das Token für die Subtraktion.
 */
@Token
public class MinusToken implements TokenInterface {

	private static final char[] SYMBOL_LIST = new char[] { '+' };

	/** {@inheritDoc} */
	@Override
	public Value read(String text) {
		if ( requireNonNull(text, "text").length() < 1 || text.charAt(0) != '-' )
			throw new IllegalArgumentException("'+' erwartet: '"+text+"'");
		return new Value(this, 1); // '-'-Token der Länge 1
	}

	/** {@inheritDoc} */
	@Override
	public char[] getSymbol() {
		return SYMBOL_LIST;
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Object> getValue() {
		return Optional.empty();
	}

	@Override
	public String toString() {
		return "MinusToken '-'";
	}

}

package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;
import com.google.common.primitives.Chars;

/**
 * Das Token für eine ganze Zahl.
 */
@Token
public class GanzzahlToken implements TokenInterface {

	private static final char[] SYMBOL_LIST = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/** {@value #SYMBOL_LIST} als Liste zum schnellen Suchen */
	private static List<Character> SYMBOLS;

	static {
		SYMBOLS = Chars.asList(SYMBOL_LIST);
	}

	/** Wert des Tokens */
	private final Integer value;

	/** {@inheritDoc} */
	@Override
	public Value read(String text) {
		if ( requireNonNull(text, "text").length() < 1 || !SYMBOLS.contains(text.charAt(0)) )
			throw new IllegalArgumentException("Ziffer erwartet: '"+text+"'");
		final StringBuilder sb = new StringBuilder(String.valueOf(text.charAt(0)));
		while ( sb.length() < text.length() && text.charAt(sb.length()) >= '0' && text.charAt(sb.length()) <= '9' )
			sb.append(text.charAt(sb.length()));
		return new Value(new GanzzahlToken(Integer.parseInt(sb.toString())), sb.length()); // Ganzzahl mit x Ziffern
	}

	/** {@inheritDoc} */
	@Override
	public char[] getSymbols() {
		return SYMBOL_LIST;
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Object> getValue() {
		return Optional.of(requireNonNull(value, "Kein Token eingelesen!"));
	}

	/** Konstruktor, um ein Ganzzahl-Token einlesen zu können */
	public GanzzahlToken() { this.value = null; }

	/** Konstruktor zur Erzeugung eines Tokens */
	private GanzzahlToken(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "GanzzahlToken [value=" + value + "]";
	}

}

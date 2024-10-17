package com.github.mbeier1406.howto.ausbildung.rechner.token;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import com.github.mbeier1406.howto.ausbildung.rechner.Lexer;
import com.github.mbeier1406.howto.ausbildung.rechner.TokenInterface;

/**
 * Das Token für eine Dezimalzahl. Es ist nicht mit {@code @Token} annotiert,
 * da es nie automatisch gelesen wird, sondern sich aus zwei Ganzzahlen ({@linkplain GanzzahlToken})
 * und dem trennenden Komma ({@linkplain CommaToken} zusammensetzt und im {@linkplain Lexer}
 * zusammengesetzt wird.
 */
public class DezimalToken implements TokenInterface {

	/** Wert des Tokens */
	private Double value;

	/** {@inheritDoc} */
	@Override
	public Value read(String text) {
		throw new IllegalAccessError("Dezimaltoken werdne über den Konstruktor erzeugt.");
	}

	/** {@inheritDoc} */
	@Override
	public char[] getSymbols() {
		throw new IllegalAccessError("Dezimaltoken werdne über den Konstruktor erzeugt.");
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Object> getValue() {
		return Optional.of(requireNonNull(value, "Kein Token erzeugt!"));
	}

	/** Konstruktor zur Erzeugung eines Tokens */
	public DezimalToken(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DezimalToken [value=" + value + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DezimalToken other = (DezimalToken) obj;
		return Objects.equals(value, other.value);
	}

}

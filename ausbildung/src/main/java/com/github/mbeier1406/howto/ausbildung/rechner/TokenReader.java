package com.github.mbeier1406.howto.ausbildung.rechner;

import static java.util.Objects.requireNonNull;

/**
 * Definiert das Interface 
 */
public interface TokenReader {

	public static record Value(Object value, int length) {
		public Value(Object value, int length) {
			this.value = requireNonNull(value, "value");
			this.length = length;
			if ( length <= 0) throw new IllegalArgumentException("length <= 0: "+this.length);
		}
	}

	public char getSymbol();

	public Value read(String text);

}

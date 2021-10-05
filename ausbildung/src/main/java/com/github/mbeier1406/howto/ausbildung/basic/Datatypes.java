package com.github.mbeier1406.howto.ausbildung.basic;

/**
 * <p><u>Ausbildung Fachinformatiker Anwendungsentwicklung</u>: Demonstration primitiver Datentypen</p>
 * Grundlegendes zu Standard-Java-Datentypen. Für die weiteren Typen {@linkplain Byte} etc. zu ergänzen.
 * @author mbeier
 */
public class Datatypes {

	/**
	 * Liefert den maximalen Wert für den Datentyp {@linkplain Short}.
	 * @return {@value Short#MAX_VALUE}
	 */
	public static short getMaxShort() {
		return Short.MAX_VALUE;
	}

	/**
	 * Liefert den minimalen Wert für den Datentyp {@linkplain Short}.
	 * @return {@value Short#MIN_VALUE}
	 */
	public static short getMinShort() {
		return Short.MIN_VALUE;
	}

	/**
	 * Zur Demonstration von Problemen beim Cast; z. B. bei Zahlen größer als {@linkplain #getMaxShort()}
	 * @param i die Zahl als {@linkplain Integer}
	 * @return die Zahl als {@linkplain Short} konvertiert
	 */
	public static short intToShort(int i) {
		return (short) i;
	}

}

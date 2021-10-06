package com.github.mbeier1406.howto.ausbildung.basic;

/**
 * <p><u>Ausbildung Fachinformatiker Anwendungsentwicklung</u>: Demonstration primitiver Datentypen</p>
 * Grundlegendes zu Standard-Java-Datentypen. Für die weiteren Typen {@linkplain Byte} etc. zu ergänzen.
 * @author mbeier
 */
public interface Datatypes {

	/**
	 * Liefert den maximalen Wert für den Datentyp {@linkplain Short}.
	 * @return {@value Short#MAX_VALUE}
	 */
	public short getMaxShort();

	/**
	 * Liefert den minimalen Wert für den Datentyp {@linkplain Short}.
	 * @return {@value Short#MIN_VALUE}
	 */
	public short getMinShort();

	/**
	 * Zur Demonstration von Problemen beim Cast; z. B. bei Zahlen größer als {@linkplain #getMaxShort()}
	 * @param i die Zahl als {@linkplain Integer}
	 * @return die Zahl als {@linkplain Short} konvertiert
	 */
	public short intToShort(int i);

	/**
	 * Liefert den UTF-16-Character zum Unicode Codepoint. Beispiel:
	 * <a href="http://www.isthisthingon.org/unicode/index.php?page=1F&subpage=4&glyph=10e1">Codepoint 4321</a>
	 * @param ch den Unicode codepoint
	 * @return den UTF-16 Character
	 */
	public char intToChar(int ch);

}
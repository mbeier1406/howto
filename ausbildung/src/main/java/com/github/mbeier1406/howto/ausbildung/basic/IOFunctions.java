package com.github.mbeier1406.howto.ausbildung.basic;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p><u>Ausbildung Fachinformatiker Anwendungsentwicklung</u>: Demonstration Input/Output Funktionen</p>
 * Grundlegendes zu IO-Funktionen.
 * @author mbeier
 */
public interface IOFunctions {

	/**
	 * Eine Beispieldatenstruktur, die über IO-Funktionen gefüllt werden soll.
	 * @author mbeier
	 */
	public static class ExampleData {
		private String str;
		private int i;
		private float f;
		public ExampleData(String str, int i, float f) {
			super();
			this.str = str;
			this.i = i;
			this.f = f;
		}
		@Override
		public String toString() {
			return "ExampleData [str=" + str + ", i=" + i + ", f=" + f + "]";
		}
	}

	/**
	 * Die Datenstruktur {@linkplain ExampleData} aus einen {@linkplain InputStream} füllen.
	 * @param in der Inputstream
	 * @param charsetName die Codierung der einzulesenden Daten (Beispiel {@linkplain StandardCharsets#UTF_8})
	 * @return das aus dem Inputstream eingelesene Objekt
	 */
	public ExampleData readExampleData(InputStream in, String charsetName);

}

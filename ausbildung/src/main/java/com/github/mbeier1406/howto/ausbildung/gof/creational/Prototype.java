package com.github.mbeier1406.howto.ausbildung.gof.creational;

/**
 * GoF - Creational Pattern: Prototype<p/>
 * Erstellt Kopien von Objekten ohne Kenntnis der konkreten Klasse.
 * Geeignet für komplex/aufwändig zu konstruierende Objekte ohne Verwendung des {@code new}-Schlüsselworts.
 * Das Interface muss eine {@code clone()}-Methode zur Erzeugung der Kopien dfinieren.
 */
public interface Prototype {

	/** Zugriff auf die Verarbeitungslogik der Implementierungen. */
	public void method();

	/** Dient der Erzeugung von Kopien aus den Prototypen der verschiedenen Implemntierungen */
	public Prototype clone();

}

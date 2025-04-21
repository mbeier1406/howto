package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * GoF - Structural Pattern: Adapter<p/>
 * Dient der Demonstration des Adapter-Patterns. Wandelt eine im eigenen Code
 * verwendete Schnittstelle (die, z. B. weil sie bereits häufig verwendet wurde)
 * nicht angepasst werden soll, in eine andere um (z. B. die einer externen
 * Bibliothek, die für eine bestimmte Funktion verwendet werden soll).
 * Auf diese Weise können inkompatible Schnittstellen verwendet werden, ohne
 * eine von ihnen anpassen zu müssen.<p/>
 * Dies ist die Zielschnittstelle, die der Client erwartet.
 * @see /howto-ausbildung/src/main/resources/com/github/mbeier1406/howto/ausbildung/gof/structural/Adapter.png
 */
public interface AdapterInterface {

	/** Sortiert ein<b>Array</b> von <b>int</b> */
	public int[] sort(int[] liste);

}

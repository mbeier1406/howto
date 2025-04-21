package com.github.mbeier1406.howto.ausbildung.gof.structural;

import org.junit.jupiter.api.Test;

/**
 * Diesee Klasse steht als Beispiel für die Stellen,
 * an denen die Schnittstelle {@linkplain AdapterInterface#sort(int[])}
 * verwendet wird, aber als Implementierung die inkompatible Klasse aus
 * {@linkplain AdapterAdaptee#sort(java.util.List)} eingesetzt werdne soll.
 */
public class AdapterTest {

	/** Das im Code kompatible Objekt */
	public AdapterInterface adapter = new Adapter();

	/** Demonstriert nur die (In-)Kompatibilität von Impelentierung und Adapter */
	@Test
	public void testeAdapter() {
		int[] liste = new int[] { 4, 8, 1, 3 };  // Die zu sortierende Liste
		// new AdapterAdaptee().sort(liste); // nicht kompatibel
		adapter.sort(liste); // Adapter ist kompatibel und verwendet die gewünschte Implementierung
	}

}

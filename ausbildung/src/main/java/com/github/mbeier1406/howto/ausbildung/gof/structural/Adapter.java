package com.github.mbeier1406.howto.ausbildung.gof.structural;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Der Adapter verwendet die im Code eingesetzte Schnittstelle {@linkplain AdapterInterface}
 * und ist somit an allen verwendeten Stellen kompatibel.
 */
public class Adapter implements AdapterInterface {

	/** Diese Implementierung soll im Code eingesetzt werden */
	private AdapterAdaptee sorter;

	public Adapter() {
		sorter = new AdapterAdaptee();
	}

	/** Die Adapter-Methode enthält den Code zur Kompatibilität mit {@linkplain AdapterAdaptee#sort(java.util.List)} */
	@Override
	public int[] sort(int[] liste) {
		var listOfInteger = Arrays.stream(liste).boxed().collect(Collectors.toList());
		listOfInteger = sorter.sort(listOfInteger);
		return listOfInteger.stream().mapToInt(i -> i).toArray();
	}

}

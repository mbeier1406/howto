package com.github.mbeier1406.howto.ausbildung.gof.structural;

import java.util.Collections;
import java.util.List;

/**
 * Dies ist die Komponente, die zur Sortierung der Liste von Zahlen verwendet werden soll.
 * Die im Code verwendete Schnittstelle {@linkplain AdapterInterface} ist aber nicht kompatibel.
 */
public class AdapterAdaptee {

	/** Die Schnittstelle {@linkplain #sort(List)} ist nicht kompatibel zu {@linkplain AdapterInterface#sort(int[])} */
	public List<Integer> sort(final List<Integer> liste) {
		Collections.sort(liste);
		return liste;
	}

}

package com.github.mbeier1406.howto.jse.easyrandom;

import java.time.LocalDate;

/**
 * Diese Objekt soll mit zufälligen Daten gefüllt und getestet werden.
 * @author mbeier
 * @see Datenspeicher
 */
public class DatumsSpeicher {

	/** Test mit einem Datum */
	private LocalDate datum;

	public LocalDate getDatum() {
		return datum;
	}

	public DatumsSpeicher setDatum(LocalDate datum) {
		this.datum = datum;
		return this;
	}

	@Override
	public String toString() {
		return "DatumsSpeicher [datum=" + datum + "]";
	}

}

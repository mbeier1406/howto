package de.gmxhome.golkonda.howto.jee.event;

import javax.enterprise.event.Event;

/**
 * Einfache Klasse für einen zu versendenden {@linkplain Event} für einen CDI-Event-Test.
 * Enthält Informationen zu einem Scanner, die an verschiedene Adressaten gesendet wird.
 * @author mbeier
 */
public class ScannerEvent {

	/** Vom Scanner gelieferte Daten */
	private String daten;

	/** Nummer des Scanners */
	private int scannerNummer;

	public ScannerEvent() {}
	public ScannerEvent(String daten, int nummer) {
		super();
		this.daten = daten;
		this.scannerNummer = nummer;
	}

	public String getName() {
		return daten;
	}

	public int getAlter() {
		return scannerNummer;
	}

	@Override
	public String toString() {
		return "ScannerEvent [daten=" + daten + ", scannerNummer" + scannerNummer + "]";
	}

}

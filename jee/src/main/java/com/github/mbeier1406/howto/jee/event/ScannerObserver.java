package com.github.mbeier1406.howto.jee.event;

import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * Basisklasse für Scanner-Observer.
 * @author mbeier
 * @see ScannerStartObserver
 * @see ScannerStopObserver
 */
public class ScannerObserver {

	/** Registratur für Events */
	@Inject
	protected ScannerRegistry scannerRegistry;

	/** Der zuletzt empfangene {@linkplain Event} */
	protected ScannerEvent scannerEvent;

	/**
	 * Liefert den zueletzt empfangenen {@linkplain #scannerEvent}.
	 * @return den zueletzt empfangenen Event
	 */
	public ScannerEvent getScannerEvent() {
		return scannerEvent;
	}

}

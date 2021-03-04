package de.gmxhome.golkonda.howto.jee.event;

import javax.inject.Inject;

/**
 * Diese Klasse dient nur zur Demonstartion des Inject-Helper im zugwhörigen Unit-Test.
 * @author mbeier
 */
public class ScannerService {

	@Inject
	private ScannerRegistry scannerRegistry;

	/**
	 * Wird im Unit-Test ohne CDI Container aufgerufen, {@linkplain #scannerRegistry}
	 * darf dann nicht <b>null</b> sein!
	 * @return Anzahl der gezählten Events
	 * @see ScannerRegistry#getRegisterScannerEvent()
	 */
	public int getRegisterScannerEvent() {
		return this.scannerRegistry.getRegisterScannerEvent();
	}

}

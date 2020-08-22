package de.gmxhome.golkonda.howto.jee.event;

/**
 * Schnittstelle zur Registry für auftretende Events.
 * @author mbeier
 * @see ScannerStartObserver
 * @see ScannerStopObserver
 */
public interface ScannerRegistry {

	/** Registriert das Starten einen Scanners */
	public void registerScannerStart();

	/** Registriert das Anhalten einen Scanners */
	public void registerScannerStop();

	/** Registriert ein beliebiges Ereignis einen Scanners */
	public void registerScannerEvent();

	/**
	 * Liefert die Anzahl der registrierten Starts eines Scanners.
	 * @return die Anzahl der Starts
	 */
	public int getRegisterScannerStart();

	/**
	 * Liefert die Anzahl der registrierten Stopps eines Scanners.
	 * @return die Anzahl der Stopps
	 */
	public int getRegisterScannerStop();

	/**
	 * Liefert die Anzahl der registrierten Ereignisse eines Scanners.
	 * @return die Anzahl der Events
	 */
	public int getRegisterScannerEvent();

}

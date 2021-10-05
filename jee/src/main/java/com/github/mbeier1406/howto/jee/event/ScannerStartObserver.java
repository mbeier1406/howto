package com.github.mbeier1406.howto.jee.event;

import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.howto.jee.event.annotation.ScannerStart;

/**
 * Empfänger für einen Scanner-Start-Event ({@linkplain ScannerEvent} mit {@linkplain ScannerStart}).
 * Beispiel-Sender in {@linkplain ScannerEventSource}.
 * Die eingehenden Events werden in der {@linkplain ScannerRegistry} registriert.
 * Wird immer empfangen, da die Instanz des Objektes ggf. automatisch erzeugt wird.
 * @author mbeier
 * @see ScannerEvent
 */
public class ScannerStartObserver extends ScannerObserver {

	public static final Logger LOGGER = LogManager.getLogger(ScannerStartObserver.class);

	/**
	 * Verarbeitet einen Event, der den Start eines Scanners anzeigt.
	 * @param scannerEvent der Event mit den Details des betroffenen Scanners
	 */
	public void observerEvent(@Observes @ScannerStart ScannerEvent scannerEvent) {
		LOGGER.trace("scannerEvent={}", scannerEvent);
		this.scannerEvent = scannerEvent;
		scannerRegistry.registerScannerStart();
	}

}


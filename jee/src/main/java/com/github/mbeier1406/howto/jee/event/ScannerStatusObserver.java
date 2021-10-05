package com.github.mbeier1406.howto.jee.event;

import static javax.enterprise.event.Reception.IF_EXISTS;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Empfänger für alle Scanner-Events ({@linkplain ScannerEvent}). Beispiel-Sender in {@linkplain ScannerEventSource}.
 * Die eingehenden Events werden in der {@linkplain ScannerRegistry} registriert.
 * Wird nur empfangen, wenn die Instanz des Objektes bereits erzeugt wurde.
 * @author mbeier
 * @see ScannerEvent
 */
@ApplicationScoped
public class ScannerStatusObserver extends ScannerObserver {

	public static final Logger LOGGER = LogManager.getLogger(ScannerStatusObserver.class);

	/**
	 * Verarbeitet einen Event, der ein Ereignis eines Scanners anzeigt.
	 * @param scannerEvent der Event mit den Details des betroffenen Scanners
	 */
	public void observerEvent(@Observes(notifyObserver = IF_EXISTS) ScannerEvent scannerEvent) {
		LOGGER.trace("scannerEvent={}", scannerEvent);
		this.scannerEvent = scannerEvent;
		scannerRegistry.registerScannerEvent();
	}

}


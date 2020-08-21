package de.gmxhome.golkonda.howto.jee.event;

import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.gmxhome.golkonda.howto.jee.event.annotation.ScannerStart;

/**
 * Empfänger für einen Scanner-Start-Event. Beispiel-Sender in{@linkplain ScannerEventSource}.
 * Die eingehenden Events werden in der {@linkplain ScannerRegistry} registriert.
 * @author mbeier
 * @see ScannerEvent
 */
//@ApplicationScoped
public class ScannerStartObserver extends ScannerObserver {

	public static final Logger LOGGER = LogManager.getLogger(ScannerStartObserver.class);

	/**
	 * Verarbeitet einen Event, der den Start eines Scanners anzeigt.
	 * @param scannerEvent der Event mit den Details des betroffenen Scanners
	 */
	public void observerEvent(@Observes/*(notifyObserver = IF_EXISTS)*/ @ScannerStart ScannerEvent scannerEvent) {
		LOGGER.trace("scannerEvent={}", scannerEvent);
		this.scannerEvent = scannerEvent;
		scannerRegistry.registerScannerStart();
	}

}


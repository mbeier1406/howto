package de.gmxhome.golkonda.howto.jee.event;

import static javax.enterprise.event.Reception.ALWAYS;

import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.gmxhome.golkonda.howto.jee.event.annotation.ScannerStop;

/**
 * Empfänger für einen Scanner-Stop-Event ({@linkplain ScannerEvent} mit {@linkplain ScannerStop}).
 * Beispiel-Sender in {@linkplain ScannerEventSource}.
 * Die eingehenden Events werden in der {@linkplain ScannerRegistry} registriert.
 * Wird immer empfangen, da die Instanz des Objektes ggf. automatisch erzeugt wird ({@code notifyObserver = ALWAYS}).
 * @author mbeier
 * @see ScannerEvent
 */
public class ScannerStopObserver extends ScannerObserver {

	public static final Logger LOGGER = LogManager.getLogger(ScannerStopObserver.class);

	/**
	 * Verarbeitet einen Event, der den Stopp eines Scanners anzeigt.
	 * @param scannerEvent der Event mit den Details des betroffenen Scanners
	 */
	public void observerEvent(@Observes(notifyObserver = ALWAYS) @ScannerStop ScannerEvent scannerEvent) {
		LOGGER.trace("scannerEvent={}", scannerEvent);
		this.scannerEvent = scannerEvent;
		scannerRegistry.registerScannerStop();
	}

}


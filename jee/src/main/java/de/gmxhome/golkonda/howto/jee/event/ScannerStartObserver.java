package de.gmxhome.golkonda.howto.jee.event;

import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.gmxhome.golkonda.howto.jee.event.annotation.ScannerStart;

/**
 * Empfänger für einen Scanner-Start-Event. Beispiel-Sender in{@linkplain ScannerEventSource}.
 * @author mbeier
 * @see ScannerEvent
 */
public class ScannerStartObserver {

	public static final Logger LOGGER = LogManager.getLogger(ScannerStartObserver.class);

	/**
	 * Verarbeitet einen Event, der den Start eines Scanners anzeigt.
	 * @param scannerEvent der Event mit den Details des betroffenen Scanners
	 */
	public void observerEvent(@Observes @ScannerStart ScannerEvent scannerEvent) {
		LOGGER.trace("scannerEvent={}", scannerEvent);
	}
}


package de.gmxhome.golkonda.howto.jee.event;

import javax.enterprise.event.Observes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.gmxhome.golkonda.howto.jee.event.annotation.ScannerStop;

/**
 * Empfänger für einen Scanner-Stop-Event. Beispiel-Sender in{@linkplain ScannerEventSource}.
 * @author mbeier
 * @see ScannerEvent
 */
public class ScannerStopObserver {

	public static final Logger LOGGER = LogManager.getLogger(ScannerStopObserver.class);

	/**
	 * Verarbeitet einen Event, der den Stopp eines Scanners anzeigt.
	 * @param scannerEvent der Event mit den Details des betroffenen Scanners
	 */
	public void observerEvent(@Observes @ScannerStop ScannerEvent scannerEvent) {
		LOGGER.trace("scannerEvent={}", scannerEvent);
	}
}


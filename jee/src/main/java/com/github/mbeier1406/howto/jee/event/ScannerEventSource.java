package com.github.mbeier1406.howto.jee.event;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.howto.jee.event.annotation.ScannerStart;
import com.github.mbeier1406.howto.jee.event.annotation.ScannerStop;

/**
 * Versendet verschiedene {@linkplain ScannerEvent}s. Definiert sind:
 * <ul>
 * <li>{@linkplain #sendEventStart(int, String)}: Scanner wird gestartet. Beispiel-Empfänger in {@linkplain ScannerStartObserver}.</li>
 * <li>{@linkplain #sendEventStop(int, String)}: Scanner wird angehalten. Beispiel-Empfänger in {@linkplain ScannerStopObserver}.</li>
 * </ul>
 * @author mbeier
 * @see ScannerEvent
 */
public class ScannerEventSource {

	public static final Logger LOGGER = LogManager.getLogger(ScannerEventSource.class);

	/** Versendet Scanner-Start-Events */
	@Inject
	@ScannerStart
	private Event<ScannerEvent> scannerStartEvent;

	/** Versendet Scanner-Stop-Events */
	@Inject
	@ScannerStop
	private Event<ScannerEvent> scannerStopEvent;

	/**
	 * Versendet einen Event.
	 * @param event der zu versendende Event
	 * @param scannerEvent
	 * @see #sendEventStart(int, String)
	 * @see #sendEventStop(int, String)
	 */
	public void sendEvent(Event<ScannerEvent> event, ScannerEvent scannerEvent) {
		try ( final CloseableThreadContext.Instance ctc = put("scannerEvent", scannerEvent.toString()).put("event", event.toString()) ) {
			event.fire(scannerEvent);
		}
		catch ( Exception e ) {
			LOGGER.error("scannerEvent={}", event);
			throw e;
		}
	}

	/**
	 * Versendet einen Start-Event.
	 * @param scannerNummer die Nummer des Scanners, der den Event ausgelöst hat
	 * @param daten Informationen zum Start
	 * @see #sendEvent(Event, ScannerEvent)
	 */
	public void sendEventStart(int scannerNummer, String daten) {
		sendEvent(scannerStartEvent, new ScannerEvent(daten, scannerNummer));
	}

	/**
	 * Versendet einen Stop-Event.
	 * @param scannerNummer die Nummer des Scanners, der den Event ausgelöst hat
	 * @param daten Informationen zum Stopp
	 * @see #sendEvent(Event, ScannerEvent)
	 */
	public void sendEventStop(int scannerNummer, String daten) {
		sendEvent(scannerStopEvent, new ScannerEvent(daten, scannerNummer));
	}

}

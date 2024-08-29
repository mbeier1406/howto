package com.github.mbeier1406.howto.ausbildung.mt;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demonstriert das Beenden von Threads über {@linkplain Thread#interrupt()} und über Deamonthreads.
 * Damit das funktioniert, muss der Thread eine der folgenden Eigenschaften besitzen:
 * <ul>
 * <li>Er fragt den Zustand per {@linkplain Thread#isInterrupted()} ab</li>
 * <li>Er verwendet eine Methode, die die {@linkplain InterruptedException} wirft</li>
 * </ul>
 * @author mbeier
 */
public class ThreadTermination {

	public static final Logger LOGGER = LogManager.getLogger(ThreadTermination.class);

	/** Startet die Threads, die sich per Untebrechung beenden lassne oder auch nicht */
	public static void main(String[] args) {
		Stream.of(
				/* Dieser Therad läßt sich per interrupt() nicht unterbrechen, da er nicht darauf reagiert */
				new BlockierenderThread(),
				/* Dieser Therad läßt sich per interrupt() unterbrechen, da er sleep() verwendet */
				new NichtBlockierenderPerMethodeThread(),
				/* Dieser Therad läßt sich per interrupt() unterbrechen, da er auf Unterbrechung testet */
				new NichtBlockierenderPerTestThread(),
				/* Dieser Therad läßt sich per interrupt() unterbrechen, da es sich um einen Daemonthread handelt */
				new NichtBlockierenderEndlosThread())
			.forEach(t -> {
				t.start();
				t.interrupt();
			});
		LOGGER.info("Alles fertig!");
	}

	/** Threads dieser Klasse lassen sich nicht unterbrechen */
	public static class BlockierenderThread extends Thread {
		@Override
		public void run() {
			setName(getClass().getSimpleName());
			for ( var i=0; i < 10000; i++ )
				for ( var j=0; j < 10000; j++ )
					new BigDecimal(String.valueOf(i)).multiply(new BigDecimal(String.valueOf(j)));
			LOGGER.info("{} fertig!", this);
		}
	}

	/** Threads dieser Klasse lassen sich unterbrechen, da {@linkplain Thread#sleep(long)} eine {@linkplain InterruptedException} wirft */
	public static class NichtBlockierenderPerMethodeThread extends Thread {
		@Override
		public void run() {
			setName(getClass().getSimpleName());
			for ( var i=0; i < 10000; i++ )
				for ( var j=0; j < 10000; j++ ) {
					new BigDecimal(String.valueOf(i)).multiply(new BigDecimal(String.valueOf(j)));
					if ( i == 1000 ) //  Nach einiger Zeit auf das Interrupt reagieren, der Main-Thread ist hier vermutlich bereits beendent
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							throw new RuntimeException();
						}
				}
			LOGGER.info("{} fertig!", this);
		}
	}

	/** Threads dieser Klasse lassen sich unterbrechen, da sie den Zustand serber prüfen */
	public static class NichtBlockierenderPerTestThread extends Thread {
		@Override
		public void run() {
			setName(getClass().getSimpleName());
			for ( var i=0; i < 10000; i++ )
				for ( var j=0; j < 10000; j++ ) {
					new BigDecimal(String.valueOf(i)).multiply(new BigDecimal(String.valueOf(j)));
					if ( i == 2000 && this.isInterrupted() ) //  Nach einiger Zeit auf das Interrupt reagieren, der Main-Thread ist hier vermutlich bereits beendent
						throw new RuntimeException();
				}
			LOGGER.info("{} fertig!", this);
		}
	}

	/**
	 * Threads dieser Klasse lassen sich unterbrechen, da sie Daemons sind. Verwende Daemon-Threads,
	 * <ul>
	 * <li>wenn eine externe Bibliothek aufgerufen wird, die evtl. keine Interrupts verwendet/testet</li>
	 * <li>bei Hintergrundprozessen, die jederzeit beendet werdne dürfen (Autosafe bei Texteditoren usw.)</li>
	 * </ul>
	 */
	public static class NichtBlockierenderEndlosThread extends Thread {
		public NichtBlockierenderEndlosThread() {
			setDaemon(true);	// Ohne diese Anweisung würde die VM sich nie beenden!		
		}
		@Override
		public void run() {
			while ( true ) {
				try { Thread.sleep(1000); } catch (InterruptedException e) { }
			}
		}
	}

}

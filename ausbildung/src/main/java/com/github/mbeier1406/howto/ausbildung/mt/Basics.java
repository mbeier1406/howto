package com.github.mbeier1406.howto.ausbildung.mt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Zeit, die beiden verschiedenen Möglickeiten, einen Thread zu Erzeugen und zu starten:
 * <ol>
 * <li>1: über eine anonyme Klasse, die das Interface {@linkplain Runnable} implementiert.</li>
 * <li>2: über eine Klasse, die {@linkplain Thread} erweitert und auf deren lokale Methoden Zugriff hat.</li>
 * </ol>
 * @author mbeier
 */
public class Basics {

	public static final Logger LOGGER = LogManager.getLogger(Basics.class);

	/**
	 * Startet die Threads auf zwei unterschiedliche Weisen. Ausführungsreihenfolge nicht deterministisch.
	 * Zeigt außerdem die Verwendung von Priorität und Laufzeitfehlerbehandlung.
	 */
	public Basics() {

		// Methode 1: anonyme Klassen. Benötigt statische Thread-Zugriffe für Methoden; setzt den Threadnamen bei der Erzeugung
		Thread t1 = new Thread(() -> {
			Thread.currentThread().setName("Thread mit anonymer Klasse: Methode 3"); // alternativ kann der Threadname so gesetzt werden, überschreibt die anderen
			LOGGER.info("t1: {}; Prio={}", Thread.currentThread().getName(), Thread.currentThread().getPriority()); // Daten über statische Thread-Methoden ermitteln
		}, "Thread mit anonymer Klasse: Methode 1");
		t1.setName("Thread mit anonymer Klasse: Methode 2"); // Überschreibt den Namen Methode 1
		t1.setPriority(3); // von 1..10 mit 10=höchste
		t1.start();

		// Methode 2: Thread-Klassen. Ermöglicht lokale Thread-Methodenzugriffe
		Thread t2 = new Thread2();
		t2.setName("Thread mit eigener Klasse");
		t2.setPriority(Thread.MAX_PRIORITY);
		t2.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {			
			/** {@inheritDoc} */
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				// Hier allokierte Ressourcen freigeben usw.
				LOGGER.error("Thread: {}; Throwable: {}", t, e);
			}
		});
		t2.start();

	}

	/** Eigene Thread-Klasse mit Verwendung lokaler Thread-Methoden */
	public static class Thread2 extends Thread {

		/** {@inheritDoc} */
		@Override
		public void run() {
			LOGGER.info("t2: {}; Prio={}", this.getName(), this.getPriority()); // Daten über lokale Thread-Methoden ermitteln
			throw new RuntimeException("Wichtiger Fehler!");
		}

	}

}

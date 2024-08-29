package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demonstriert die Koordination von Threads mittels {@linkplain Thread#join(long)}.
 * Eine Liste von Threads wird gestartet, die zwischen null und vier Sekunden warten und
 * dann einen zufälligen Wert als Ergebnis ermitteln. Sie haben eine beschränkte Zeit,
 * da sie sonst vom Main-Thread, der sie gestartet hat, unterbrochen werden (die
 * Reihenfolge bedingt, dass die letzten Threads in der Liste am meisten Zeit haben).
 * @author mbeier
 */
public class ThreadCoordination {

	public static final Logger LOGGER = LogManager.getLogger(ThreadCoordination.class);

	/** Maximale Zeit, die ein Thread warten soll, ist {@value} */
	public static final short MAX_WARTEZEIT = 5;

	/**
	 * Der Thread wartet eine zufällige Anzahl Sekunden (max. {@value ThreadCoordination#MAX_WARTEZEIT}
	 * und "berechnet" dann ein Ergebnis. Ist es <i>-1</i> (Initialwert), wird der Thread als nicht
	 * abgeschlossen (unterbrochen) gewertet (hat zu viel Zeit benötigt).
	 */
	public static class WarteThread extends Thread {
		private final short zeit;   /* Wie viele Sekunden der Thread warten soll */
		private final short nr;     /* Nummer des Theads für das Logging */
		private int ergebnis = -1;  /* Ergebnis der "Berechnung" des Threads */
		public WarteThread(short nr) {
			this.nr = nr;
			this.zeit = (short) new Random().nextInt(nr);
		}
		@Override
		public void run() {
			try {
				Thread.sleep(this.zeit*1000);
				this.ergebnis = new Random().nextInt();
			} catch (InterruptedException e) {
				LOGGER.error("Thread {} mit Wartezeit {} wurde unterbrochen!", this.nr, this.zeit);
			}
		}
		public int getErgebnis() {
			return this.ergebnis;
		}
		public boolean isFinished() {
			return this.ergebnis != -1; // Ergebnis noch -1: Berechnung nicht fertig
		}
		@Override
		public String toString() {
			return "WarteThread [zeit=" + zeit + ", nr=" + nr + ", ergebnis=" + ergebnis + "]";
		}
	}

	/**
	 * Startet eine Liste von Warte-Threads, wartet nacheinander max. eine Sekunde
	 * auf ihr Ergebnis, unterbricht sie andernfalls und gibt das Ergebnis aus.
	 */
	public static void main(String[] args) {
		final List<WarteThread> listeWarteThread = new ArrayList<>();
		for ( var i=0; i < 5; i++ ) listeWarteThread.add(new WarteThread((short)(i+1)));
		listeWarteThread.stream().forEach(t -> t.start());
		listeWarteThread.stream().forEach(t -> {
			try {
				t.join(1000); // max. eine Sekunde warten...
				if ( !t.isFinished() ) t.interrupt(); // wenn nicht fertig, abbrechen
			} catch (InterruptedException e) { }
		});
		listeWarteThread
			.stream()
			.peek(LOGGER::trace)
			.filter(t -> !t.isFinished())
			.forEach(t -> LOGGER.info("Thread ist nicht fertig: {}", t));
	}

}

package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.Random;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Zeigt das parallele Arbeiten von Threads anhand eines kleinen Ratespiels.
 * Die Klasse {@linkplain ZuratendeZahl} speichert die zufällig ermittelte, zu ratende, Zahl.
 * Die Klasse {@linkplain ZufaelligerRatethread} versucht, die zu ratende Zahl zufällig zu ermitteln.
 * Die Klasse {@linkplain SystematischerRatethread} errät die Zahl durch Ausprobieren aller Möglichkeiten.
 * Das Spiel gewinnen entweder zwei zufällige, oder der systematische Thread.
 * @author mbeier
 */
public class Ratespiel {

	public static final Logger LOGGER = LogManager.getLogger(Ratespiel.class);

	/** Die zu ratenden Zahl zwischen 0 und {@value #MAX_ZAHL}-1 */
	public static final int MAX_ZAHL = 1000;

	/**
	 * Speichert die zu ratende Zahl und bietet eine Methode zum Überprüfen an.
	 * Diese verzögert die Ausführung um einige Millisekunden, damit nicht
	 * immer die ratenden Threads gewinnen.
	 */
	public static class ZuratendeZahl {
		private int zahl;
		public ZuratendeZahl(int zahl) {
			this.zahl = zahl;
		}
		public boolean pruefe(int gerateneZahl) {
			try { Thread.sleep(10); } catch (InterruptedException e) { }
			return zahl == gerateneZahl;
		}
		@Override
		public String toString() {
			return "ZuratendeZahl [zahl=" + zahl + "]";
		}
	}

	/** Grundsätzliche Einstellungen für alle ratethread-Implementierungen */
	public static abstract class RateTheadBasics extends Thread {
		protected ZuratendeZahl zuratendeZahl;
		protected int durchlauf = 0;
		protected int gerateneZahl;
		public RateTheadBasics(final ZuratendeZahl zuratendeZahl) {
			this.zuratendeZahl = zuratendeZahl;
			setPriority(MAX_PRIORITY);
			setName(getName()+" ("+this.getClass().getSimpleName()+")");
		}
		public void logAndCheck() {
			if ( durchlauf++%100 == 0 )
				LOGGER.trace("{}: gerateneZahl={}", this, gerateneZahl);
			if ( zuratendeZahl.pruefe(gerateneZahl) ) {
				LOGGER.info("{}: Treffer: {}", this, gerateneZahl);
				System.exit(0);
			}
		}
	}

	/** Diese Implementierung versucht die zu ratende Zahl zufällig zu ermitteln */
	public static class ZufaelligerRatethread extends RateTheadBasics {
		public ZufaelligerRatethread(ZuratendeZahl zuratendeZahl) {
			super(zuratendeZahl);
		}
		@Override
		public void run() {
			while ( true ) {
				gerateneZahl = new Random().nextInt(MAX_ZAHL);
				logAndCheck();
			}
		}
	}

	/** Diese Implementierung errät die Zahl durch herunterzählen */
	public static class SystematischerRatethread extends RateTheadBasics {
		public SystematischerRatethread(ZuratendeZahl zuratendeZahl) {
			super(zuratendeZahl);
		}
		@Override
		public void run() {
			for( gerateneZahl = MAX_ZAHL-1; gerateneZahl >= 0; gerateneZahl-- ) {
				logAndCheck();
			}
		}
	}


	/** Führt das Ratespiel durch */
	public static void main(String[] args) {
		var zuratendeZahl = new ZuratendeZahl(new Random().nextInt(MAX_ZAHL));
		LOGGER.info("zuratendeZahl={}", zuratendeZahl);
		// Startet zwei Threads die zufällig raten und einen, der alle Möglichkeiten probiert
		Stream.of(new ZufaelligerRatethread(zuratendeZahl), new ZufaelligerRatethread(zuratendeZahl), new SystematischerRatethread(zuratendeZahl)).forEach(t -> {
			LOGGER.info("Starte {}...", t.getName());
			t.start();
		});
	}

}

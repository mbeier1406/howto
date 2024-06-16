package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.Random;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dieses Beispiel zeigt das Aufzeichnen von Performancedaten.
 * Die Klasse {@linkplain Metrik} speichert die Latenz/Dauer der Ausführung von {@linkplain Geschaeftslogik#run()}.
 * Es wird erwartet, das ess ungefaehr die Haelfte von {@value #WARTE_ZEIT} ist.
 * @author mbeier
 */
public class MetricsMeasurement {

	public static final Logger LOGGER = LogManager.getLogger(MetricsMeasurement.class);

	/** Die Geschaeftslogik soll max. {@value} ms dauern. */
	public static final int WARTE_ZEIT = 10;


	/**
	 * Diese Klasse speichert den aktuellen Durchschnitt der Laufzeiten (Dauer der Ausfuehrung / Latenz)
	 * von {@linkplain Geschaeftslogik#run()}. Der Aufruf von {@linkplain Metrik#neuerMesspunkt(long)}
	 * ist ein <i>kritischer Bereich</i> und muss gegen parallele Ausführung geschuetzt (<i>synchronisiiert</i>) werden.
	 * Da Zuweisung und Auslesen von <u>long</u> und <u>double</u> auch als <i>primitive Datentypen</i> <b>nicht
	 * atomar</b> (also nicht thread-safe) ist, muss das atomare Auslesen der Latenz in {@linkplain Metrik#getDurchschnitt()}
	 * durch das Schluesselwort <b>volatile</b> fuer {@linkplain Metrik#durchschnitt} erzwungen werden.
	 */
	public static class Metrik {
		private long anzahlMessungen = 0L;
		private volatile double durchschnitt = 0.0; // volatile, damit das Auslesen in in getDurchschnitt() atomar erfolgt
		public synchronized void neuerMesspunkt(long wert) { // synchronisiert um den kritischen Bereich zu sichern
			double aktuelleSumme = this.anzahlMessungen * this.durchschnitt;
			this.anzahlMessungen++;
			this.durchschnitt = (aktuelleSumme+wert) / this.anzahlMessungen; // atomar da volatile 
		}
		public double getDurchschnitt() {
			return this.durchschnitt;
		}
	}

	/** Diese Klasse simuliert die "Geschaeftslogik", deren Latenz gemessen werden soll */
	public static class Geschaeftslogik extends Thread {
		private final Metrik metrik;
		private final Random random = new Random();
		public Geschaeftslogik(Metrik metrik) {
			this.metrik = metrik;
		}
		@Override
		public void run() {
			while ( true ) {
				long start = System.currentTimeMillis();
				try { Thread.sleep(random.nextLong(WARTE_ZEIT)); } catch (InterruptedException e) { }
				this.metrik.neuerMesspunkt(System.currentTimeMillis()-start);
			}
		}
	}

	/** In regelmaessigen Abstaenden die durchschnitteliche Ausfuehrungszeit von {@linkplain Geschaeftslogik#run()} ausgeben */
	public static class MetrikAusgeben extends Thread {
		private final Metrik metrik;
		public MetrikAusgeben(Metrik metrik) {
			this.metrik = metrik;
		}
		@Override
		public void run() {
			while ( true ) {
				/* Der Aufruf getDurchschnitt() ist NICHT synchronisiert und bremst daher NICHT die Geschaeftslogik! */
				LOGGER.info("Durchschnittliche Laufzeit: {}", this.metrik.getDurchschnitt());
				try { Thread.sleep(1000); } catch (InterruptedException e) { }
			}
		}
	}

	/** Messung starten */
	public static void main(String[] args) {
		final var metrik = new Metrik();
		IntStream.range(0, 10).forEach(i -> new Geschaeftslogik(metrik).start());
		new MetrikAusgeben(metrik).start();
	}

}

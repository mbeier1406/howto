package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demonstriert das Eintreten eines Deadlocks. In {@linkplain Kreuzung} werden zwei
 * sich kreuzende Strassen simuliert, die jeweils durch eine Ampel abgesichert sind.
 * Das Passieren der Kreutzung wird durch eine zufällige Wartezeit simuliert.
 * Die Fahrzeuge versuchen jeweils die Ampel auf ihrer Strasse zu blockieren, und
 * danach die andere. Das Deadlock entsteht jeweils dann, wenn ein Fahrzeug die
 * eigene Ampel blockiert hat, unterbrochen wird und danach die versucht, die andere
 * zu aqurieren (die vom anderen Prozess bereits geblockt ist.
 * @author mbeier
 */
public class Deadlock {

	public static final Logger LOGGER = LogManager.getLogger();

	/** Simuliert die Kreuzung mit zwei Wegen und zwei Ampeln. */
	public static class Kreuzung {

		/** Die Ampel für Weg A */
		private Object ampelWegA = new Object();
		/** Die Ampel für Weg B */
		private Object ampelWegB = new Object();
		/** Erzeugt die zufällige Durchfahrtszeit für ein Fahrzeug */
		private Random random = new Random();

		/**
		 * Simuliert die Durchfahrt eines Fahrzeugs über die Kreuzung.
		 * @param ampel1 wenn {@linkplain #ampelWegA}, dann Fahrzeug auf Weg A sonst Weg B
		 * @param ampel2 wenn {@linkplain #ampelWegB}, dann Fahrzeug auf Weg A sonst Weg B
		 */
		public void wegBefahren(Object ampel1, Object ampel2) {
			final var fahrzeug = ampel1==this.ampelWegA?"A":"B";
			LOGGER.info("Fahrzeug {} wartet auf Ampel 1", fahrzeug);
			synchronized(ampel1) { // Eigene Ampel blockieren
				LOGGER.info("Fahrzeug {} wartet auf Ampel 2", fahrzeug);
				synchronized(ampel2) { // Ampel auf dem anderen Weg blockieren
					try {
						LOGGER.info("Fahrzeug passiert Weg {}...", fahrzeug);
						Thread.sleep(random.nextInt(10));
					} catch (InterruptedException e) { }
				}
			}
		}

		/** Fahrzeug versucht auf Weg A die Kreuzung zu passieren */
		public void wegABefahren(Void v) {
			this.wegBefahren(this.ampelWegA, this.ampelWegB);
		}

		/** Fahrzeug versucht auf Weg B die Kreuzung zu passieren */
		public void wegBBefahren(Void v) {
			this.wegBefahren(this.ampelWegB, this.ampelWegA);
		}

	}

	/**
	 * Ein Fahrzeug, das in einer Endlosschleife die Kreuzung entweder
	 * über Weg A ({@linkplain Kreuzung#wegABefahren(Void)}) oder
	 * Weg B ({@linkplain Kreuzung#wegBBefahren(Void)}) passieren will.
	 */
	public static class Fahrzeug implements Runnable {
		private final Kreuzung kreuzung;
		private final Consumer<Void> f;
		public Fahrzeug(final Kreuzung kreuzung, final Consumer<Void> f) {
			this.kreuzung = kreuzung;
			this.f = f;
		}
		@Override
		public void run() {
			while ( true ) {
				f.accept(null);
			}
		}
	}

	/**
	 * Erzeugt die {@linkplain Kreuzung} und startet die beiden {@linkplain Fahrzeug}e
	 * auf Weg A und Weg B. Terminiert nicht da die Threads (Fahrzeuge) in einer
	 * Endlosschleife laufen.
	 */
	public static void main(String[] args) throws InterruptedException {
		final var kreuzung = new Kreuzung();
		final Consumer<Void> a = kreuzung::wegABefahren, b = kreuzung::wegBBefahren;
		List<Thread> threads = Stream
			.of(a, b)
			.map(f -> new Thread(new Fahrzeug(kreuzung, f)))
			.collect(Collectors.toList());
		threads.forEach(Thread::start);
		threads.forEach(t -> {
			try { t.join(); } catch (InterruptedException e) { }
		});
	}

}

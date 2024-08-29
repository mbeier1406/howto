package com.github.mbeier1406.howto.ausbildung.mt;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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

	private static final Logger LOGGER = LogManager.getLogger();


	/**
	 * Alle Implementierungen einer Kreuzung müssen zwei
	 * Fahrzeugen die Möglichkeit geben, sie über Weg A
	 * oder Weg B zu passieren.
	 */
	public static interface Kreuzung {
		public void wegABefahren(Void v);
		public void wegBBefahren(Void v);
	}


	/**
	 * Simuliert die Kreuzung mit zwei Wegen und zwei Ampeln.
	 * Die Reihenfolge der Blockierung der Ampeln ist für beide
	 * Wege unterschiedlich (circular wait), was zu einer Blockade
	 * führen kann, da die Standardmethode <i>synchronized</i>
	 * verwendet wird (nicht unterbrechbar).
	 */
	public static class KreuzungBlockierend implements Kreuzung {

		/** Die Ampel für Weg A */
		protected Object ampelWegA = new Object();
		/** Die Ampel für Weg B */
		protected Object ampelWegB = new Object();
		/** Erzeugt die zufällige Durchfahrtszeit für ein Fahrzeug */
		protected Random random = new Random();

		/**
		 * Simuliert die Durchfahrt eines Fahrzeugs über die Kreuzung.
		 * @param ampel1 wenn {@linkplain #ampelWegA}, dann Fahrzeug auf Weg A sonst Weg B
		 * @param ampel2 wenn {@linkplain #ampelWegB}, dann Fahrzeug auf Weg A sonst Weg B
		 */
		protected void wegBefahren(Object ampel1, Object ampel2) {
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
		@Override
		public void wegABefahren(Void v) {
			this.wegBefahren(this.ampelWegA, this.ampelWegB);
		}

		/** Fahrzeug versucht auf Weg B die Kreuzung zu passieren */
		@Override
		public void wegBBefahren(Void v) {
			this.wegBefahren(this.ampelWegB, this.ampelWegA);
		}

	}


	/**
	 * Diese Implementierung von {@linkplain Kreuzung} ist nicht blockierend,
	 * da die Ampeln von beiden Fahrzeugen in der gleichen Reihenfolge
	 * blockiert werden (vermeidet <i>circular wait</i>).
	 */
	public static class KreuzungNichtBlockierend extends KreuzungBlockierend {
		
		/** Fahrzeug versucht auf Weg A die Kreuzung zu passieren */
		@Override
		public void wegABefahren(Void v) {
			this.wegBefahren(this.ampelWegA, this.ampelWegB);
		}

		/** Fahrzeug versucht auf Weg B die Kreuzung zu passieren */
		@Override
		public void wegBBefahren(Void v) {
			this.wegBefahren(this.ampelWegA, this.ampelWegB);
		}

	}


	/**
	 * Eine nicht-performante/nicht-sichere Lösung für das Deadlock-Problem: soll die grundsätzliche Verwendung
	 * von {@linkplain Lock}s zeigen: über {@linkplain ReentrantLock#isLocked()} prüfen, ob das Lock frei ist
	 * und dann belegen. Kann trotzdem zum Deadlock führen, wenn nach der Abfrage ein anderer Thread das Lock erhält.<p/>
	 * <em>Hinweis</em>: In {@linkplain KreuzungLockNichtBlockierendNichtSicher#wegABefahren(Void)} und
	 * {@linkplain KreuzungLockNichtBlockierendNichtSicher#wegBBefahren(Void)} ist die selbe Logik wie in
	 * {@linkplain KreuzungBlockierend#wegABefahren(Void)} etc., aber aufgrund des {@linkplain ReentrantLock}s
	 * kann ein Deadlock hier vermieden werden.
	 */
	public static class KreuzungLockNichtBlockierendNichtSicher extends KreuzungBlockierend implements Kreuzung {

		protected ReentrantLock ampelWegA = new ReentrantLock(), ampelWegB = new ReentrantLock();

		protected void wegBefahren(ReentrantLock ampel1, ReentrantLock ampel2) {
			final var fahrzeug = ampel1==this.ampelWegA?"A":"B";
			try {
				LOGGER.info("Fahrzeug {} wartet auf Ampel 1; wartend: {}", fahrzeug, ampel1.getQueueLength());
				if ( ampel1.isLocked() ) return; else ampel1.lock(); // Eigene Ampel blockieren
				try {
					LOGGER.info("Fahrzeug {} wartet auf Ampel 2; wartend: {}", fahrzeug, ampel2.getQueueLength());
					if ( ampel2.isLocked() ) return; else ampel2.lock(); // Ampel auf dem anderen Weg blockieren
					LOGGER.info("Fahrzeug passiert Weg {} (ampel1={}; ampel2={})...", fahrzeug, ampel1.isHeldByCurrentThread(), ampel2.isHeldByCurrentThread());
					Thread.sleep(random.nextInt(10));
				} catch (InterruptedException e) { }
				finally {
					try { ampel2.unlock(); } catch ( IllegalMonitorStateException e ) {}
				}
			}
			finally {
				try { ampel1.unlock(); } catch ( IllegalMonitorStateException e ) {}
			}
		}

		/** Fahrzeug versucht auf Weg A die Kreuzung zu passieren */
		@Override
		public void wegABefahren(Void v) {
			this.wegBefahren(this.ampelWegA, this.ampelWegB);
		}

		/** Fahrzeug versucht auf Weg B die Kreuzung zu passieren */
		@Override
		public void wegBBefahren(Void v) {
			this.wegBefahren(this.ampelWegB, this.ampelWegA);
		}

	}


	/**
	 * Diese Lösung ist nicht performant, funktioniert aber grundsätzlich: die Versuche, ein Lock
	 * zu erhalten sind unterbrechungsfähig, der {@linkplain WatchDog} kann blockierte Fahrzeuge
	 * freigeben (per {@linkplain Thread#interrupt()}, die dann ggf. bereits eine blockierte Ampel
	 * wieder freigeben, so dass die Kreuzng wieder frei ist.
	 */
	public static class KreuzungLockUnterbrechbar extends KreuzungLockNichtBlockierendNichtSicher {

		protected void wegBefahren(ReentrantLock ampel1, ReentrantLock ampel2) {
			final var fahrzeug = ampel1==this.ampelWegA?"A":"B";
			try {
				LOGGER.info("Fahrzeug {} wartet auf Ampel 1...", fahrzeug);
				ampel1.lockInterruptibly(); // Eigene Ampel blockieren, kann unterbrochen werden
				try {
					LOGGER.info("Fahrzeug {} wartet auf Ampel 2...", fahrzeug);
					ampel2.lockInterruptibly(); // Ampel auf dem anderen Weg blockieren, kann unterbrochen werden
					LOGGER.info("Fahrzeug passiert Weg {}!", fahrzeug);
					Thread.sleep(random.nextInt(10));
				}
				catch (InterruptedException e) {
					LOGGER.info("Fahrzeug {} bekommt Ampel 2 nicht, Ampel 1 freigeben!", fahrzeug);
				}
				finally {
					if ( ampel2.isHeldByCurrentThread() ) ampel2.unlock();
				}
			}
			catch (InterruptedException e1) {
				LOGGER.info("Fahrzeug {} bekommt Ampel 1 nicht, neuer Versuch!", fahrzeug);
			}
			finally {
				if ( ampel1.isHeldByCurrentThread() ) ampel1.unlock();
			}
		}
	}


	/**
	 * Die korrekte Lösung: mittels {@linkplain ReentrantLock#tryLock()} versuchen,
	 * das Lock zu erhalten, und ansonsten den Versuch, die Kreuzung zu überqueren
	 * abbrechen und ggf. bereits die blockierte eigene Ampel (Ampel 1) wieder
	 * freigeben.
	 */
	public static class KreuzungLock extends KreuzungLockNichtBlockierendNichtSicher {

		protected void wegBefahren(ReentrantLock ampel1, ReentrantLock ampel2) {
			final var fahrzeug = ampel1==this.ampelWegA?"A":"B";
			try {
				LOGGER.info("Fahrzeug {} wartet auf Ampel 1...", fahrzeug);
				if ( !ampel1.tryLock(10L, TimeUnit.MILLISECONDS) ) return; // Eigene Ampel blockieren, max 10 ms warten
				try {
					LOGGER.info("Fahrzeug {} wartet auf Ampel 2...", fahrzeug);
					if ( !ampel2.tryLock(10L, TimeUnit.MILLISECONDS) ) return; // Ampel auf dem anderen Weg blockieren, , max 10 ms warten
					LOGGER.info("Fahrzeug passiert Weg {}!", fahrzeug);
					Thread.sleep(random.nextInt(10));
				}
				catch (InterruptedException e) {
					LOGGER.info("Fahrzeug {} bekommt Ampel 2 nicht, Ampel 1 freigeben!", fahrzeug);
				}
				finally {
					if ( ampel2.isHeldByCurrentThread() ) ampel2.unlock();
				}
			}
			catch (InterruptedException e1) {
				LOGGER.info("Fahrzeug {} bekommt Ampel 1 nicht, neuer Versuch!", fahrzeug);
			}
			finally {
				if ( ampel1.isHeldByCurrentThread() ) ampel1.unlock();
			}
		}
	}


	/**
	 * Ein Fahrzeug, das in einer Endlosschleife die Kreuzung entweder
	 * über Weg A ({@linkplain Kreuzung#wegABefahren(Void)}) oder
	 * Weg B ({@linkplain Kreuzung#wegBBefahren(Void)}) passieren will.
	 * Mittels {@linkplain Fahrzeug#getLetzteUeberquerung()} kann festgestellt
	 * werden, ob das fahrzeug noch aktiv ist oder blockiert wurde.
	 */
	public static class Fahrzeug extends Thread {
		private final Consumer<Void> f;
		private LocalDateTime letzteUeberquerung = LocalDateTime.now();
		public Fahrzeug(final Consumer<Void> f) {
			this.f = f;
		}
		@Override
		public void run() {
			while ( true ) {
				f.accept(null);
				letzteUeberquerung = LocalDateTime.now();
			}
		}
		public LocalDateTime getLetzteUeberquerung() {
			return this.letzteUeberquerung;
		}
	}


	/**
	 * Dieser WatchDog wird in {@linkplain Deadlock#main(String[])} zusammen
	 * mit den {@linkplain Fahrzeug}en gestartet. Ist ihre letzte Durchfahrt
	 * durch die Kreuzung länger als eine Sekunde her, versucht er sie per
	 * {@linkplain Thread#interrupt()} zu beenden/aufzuwecken.
	 */
	public static class WatchDog implements Runnable {
		private final List<Fahrzeug> listeDerFahrzeuge;
		public WatchDog(final List<Fahrzeug> listeDerFahrzeuge) {
			this.listeDerFahrzeuge = listeDerFahrzeuge;
		}
		@Override
		public void run() {
			while ( true ) {
				try { Thread.sleep(1000); } catch (InterruptedException e) { }
				this.listeDerFahrzeuge.forEach(f -> {
					if ( f.getLetzteUeberquerung().isBefore(LocalDateTime.now().minusSeconds(1)) ) {
						LOGGER.info("Fahrzeug '{}' blockiert seit {}: anhalten...", f.getName(), f.getLetzteUeberquerung());
						f.interrupt();
					}
				});
			}
		}
	}


	/**
	 * Erzeugt die {@linkplain Kreuzung} und startet die beiden {@linkplain Fahrzeug}e
	 * auf Weg A und Weg B. Startet einen {@linkplain WatchDog}, der versucht, die
	 * Fahrzeuge anzuhalten, wenn sie blockiert sind. Terminiert ggf. nicht da die Threads
	 * (Fahrzeuge) in einer Endlosschleife laufen und bei Blockierung nicht auf
	 * {@linkplain Thread#interrupt()} reagieren.<p/>
	 * Es können verschieden blockierende/nicht-blockierende Kreuzungen ausprobiert werden.
	 * Dazu das Systemproperty <b>kreuzungZuTesten</b> mit dem Klassennamen verwenden.
	 */
	public static void main(String[] args) throws InterruptedException {
		String kreuzungZuTesten = System.getProperty("kreuzungZuTesten", KreuzungBlockierend.class.getSimpleName());
		final Map<String, Kreuzung> listOfKreuzung = Stream
			.of(
					KreuzungBlockierend.class, KreuzungNichtBlockierend.class,
					KreuzungLockNichtBlockierendNichtSicher.class, KreuzungLockUnterbrechbar.class,
					KreuzungLock.class
			).reduce(
				new HashMap<String, Kreuzung>(),
				(map, c) -> {
					try {
						map.put(c.getSimpleName(), c.getDeclaredConstructor().newInstance());
						return map;
					} catch ( Exception e ) {
						throw new RuntimeException(c.getSimpleName());
					}
				},
				(map1, map2) -> {
					map1.putAll(map2);
					return map1;
				}
			);
		final Kreuzung kreuzung = Optional.ofNullable(listOfKreuzung.get(kreuzungZuTesten)).orElseThrow(IllegalArgumentException::new);
		LOGGER.info("Teste Kreuzung '{}'.", kreuzungZuTesten);
		final Consumer<Void> a = kreuzung::wegABefahren, b = kreuzung::wegBBefahren;
		final List<Fahrzeug> fahrzeuge = Stream
			.of(a, b)
			.map(f -> new Fahrzeug(f))
			.collect(Collectors.toList());
		fahrzeuge.forEach(Thread::start);
		new Thread(new WatchDog(fahrzeuge)).start();
		fahrzeuge.forEach(t -> {
			try { t.join(); } catch (InterruptedException e) { }
		});
	}

}

package com.github.mbeier1406.howto.ausbildung.mt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demonstriert das Problem mit dem konkurrierend Zugriff von
 * Threads auf gemeinsame Ressourcen sowie veschiedene Lösungsansätze.
 * @author mbeier
 */
public class SharedDataProblem {

	public static final Logger LOGGER = LogManager.getLogger(SharedDataProblem.class);

	/** {@value} definiert die Anzahl der Teile, die dem {@linkplain Container} hinzugefuegt oder entnommen werden */
	public static final int ANZAHL_TEILE = 1000000;


	/**	Definiert die Methode zum Befuellen und Leeren des {@linkplain Container}s */
	@FunctionalInterface
	public static interface ContainerFuelleUndLeeren {
		int ausfuehren(final Container container) throws InterruptedException;
	}


	/**
	 * Erzeugt die verschiedenen {@linkplain Container} und {@linkplain ContainerFuelleUndLeeren Methoden}
	 * zum Fuellen und Leeren um das Problem des konkurrierenden Zugriffs zu demonstrieren.
	 */
	public static void main(String[] args) {

		/* Die unterschiedlichen Container, die getestet werden sollen */
		@SuppressWarnings("serial")
		final List<Class<? extends Container>> testContainer = new ArrayList<>() {{
			add(Container.class);
			add(ContainerMethodenSynchronisiert.class);
			add(ContainerAttributSynchronisiert.class);
			add(ContainerObjektSynchronisiert.class);
			add(ContainerObjekt2Synchronisiert.class);
			add(LockFreeContainer.class);
			add(LockFreeContainer2.class);
		}};

		/* Die unterschiedlichen Methoden, die testContainer zu Befuellen und Leeren */
		@SuppressWarnings("serial")
		final Map<String, ContainerFuelleUndLeeren> testMethoden = new HashMap<>() {{
			put("Sequentielles Befuellen und Leeren", sequentiellAusfuehren);
			put("Paralleles Befuellen und Leeren", parallelAusfuehren);
		}};

		/* Fuer jeden Container die unterschiedlichen Methoden testen */
		LOGGER.info("Start...");
		testContainer.stream().forEach(cl -> {
			LOGGER.info("Container \"{}\":", cl);
			testMethoden.entrySet().stream().forEach(m -> {
				try {
					var c = (Container) cl.getDeclaredConstructor().newInstance();
					int anzahlTeile =  m.getValue().ausfuehren(c);
					LOGGER.info("  Typ \"{}\"; Methode \"{}\": {} ({})", c.getTyp(), m.getKey(), anzahlTeile, anzahlTeile==0?"In Ordnung":"Fehler");
				} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InterruptedException | NoSuchMethodException e) {
					LOGGER.error("m={}", m.getKey(), e);
				}
			});
		});
		LOGGER.info("Fertig!");
	}


	/*
	 * Die unterschiedlichen Methoden zum Leeren und Fuellen von Containern
	 */

	/**
	 * Befuellt und entleert den {@linkplain Container} nacheinander mit {@value #ANZAHL_TEILE} Teilen.
	 * Erwartetes Ergebnis ist, dass er danach leer ist ({@linkplain Container#getAnzahlTeile()} ist <b>0</b>.
	 * @param container der Container
	 * @return Anzahl Teile im Container
	 * @throws InterruptedException
	 */
	private static ContainerFuelleUndLeeren sequentiellAusfuehren = container -> {
		var containerBefuellen = new ContainerBefuellen(container);
		var containerLeeren = new ContainerLeeren(container);
		containerBefuellen.start();
		containerBefuellen.join();
		containerLeeren.start();
		containerLeeren.join();
		return container.getAnzahlTeile();
	};


	/**
	 * Befuellt und entleert den {@linkplain Container} parallel mit {@value #ANZAHL_TEILE} Teilen.
	 * Erwartetes Ergebnis istzufaellig!
	 * @param container der Container
	 * @return Anzahl Teile im Container
	 * @throws InterruptedException
	 */
	private static ContainerFuelleUndLeeren parallelAusfuehren = container -> {
		var containerBefuellen = new ContainerBefuellen(container);
		var containerLeeren = new ContainerLeeren(container);
		containerBefuellen.start();
		containerLeeren.start();
		containerBefuellen.join();
		containerLeeren.join();
		return container.getAnzahlTeile();
	};


	/*
	 * Die unterschiedlichen Container
	 */

	/** Stellt den <i>nicht-synchronisierten</i> Container bereit, der die Anzahl der enthaltenen Teile speichert */
	public static class Container {
		protected int anzahlTeile = 0;
		protected String getTyp() {
			return "Nicht-synchronisierter Container";
		}
		public int getAnzahlTeile() {
			return anzahlTeile;
		}
		public void teilEinfuegen() {
			anzahlTeile++;
		}
		public void teilEntnehmen() {
			anzahlTeile--;
		}
		@Override
		public String toString() {
			return "Container [anzahlTeile=" + anzahlTeile + "]";
		}
	}

	/** Stellt den <i>Methoden-synchronisierten</i> (Monitor) Container bereit, der die Anzahl der enthaltenen Teile speichert */
	public static class ContainerMethodenSynchronisiert extends Container {
		@Override
		protected String getTyp() {
			return "Methoden-synchronisierter Container";
		}
		@Override
		public synchronized void teilEinfuegen() {
			anzahlTeile++;
		}
		@Override
		public synchronized void teilEntnehmen() {
			anzahlTeile--;
		}
	}

	/** Stellt den <i>Attribut-synchronisierten</i> Container bereit, der die Anzahl der enthaltenen Teile speichert */
	public static class ContainerAttributSynchronisiert extends Container {
		private AtomicInteger anzahlTeile = new AtomicInteger(0);
		@Override
		protected String getTyp() {
			return "Attribut-synchronisierter Container";
		}
		@Override
		public void teilEinfuegen() {
			anzahlTeile.addAndGet(1);
		}
		@Override
		public void teilEntnehmen() {
			anzahlTeile.addAndGet(-1);
		}
		@Override
		public int getAnzahlTeile() {
			return anzahlTeile.get();
		}
	}

	/** Stellt den <i>Objekt-synchronisierten</i> Container bereit, der die Anzahl der enthaltenen Teile speichert */
	public static class ContainerObjektSynchronisiert extends Container {
		@Override
		protected String getTyp() {
			return "Objekt-synchronisierter Container";
		}
		@Override
		public void teilEinfuegen() {
			synchronized(this) {
				anzahlTeile++;
			}
		}
		@Override
		public void teilEntnehmen() {
			synchronized(this) {
				anzahlTeile--;
			}
		}
	}

	/** Stellt den <i>Objekt-synchronisierten</i> (Lock-Objekt) Container bereit, der die Anzahl der enthaltenen Teile speichert */
	public static class ContainerObjekt2Synchronisiert extends Container {
		private Object lock = new Object();
		@Override
		protected String getTyp() {
			return "Objekt-synchronisierter Container";
		}
		@Override
		public void teilEinfuegen() {
			synchronized(this.lock) {
				anzahlTeile++;
			}
		}
		@Override
		public void teilEntnehmen() {
			synchronized(this.lock) {
				anzahlTeile--;
			}
		}
	}

	/** Verwendet Klassen aus <code>java.util.concurrent.atomic</code> um die Nachteile von Locks zu umgehen */
	public static class LockFreeContainer extends Container {
		private AtomicInteger anzahlTeile = new AtomicInteger(0);
		@Override
		protected String getTyp() {
			return "Lock-free Container";
		}
		@Override
		public int getAnzahlTeile() {
			return anzahlTeile.get();
		}
		@Override
		public void teilEinfuegen() {
			anzahlTeile.incrementAndGet();
		}
		@Override
		public void teilEntnehmen() {
			anzahlTeile.decrementAndGet();
		}
	}

	/** Synchronisiert ohne Locking mittels {@linkplain AtomicReference} und compareAndSet() */
	public static class LockFreeContainer2 extends Container {
		private AtomicReference<Integer> anzahlTeile = new AtomicReference<>(0);
		@Override
		protected String getTyp() {
			return "Lock-free Container2";
		}
		@Override
		public int getAnzahlTeile() {
			return anzahlTeile.get();
		}
		@Override
		public void teilEinfuegen() {
			wertAendern(i -> i+1);
		}
		@Override
		public void teilEntnehmen() {
			wertAendern(i -> i-1);
		}
		private void wertAendern(Function<Integer, Integer> f) {
			Integer alt, neu; // nicht int nehmen, da beim Boxing ab 128 neue Objekte erzeugt werden (Identitätsprüfung in compareAndSet)
			do {
				alt = anzahlTeile.get();
				neu = f.apply(alt);
			} while ( !anzahlTeile.compareAndSet(alt, neu) );
		}
	}

	/*
	 * Die Klassen zum Befuellen und Leeren von Containern
	 */

	/** Der Thread diese Klasse befüllt den {@linkplain Container} mit {@value #ANZAHL_TEILE} Teilen */
	public static class ContainerBefuellen extends Thread {
		private final Container container;
		public ContainerBefuellen(Container container) {
			super();
			this.container = container;
		}
		@Override
		public void run() {
			for ( int i=0; i < ANZAHL_TEILE; i++ )
				container.teilEinfuegen();
		}
	}

	/** Der Thread diese Klasse entnimmt dem {@linkplain Container} {@value #ANZAHL_TEILE} Teile */
	public static class ContainerLeeren extends Thread {
		private final Container container;
		public ContainerLeeren(Container container) {
			super();
			this.container = container;
		}
		@Override
		public void run() {
			for ( int i=0; i < ANZAHL_TEILE; i++ )
				container.teilEntnehmen();
		}
	}

}

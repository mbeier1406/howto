package com.github.mbeier1406.howto.ausbildung.mt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demonstriert das Problem mit dem konkurrierend Zugriff von
 * Threads auf gemeinsame Ressourcen.
 * @author mbeier
 */
public class SharedDataProblem {

	public static final Logger LOGGER = LogManager.getLogger(SharedDataProblem.class);

	/** {@value} definiert die Anzahl der Teile, die dem {@linkplain Container} hinzugefuegt oder entnommen werden */
	public static final int ANZAHL_TEILE = 1000000;

	private static Container newInstance;


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
		final List<Class<? extends Container>> testContainer = new ArrayList<>() {{
			add(Container.class);
			add(ContainerMethodenSynchronisiert.class);
		}};

		/* Die unterschiedlichen Methoden, die testContainer zu Befuellen und Leeren */
		final Map<String, ContainerFuelleUndLeeren> testMethoden = new HashMap<>() {{
			put("Sequentielles Befuellen und Leeren", sequentiellAusfuehren);
			put("Paralleles Befuellen und Leeren", parallelAusfuehren);
		}};

		/* Fuer jeden Container die unterschiedlichen Methoden testen */
		testContainer.stream().forEach(cl -> {
			LOGGER.info("Container \"{}\":", cl);
			testMethoden.entrySet().stream().forEach(m -> {
				try {
					var c = (Container) cl.getDeclaredConstructors()[0].newInstance();
					int anzahlTeile =  m.getValue().ausfuehren(c);
					LOGGER.info("  Typ \"{}\"; Methode \"{}\": {} ({})", c.getTyp(), m.getKey(), anzahlTeile, anzahlTeile==0?"In Ordnung":"Fehler");
				} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InterruptedException e) {
					LOGGER.error("m={}", m.getKey(), e);
				}
			});
		});
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

	/** Stellt den <i>synchronisierten</i> Container bereit, der die Anzahl der enthaltenen Teile speichert */
	public static class ContainerMethodenSynchronisiert extends Container {
		protected String getTyp() {
			return "Methoden-synchronisierter Container";
		}
		public synchronized void teilEinfuegen() {
			anzahlTeile++;
		}
		public synchronized void teilEntnehmen() {
			anzahlTeile--;
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

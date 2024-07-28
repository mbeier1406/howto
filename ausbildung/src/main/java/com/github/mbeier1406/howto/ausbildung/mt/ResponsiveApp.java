package com.github.mbeier1406.howto.ausbildung.mt;

import com.github.mbeier1406.howto.ausbildung.mt.ResponsiveApp.WaehrungsHandelssystem.WAEHRUNGEN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Ein Beispiel für eine Anwendung mit User-Interface, in dem sich zwei Threads
 * eine Ressource teilen, eine Klasse, die anzuzeigende Werte enthält. Die beiden
 * Threads sind:
 * <ol>
 * <li>Der Java-FX GUI-Thread, der die Benutzeroberfläche steuert, und</li>
 * <li>der Update-Thread, der die anzuzeigende Werte ändert.
 * </ol>
 * Es wird in der Anwendung vorausgestzt, dass die anzuzeigenden Werte nicht gleichzeitig
 * gelesen und geändert werden können, so dass der Zugriff auf die Ressource synchronisiert
 * werden muss. Je nach verwendeter Blocking Methode ist die Anwendung reaktiv, oder
 * sie "<i>hakelt<i>", d. h. ist nicht (gut) benutzbar.
 * @author mbeier
 */
public class ResponsiveApp extends Application {

	public static final Logger LOGGER = LogManager.getLogger(ResponsiveApp.class);

	/** Breite Anwendungsfenster ist {@value} */
	public static final double BREITE = 400;

	/** Höhe Anwendungsfenster ist {@value} */
	public static final double HOEHE = 250;


	/** Bildschirmdefinition */
	private Rectangle2D screen = Screen.getPrimary().getVisualBounds();

	/** Die geteilte Ressource: Das Handelssystem, blockierend oder nicht, mit dem getestet wird */
	private static WaehrungsHandelssystem waehrungsHandelssystem;

	/** Diese Map enthält die anzuzeigenden Werte mit Namen/Label */
	private static Map<WAEHRUNGEN, Waehrungsanzeiger> anzeige;


	/** Für jede Währung einen {@linkplain Label} zur Anzeige des Wertes */
	public static record Waehrungsanzeiger (Label label, WaehrungsHandelssystem.Waehrung waehrung) {};


	/**
	 * Startet die GUI der Anwendung mit dem einen Thread, der die GUI steuert
	 * und die anzuzeigenden Werte aktualisiert. Bei blockierender geteilter
	 * Ressource ({@linkplain WaehrungsHandelssystem} wird dieser Thread
	 * unterbrochen und die GUI ist nicht benutzbar.
	 */
	@Override
	public void start(final Stage primaryStage) {

		/* Anzeige der Werte: Klick auf eine Währung ändert die Farbe */
		final var gridPane = new GridPane();
		gridPane.setVgap(10.0);
		gridPane.setHgap(10.0);
		gridPane.setAlignment(Pos.CENTER);
		final var row = new AtomicInteger(-1);
		anzeige
			.entrySet()
			.stream()
			.forEach(e -> {
				var label = new Label(e.getKey().name());
				label.setTextFill(Color.BLUE);
				label.setOnMousePressed(event -> label.setTextFill(Color.RED));
				label.setOnMouseReleased(event -> label.setTextFill(Color.BLUE));
				gridPane.add(label, 0, row.addAndGet(1));
				gridPane.add(e.getValue().label(), 1, row.get());
			});

		/* Hintergrundfarbe sekündlich wechseln */
		final var background = new Rectangle(BREITE, HOEHE);
		final var fillTransition = new FillTransition(Duration.millis(1000), background, Color.LIGHTGRAY, Color.LIGHTBLUE);
		fillTransition.setCycleCount(Timeline.INDEFINITE);
		fillTransition.setAutoReverse(true);
		fillTransition.play();

		/* Baut die GUI zusammen */
		final var root = new StackPane();
		root.getChildren().addAll(background, gridPane);

		/* Diese Klasse liest aus der geteilten Ressource und blockiert die GUI, falls der Aufruf blockiert wird */
		final var waehrungsHandelssystemReader = new WaehrungsHandelssystemReader(waehrungsHandelssystem);

		/* Startet das aktualisieren der Währungsanzeige */
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				waehrungsHandelssystemReader.update();
			}
		}.start();

		/* GUI anzeigen */
		primaryStage.setTitle("Handelssystem - Währungen");
		primaryStage.setScene(new Scene(root, BREITE, HOEHE));
		primaryStage.setX(screen.getWidth()/2-BREITE/2); // Anwendung zentral am oberen Bildschirmrand plazieren
		primaryStage.setY(150);
		primaryStage.show();
	}


	/** Initialisiert das (nicht-)blockierende Handelssystem und startet die beiden Threads Lesen/Schreiben */
	public static void main(String[] args) {
		// waehrungsHandelssystem = new WaehrungsHandelssystemBlockierend(); // verwendet die blockierende geteilte Ressource -> GUI nicht benutzbar
		waehrungsHandelssystem = new WaehrungsHandelssystemNichtBlockierend(); // verwendet die nicht-blockierende geteilte Ressource -> GUI bleibt benutzbar
		anzeige = waehrungsHandelssystem
				.getWaehrungsListe()
				.stream()
				.map(w -> new Waehrungsanzeiger(new Label("0"), w))
				.collect(Collectors.toMap(x -> x.waehrung.getName(), x -> x));
		new WaehrungsHandelssystemFeeder(waehrungsHandelssystem).start();
		launch(args);
	}


	/** Definiert die Schnittstelle zur geteilten Ressource */
	public static interface WaehrungsHandelssystem {
		public static class Waehrung {
			private final WAEHRUNGEN waehrung;
			private double wert;
			public Waehrung(WAEHRUNGEN waehrung) {
				this.waehrung = waehrung;
				this.wert = 0;
			}
			public WAEHRUNGEN getName() { return this.waehrung; }
			public double getWert() { return this.wert; }
			public void setWert(double wert) { this.wert = wert; }
		}		
		public static enum WAEHRUNGEN { EUR, GBP, USD, DKR, PLS };
		public static final List<Waehrung> WAEHRUNGSLISTE = new ArrayList<>();
		public List<Waehrung> getWaehrungsListe();
		/** Diese Methode wird vom Lese-GUI-Thread JavaFX verwendet */
		public double getWert(WAEHRUNGEN waehrung) throws IllegalArgumentException;
		/** Diese Methode wird vom Schreib-Thread {@linkplain WaehrungsHandelssystemFeeder} verwendet */
		public void wertAktualisieren(Waehrung waehrung, double wert) throws IllegalArgumentException;
	}


	/** Der Schreib-Thread verwendet die geteilte Ressource {@linkplain WaehrungsHandelssystem} zum Aktuaisieren der Werte */
	public static class WaehrungsHandelssystemFeeder extends Thread {
		private final WaehrungsHandelssystem waehrungsHandelssystem;
		private Random random = new Random();
		public WaehrungsHandelssystemFeeder(final WaehrungsHandelssystem waehrungsHandelssystem) {
			LOGGER.info("Feeder Start...");
			this.waehrungsHandelssystem = waehrungsHandelssystem;
		}
		@Override
		public void run() {
			while ( true ) {
				this.waehrungsHandelssystem
					.getWaehrungsListe()
					.forEach(w -> this.waehrungsHandelssystem.wertAktualisieren(w, random.nextInt(100)));
				try { Thread.sleep(100); } catch (InterruptedException e) { }
			}
		}
	}


	/** Diese Klasse wird vom GUI-Thread verwendet, um die aktualisierten Werte in der GUI zu aktualisieren */
	public static class WaehrungsHandelssystemReader {
		private final WaehrungsHandelssystem waehrungsHandelssystem;
		private Random random = new Random();
		public WaehrungsHandelssystemReader(final WaehrungsHandelssystem waehrungsHandelssystem) {
			LOGGER.info("Reader Start...");
			this.waehrungsHandelssystem = waehrungsHandelssystem;
		}
		public void update() {
			anzeige
				.entrySet()
				.stream()
				.forEach(w -> {
					double wert = this.waehrungsHandelssystem.getWert(w.getKey());
					if ( wert != -1.0 ) w.getValue().label().setText(String.valueOf(wert));
				});
		}
	}


	/** Diese geteilte Ressource blockiert Lese- und Schreibzugriffe und macht die GUI unbenutzbar */
	public static class WaehrungsHandelssystemBlockierend implements WaehrungsHandelssystem {
		static {
			Arrays.stream(WAEHRUNGEN.values()).forEach(w -> WAEHRUNGSLISTE.add(new Waehrung(w)));
		}
		private Lock lock = new ReentrantLock();
		private Random random = new Random();
		@Override
		public final List<Waehrung> getWaehrungsListe() { return WAEHRUNGSLISTE; }
		@Override
		public double getWert(WAEHRUNGEN waehrung) throws IllegalArgumentException {
			lock.lock();
			try {
				Optional<Waehrung> wertZuALiefern = getWaehrungsListe()
						.stream()
						.filter(w -> w.getName().equals(waehrung))
						.findAny();
				if ( wertZuALiefern.isEmpty() )
					throw new IllegalArgumentException(waehrung.toString());
				else {
					LOGGER.info("Liefere {}: {}", waehrung, wertZuALiefern.get().getWert());
					return wertZuALiefern.get().getWert();
				}
			}
			finally {
				lock.unlock();
			}
		}
		@Override
		public void wertAktualisieren(Waehrung waehrung, double wert) throws IllegalArgumentException {
			lock.lock();
			try {
				Optional<Waehrung> wertZuAktualisieeren = getWaehrungsListe()
					.stream()
					.filter(w -> w.getName().equals(waehrung.getName()))
					.findAny();
				if ( wertZuAktualisieeren.isEmpty() )
					throw new IllegalArgumentException(waehrung.toString());
				else {
					LOGGER.info("Aktualisiere {}: {}", wertZuAktualisieeren.get().getName(), wert);
					wertZuAktualisieeren.get().setWert(wert);
					try {
						Thread.sleep(random.nextInt(1500));
					} catch (InterruptedException e) { }
				}
			}
			finally {
				lock.unlock();
			}
		}
	}

	/** Diese geteilte Ressource blockiert Lese- und Schreibzugriffe NICHT: die GUI bleibt benutzbar */
	public static class WaehrungsHandelssystemNichtBlockierend implements WaehrungsHandelssystem {
		static {
			Arrays.stream(WAEHRUNGEN.values()).forEach(w -> WAEHRUNGSLISTE.add(new Waehrung(w)));
		}
		private Lock lock = new ReentrantLock();
		private Random random = new Random();
		@Override
		public final List<Waehrung> getWaehrungsListe() { return WAEHRUNGSLISTE; }
		@Override
		public double getWert(WAEHRUNGEN waehrung) throws IllegalArgumentException {
			if ( lock.tryLock() ) {
				try {
					Optional<Waehrung> wertZuALiefern = getWaehrungsListe()
							.stream()
							.filter(w -> w.getName().equals(waehrung))
							.findAny();
					if ( wertZuALiefern.isEmpty() )
						throw new IllegalArgumentException(waehrung.toString());
					else {
						LOGGER.info("Liefere {}: {}", waehrung, wertZuALiefern.get().getWert());
						return wertZuALiefern.get().getWert();
					}
				}
				finally {
					lock.unlock();
				}
			}
			else
				return -1.0; // Nicht anzeigen
		}
		@Override
		public void wertAktualisieren(Waehrung waehrung, double wert) throws IllegalArgumentException {
			if ( lock.tryLock() ) {
				try {
					Optional<Waehrung> wertZuAktualisieeren = getWaehrungsListe()
						.stream()
						.filter(w -> w.getName().equals(waehrung.getName()))
						.findAny();
					if ( wertZuAktualisieeren.isEmpty() )
						throw new IllegalArgumentException(waehrung.toString());
					else {
						LOGGER.info("Aktualisiere {}: {}", wertZuAktualisieeren.get().getName(), wert);
						wertZuAktualisieeren.get().setWert(wert);
						try {
							Thread.sleep(random.nextInt(1500));
						} catch (InterruptedException e) { }
					}
				}
				finally {
					lock.unlock();
				}
			}
		}
	}

}

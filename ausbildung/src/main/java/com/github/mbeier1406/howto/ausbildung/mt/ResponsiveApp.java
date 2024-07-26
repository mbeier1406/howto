package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class ResponsiveApp extends Application {

	public static final Logger LOGGER = LogManager.getLogger(ResponsiveApp.class);

	/** Breite Anwendungsfenster ist {@value} */
	public static final double BREITE = 400;

	/** Höhe Anwendungsfenster ist {@value} */
	public static final double HOEHE = 250;


	/** Bildschirmdefinition */
	private Rectangle2D screen = Screen.getPrimary().getVisualBounds();


	@Override
	public void init() {
		LOGGER.info("Init...");
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		LOGGER.info("Halt.");
	}

	/** Für jede Währung einen {@linkplain Label} zur Anzeige des Wertes */
	public static record Waehrungsanzeiger (Label label, WaehrungsHandelssystem.Waehrung waehrung) {};

	@Override
	public void start(final Stage primaryStage) {

		final var anzeige = WaehrungsHandelssystem
			.getWaehrungsListe()
			.stream()
			.map(w -> new Waehrungsanzeiger(new Label("0"), w))
			.collect(Collectors.toMap(x -> x.waehrung.getName(), x -> x));

		final var gridPane = new GridPane();
		gridPane.setVgap(10.0);
		gridPane.setHgap(10.0);
		gridPane.setAlignment(Pos.CENTER);
		final var row = new AtomicInteger(-1);
		anzeige
			.entrySet()
			.stream()
			.forEach(e -> {
				var label = new Label(e.getKey());
				label.setTextFill(Color.BLUE);
				label.setOnMousePressed(event -> label.setTextFill(Color.RED));
				label.setOnMouseReleased(event -> label.setTextFill(Color.BLUE));
				gridPane.add(label, 0, row.addAndGet(1));
				gridPane.add(e.getValue().label(), 1, row.get());
			});

		final var background = new Rectangle(BREITE, HOEHE);
		final var fillTransition = new FillTransition(Duration.millis(1000), background, Color.LIGHTGRAY, Color.LIGHTBLUE);
		fillTransition.setCycleCount(Timeline.INDEFINITE);
		fillTransition.setAutoReverse(true);
		fillTransition.play();

		final var root = new StackPane();
		root.getChildren().addAll(background, gridPane);

		primaryStage.setTitle("Handelssystem - Währungen");
		primaryStage.setScene(new Scene(root, BREITE, HOEHE));
		primaryStage.setX(screen.getWidth()/2-BREITE/2); // Anwendung zentral am oberen Bildschirmrand plazieren
		primaryStage.setY(150);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


	public static class WaehrungsHandelssystem {
		public static class Waehrung {
			private final String waehrung;
			private double wert;
			public Waehrung(String waehrung) {
				this.waehrung = waehrung;
				this.wert = 0;
			}
			public String getName() { return this.waehrung; }
			public double getWert() { return this.wert; }
			public void setWert(double wert) { this.wert = wert; }
		}
		public static final String[] WAEHRUNGEN = { "EUR", "GBP", "USD", "DKR", "PLS" };
		public static final List<Waehrung> WAEHRUNGSLISTE = new ArrayList<>();
		static {
			Arrays.stream(WAEHRUNGEN).forEach(w -> WAEHRUNGSLISTE.add(new Waehrung(w)));
		}
		public static final List<Waehrung> getWaehrungsListe() { return WAEHRUNGSLISTE; }
	}

}

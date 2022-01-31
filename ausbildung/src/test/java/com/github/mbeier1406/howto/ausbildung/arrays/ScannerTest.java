package com.github.mbeier1406.howto.ausbildung.arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.arrays.Scanner.Camera;
import com.github.mbeier1406.howto.ausbildung.arrays.impl.StdScannerImpl;

/**
 * Tests für das Interface {@linkplain Scanner} und die Standardimplementierung in {@linkplain StdScannerImpl}.
 * @author mbeier
 */
public class ScannerTest {

	public static final Logger LOGGER = LogManager.getLogger(ScannerTest.class);

	/** Das zu testende Objekt */
	public Scanner scanner = new StdScannerImpl(1, "Scanner 1");

	/** Ein paar Kameras zum testen aufsteigend nach Nummer sortiert */
	public Camera[] listOfCameras = {
			new Camera(1, 1000, "Camera 1"),
			new Camera(2, 2000, "Camera 2"),
			new Camera(3, 3000, "Camera 3"),
	};

	/** {@linkplain #scanner} initialisieren, {@linkplain #listOfCameras} für Tests unverändert */
	@BeforeEach
	public void init() {
		scanner.setCameras(scanner.cloneCameraList(listOfCameras)); // Kopie verwenden
	}

	/** Prüft {@linkplain Scanner#STD_CAMERA_GENERATOR} mit {@linkplain Arrays#setAll(Object[], java.util.function.IntFunction)} */
	@Test
	public void testeCamerasUeberFunktionErzeugen() {
		int anzahlKameras = 5;
		scanner.setCameras(anzahlKameras, Scanner.STD_CAMERA_GENERATOR);
		Camera[] cameras = scanner.getCameras();
		Arrays.stream(cameras).forEach(LOGGER::info);
		pruefeStdKameraListe(cameras,anzahlKameras);
	}

	/** Prüfen, ob das Clonen eines Arrays funktioniert: unterschiedliche Instanzen mit gleichem Inhalt prüfen */
	@Test
	public void pruefeArrayClonen() {
		Camera[] listOfClonedCameras = scanner.getCameras(); //Liefert eine Kopie, neue Instanzen mit gleichem Inhalt
		Stream.concat(Arrays.stream(listOfCameras), (Arrays.stream(listOfClonedCameras))).forEach(LOGGER::info);
		for ( int i=0; i < listOfCameras.length; i++ )
			assertThat(listOfCameras[i], allOf(
					not(sameInstance(listOfClonedCameras[i])),
					equalTo(listOfClonedCameras[i])));
	}

	/** Prüfen, ob Kameras als Zeichenkette korrekt ausgegeben werden */
	@Test
	public void testeKamerasAlszeichenkette() {
		assertThat(scanner.camerasAsString(), equalTo("[" +
				"Camera [nummer=1, resolution=1000, name=Camera 1, sumResolution=0], " +
				"Camera [nummer=2, resolution=2000, name=Camera 2, sumResolution=0], " +
				"Camera [nummer=3, resolution=3000, name=Camera 3, sumResolution=0]]"));
	}

	/** Prüfen, ob beim links-verschieben die Kameras korrekt angeordnet sind */
	@Test
	public void testeLeftShift() {
		Camera[] listOfCamerasShifted = scanner.leftShiftCameras();
		assertThat(listOfCamerasShifted.length, equalTo(listOfCameras.length));
		for ( int i=0; i < listOfCamerasShifted.length; i++ )
			assertThat(listOfCamerasShifted[i], equalTo(listOfCameras[(i+1)%listOfCamerasShifted.length]));
	}

	/** Kamerareihenfolge verschieben, sortieren und gegen den Originalbestand prüfen */
	@Test
	public void testeKamerasSortieren() {
		scanner.leftShiftCameras(); // Originalliste weicht von Scannerreihenfolge ab
		LOGGER.info("unsortiert: {}", scanner.camerasAsString());
		Camera[] sortedCameras = scanner.sortCameras();
		LOGGER.info("sortiert: {}", scanner.camerasAsString());
		assertThat(sortedCameras.length, equalTo(listOfCameras.length));
		for ( int i=0; i < sortedCameras.length; i++ )
			assertThat(sortedCameras[i], equalTo(listOfCameras[i]));
	}

	/** Summe über Scannerauflösung in {@linkplain #listOfCameras} geteilt durch drei (Scanner) */
	@Test
	public void testeAvgResolution() {
		assertThat(scanner.getAvgResolution(), equalTo(2000));
	}

	/** Prüfen, ob die initialisierte Liste im Scanner ist */
	@Test
	public void testeListOfCameraEquals() {
		assertThat(scanner.listOfCameraEquals(listOfCameras), equalTo(true));
	}

	/** Prüfen, ob das Erweitern der Kameraliste im Scanner funktioniert */
	@Test
	public void testeListOfCameraErweitern() {
		Camera[] cameras = scanner.appendCameras(new Camera[] { new Camera(4, 4000, "Camera 4"), new Camera(5, 5000, "Camera 5") });
		Arrays.stream(scanner.getCameras()).forEach(LOGGER::info);
		pruefeStdKameraListe(cameras, 5);
	}

	/** Suchen einer Kamera in der Kameraliste */
	@Test
	public void testeCameraSuchen() {
		Camera camera3 = new Camera(3, 2000, "Camera 3");
		Camera[] cameras = new Camera[] { // Liste ist nicht soritert!
				camera3,
				new Camera(1, 1000, "Camera 1"),
				new Camera(2, 1500, "Camera 2"),
				new Camera(5, 5500, "Camera 5"),
				new Camera(4, 2500, "Camera 4")
		};
		scanner.setCameras(cameras);
		Optional<Camera> camera = scanner.findCamera(6);
		assertThat(camera.isEmpty(), equalTo(true)); // Kamera Nummer 6 gibt es nicht
		camera = scanner.findCamera(3);
		assertThat(camera.isPresent(), equalTo(true));
		assertThat(camera.get(), equalTo(camera3));
	}

	/** kein Test: Demonstiert die erweiterte Schleife mit ":" und das Setzen mit Aufzählung ","-separiert statt Array */
	@Test
	public void demoErweiterteSchleife() {
		scanner.setCameras(
				new Camera(1, 1000, "Camera 1"),
				new Camera(2, 1500, "Camera 2"),
				new Camera(3, 2000, "Camera 3"),
				new Camera(4, 2500, "Camera 4"));
		for ( Camera c : scanner.getCameras() )
			LOGGER.info(c);
	}

	/**
	 * Prüft, ob eine Liste von Kameras den Vorgaben dieses Tests entsprechen:
	 * <i>nummer = Index+1, Auflösung = 1000*nummer, Name = "Camera &lt;nummer&gt;"</i>
	 * @param cameras Liste der zur prüfenden Kameras
	 */
	public void pruefeStdKameraListe(Camera[] cameras, int anzahlKameras) {
		assertThat(cameras.length, equalTo(anzahlKameras));
		for ( int i=1; i <= cameras.length; i++ )
			assertThat(cameras[i-1], allOf(
					hasProperty("nummer", equalTo(i)),
					hasProperty("resolution", equalTo(i*1000)),
					hasProperty("name", equalTo("Camera "+i))));

	}

}

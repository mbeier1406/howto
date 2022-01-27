package com.github.mbeier1406.howto.ausbildung.arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
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

	/** Ein paar Kameras zum testen */
	public Camera[] listOfCameras = {
			new Camera(1, 1000, "Camera 1"),
			new Camera(2, 1500, "Camera 2"),
			new Camera(3, 2000, "Camera 3"),
	};

	/** {@linkplain #scanner} initialisieren */
	@BeforeEach
	public void init() {
		scanner.setCameras(listOfCameras);
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

}

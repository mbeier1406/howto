package com.github.mbeier1406.howto.ausbildung.basic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.basic.IOFunctions.ExampleData;
import com.github.mbeier1406.howto.ausbildung.basic.impl.IOFunctionsImpl;
import com.github.mbeier1406.howto.ausbildung.basic.impl.IOFunctionsSwingImpl;

/**
 * Tests für die {@linkplain IOFunctions}-Implementierung in {@linkplain IOFunctionsSwingImpl}.
 * @author mbeier
 */
public class IOFunctionsTest {

	public static final Logger LOGGER = LogManager.getLogger(IOFunctionsTest.class);

	/** Eingabedatei für {@linkplain ExampleData} ist {@value} */
	public static final String EXAMPLE_DATA_FILE = "ExampleData.txt";

	/** Das zu testende Objekt */
	public static IOFunctions ioFunctions;

	/** Initialisierung {@linkplain #ioFunctions} */
	@BeforeAll
	public static void init() {
		ioFunctions = new IOFunctionsImpl();
	}

	/** {@linkplain ExampleData} über Tastatureingabe einlesen */
	@Disabled("Kein automatischer Test - erfordert Tatstatureingaben")
	@Test
	public void testeEingabeAusStdIn() {
		ExampleData exampleData = ioFunctions.readExampleData(System.in, StandardCharsets.UTF_8.toString());
		LOGGER.info("exampleData={}", exampleData);
	}

	/** {@linkplain ExampleData} aus {@value #EXAMPLE_DATA_FILE} einlesen und Inhalt prüfen */
	@Test
	public void testeEingabeAusDatei() {
		ExampleData exampleData = ioFunctions.readExampleData(IOFunctionsTest.class.getResourceAsStream(EXAMPLE_DATA_FILE), StandardCharsets.UTF_8.toString());
		LOGGER.info("exampleData={}", exampleData);
		assertThat("ExampleData [str=abc, i=123, f=654.321]", equalTo(exampleData.toString()));
	}

}

package com.github.mbeier1406.howto.ausbildung.basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.basic.IOFunctions.ExampleData;
import com.github.mbeier1406.howto.ausbildung.basic.impl.IOFunctionsImpl;
import com.github.mbeier1406.howto.ausbildung.basic.impl.IOFunctionsSwingImpl;

/**
 * Tests für die {@linkplain IOFunctions}-Implementierung in {@linkplain IOFunctionsImpl}.
 * @author mbeier
 */
public class IOFunctionsSwingTest {

	public static final Logger LOGGER = LogManager.getLogger(IOFunctionsSwingTest.class);

	/** Eingabedatei für {@linkplain ExampleData} ist {@value} */
	public static final String EXAMPLE_DATA_FILE = "ExampleData.txt";

	/** Das zu testende Objekt */
	public static IOFunctions ioFunctions;

	/** Initialisierung {@linkplain #ioFunctions} */
	@BeforeAll
	public static void init() {
		ioFunctions = new IOFunctionsSwingImpl();
	}

	/** {@linkplain ExampleData} über Swing-Eingabefeld einlesen */
	@Disabled("Kein automatischer Test - erfordert Tatstatureingaben")
	@Test
	public void testeEingabeUeberSwingDialog() {
		ExampleData exampleData = ioFunctions.readExampleData(null, null); // kein InputStream notwendig, default Characterset
		LOGGER.info("exampleData={}", exampleData);
	}

}

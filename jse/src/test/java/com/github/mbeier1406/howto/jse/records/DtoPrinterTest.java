package com.github.mbeier1406.howto.jse.records;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Testet die Eigenschaften von Records (hier: {@linkplain DtoPrinter}).
 * @author mbeier
 */
public class DtoPrinterTest {

	public static final Logger LOGGER = LogManager.getLogger(DtoPrinterTest.class);

	public final DtoPrinterImpl dtoPrinterImpl = new DtoPrinterImpl("test", 1);

	/** Records haben automatisch eine {@code toString()}-Methode */
	@Test
	public void testeToString() {
		LOGGER.info("dtoPrinterImpl={}", dtoPrinterImpl);
		assertThat(dtoPrinterImpl.toString(), equalTo("DtoPrinterImpl[str=test, num=1]"));
	}

	/** Records haben automatisch Getter für alle Properties */
	@Test
	public void testeGetter() {
		assertThat(dtoPrinterImpl.str(),  equalTo("test"));
		assertThat(dtoPrinterImpl.num(),  equalTo(1));
	}

	/** Records haben automatisch eine {@code equals()}- und {@code hashCode()}-Methode */
	@Test
	public void testeEquals() {
		assertThat(dtoPrinterImpl,  equalTo(new DtoPrinterImpl("test", 1)));
	}

	/** Konstruktoren können überladen werden */
	@Test
	public void testeKontruktorMitPruefungen() {
		IllegalArgumentException assertThrows = assertThrows(IllegalArgumentException.class, () -> {
			new DtoPrinterImpl("test", null);
		});
		assertThat(assertThrows.getMessage(), equalTo("num ist NULL"));
	}

	/** Records können Interfaces implementieren */
	@Test
	public void testeImplementierung() {
		assertThat(dtoPrinterImpl.printFormatted(), equalTo("test 4"));		
	}

}

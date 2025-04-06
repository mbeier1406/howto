package com.github.mbeier1406.howto.ausbildung.gof.creational;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Implementierungen des {@linkplain Prototype}-Interfaces
 * und der Registry {@linkplain PrototypeRegistry}.
 */
public class PrototypeTest {

	/** Der Service, mit dem getestet wird */
	private static final String IMPL1 = "impl1";

	/** Stellt sicher, dass bei einem gültigen Keyword ein Objekt erzeugt wird */
	@Test
	public void testePrototypenNichtNull() {
		assertThat(PrototypeRegistry.getPrototype(IMPL1), not(equalTo(null)));
	}

	/** Stellt sicher, dass es sich bei einer neuen Kopie um neues Objekt handelt */
	@Test
	public void testePrototypenUngleich() {
		// Testet nicht das deep cloning!
		var p1 = PrototypeRegistry.getPrototype(IMPL1);
		var p2 = PrototypeRegistry.getPrototype(IMPL1);
		assertThat(p1 == p2, not(equalTo(true)));
	}

	/** Eine ungültige Kennung erzeugt eine Exception */
	@Test
	public void testeUngueltigenPrototypen() {
		assertThrows(IllegalArgumentException.class, () -> PrototypeRegistry.getPrototype("XYZ"));
	}

}

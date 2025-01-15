package com.github.mbeier1406.howto.ausbildung.gof.creational;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Implementierungen des {@linkplain Prototype}-Interfaces
 * und der Factory {@linkplain PrototypeRegistry}.
 */
public class PrototypeTest {

	/** Stellt sicher, dass bei einem gültigen Keyword ein Objekt erzeugt wird */
	@Test
	public void testePrototypenNichtNull() {
		assertThat(PrototypeRegistry.getPrototype("impl1"), not(equalTo(null)));
	}

	/** Stellt sicher, dass es sich bei einer neuen Kopie um neues Objekt handelt */
	@Test
	public void testePrototypenUngleich() {
		// Testet nicht das deep cloning!
		var p1 = PrototypeRegistry.getPrototype("impl1");
		var p2 = PrototypeRegistry.getPrototype("impl1");
		assertThat(p1 == p2, not(equalTo(true)));
	}

	/** Beim Versuch, einen ungültigen  */
	@Test
	public void testeUngueltigenPrototypen() {
		assertThrows(IllegalArgumentException.class, () -> PrototypeRegistry.getPrototype("XYZ"));
	}

}

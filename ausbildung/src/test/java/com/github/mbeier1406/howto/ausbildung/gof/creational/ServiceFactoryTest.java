package com.github.mbeier1406.howto.ausbildung.gof.creational;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Implementierungen des {@linkplain Service}-Interfaces und der Factory {@linkplain ServiceFactory}.
 */
public class ServiceFactoryTest {

	/** Der Service, mit dem getestet wird */
	private static final String SERVICE1 = "Service1";

	/** Stellt sicher, dass bei einem gültigen Keyword ein Objekt erzeugt wird */
	@Test
	public void testeServiceVorhanden() {
		assertThat(ServiceFactory.getService(SERVICE1), not(equalTo(null)));
	}

	/** Stellt sicher, dass es sich bei einer neuen Instanz um neues Objekt handelt */
	@Test
	public void testeServicesUngleich() {
		var s1 = ServiceFactory.getService(SERVICE1);
		var s2 = ServiceFactory.getService(SERVICE1);
		assertThat(s1 == s2, not(equalTo(true)));
	}

	/** Eine ungültige Kennung erzeugt eine Exception */
	@Test
	public void testeUngueltigenService() {
		assertThrows(IllegalArgumentException.class, () -> ServiceFactory.getService("XYZ"));
	}

}

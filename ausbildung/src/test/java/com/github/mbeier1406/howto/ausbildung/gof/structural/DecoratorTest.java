package com.github.mbeier1406.howto.ausbildung.gof.structural;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Demonstriert das Decorator Pattern indem gezeigt
 * wird, wie die Dekoratoren flexibel kombiniert werden können,
 * ohne dass für die Serviceklasse Kombinationen von Ableitungen
 * erzeugt werden müssen.
 */
public class DecoratorTest {

	/** Das konkrete Objekt, das dekoriert wird */
	public static final DecoratorComponent c = new DecoratorComponentImpl();

	/** Prüfz die Kombination der Decorator */
	@ParameterizedTest
	@MethodSource("getTestdaten")
	public void testeDecorator(Decorator decorator, int ergebnis) {
		assertEquals(decorator.getZahl(), ergebnis);
	}

	/** Liefert die Testfälle */
	public static List<Arguments> getTestdaten() {
		return List.of(
			Arguments.of(new DecoratorPlusFuenf(new DecoratorPlusFuenf(new DecoratorMalZwei(new DecoratorPlusFuenf(c)))), 26),
			Arguments.of(new DecoratorPlusFuenf(new DecoratorMalZwei(new DecoratorPlusFuenf(c))), 21),
			Arguments.of(new DecoratorMalZwei(new DecoratorPlusFuenf(new DecoratorMalZwei(c))), 22),
			Arguments.of(new DecoratorMalZwei(new DecoratorPlusFuenf(c)), 16),
			Arguments.of(new DecoratorPlusFuenf(c), 8),
			Arguments.of(new DecoratorMalZwei(c), 6));
	}

}

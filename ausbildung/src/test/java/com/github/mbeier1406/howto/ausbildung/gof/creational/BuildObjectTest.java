package com.github.mbeier1406.howto.ausbildung.gof.creational;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests für die Klasse {@linkplain BuildObject}.
 */
public class BuildObjectTest {

	/** Führt den Builder aus und testet die Attribute des erzeugten Objekts */
	@ParameterizedTest
	@MethodSource("getTestData")
	public void testeBuilder(BuildObject.Builder builder, Matcher<BuildObject> matcher) {
		var buldObject = builder.build();
		System.out.println("buldObject="+buldObject);
		assertThat(buldObject, matcher);
	}

	/** Liefert die Testfälle für {@linkplain #testeBuilder(com.github.mbeier1406.howto.ausbildung.gof.creational.BuildObject.Builder, Matcher)} */
	public static List<Arguments> getTestData() {
		return List.of(
				Arguments.of(
						BuildObject.newBuilder(), // Kein Attribut gesetzt
						allOf(
								hasProperty("zahl1", nullValue()),
								hasProperty("zahl2", nullValue()),
								hasProperty("wahr", nullValue()),
								hasProperty("text", nullValue()))),
				Arguments.of(
						BuildObject.newBuilder().withText("text").withWahr(true).withZahl1(1).withZahl2(2L), // Alle Attribute gesetzt
						allOf(
								hasProperty("zahl1", equalTo(1)),
								hasProperty("zahl2", equalTo(2L)),
								hasProperty("wahr", equalTo(true)),
								hasProperty("text", equalTo("text")))),
				Arguments.of(
						BuildObject.newBuilder().withZahl1(1).withZahl2(2L), // Einige Attribute gesetzt
						allOf(
								hasProperty("zahl1", equalTo(1)),
								hasProperty("zahl2", equalTo(2L)),
								hasProperty("wahr", nullValue()),
								hasProperty("text", nullValue())))
				);
	}

}

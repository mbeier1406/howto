package com.github.mbeier1406.howto.ausbildung.rechner.token;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mbeier1406.howto.ausbildung.rechner.TokenReader;

/**
 * Klassen, die mit dieser Annotation ausgezeichnet sind,
 * werden zur Laufzeit als Token (Zahl, arithmetisch eOperation usw.)
 * der auszuwertenden mathematischen Formel erkannt.
 * @see TokenReader
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

	/** Informationen zum Token */
	public String info() default "";

}
